package com.semgrep.idea.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreter
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.util.text.SemVer
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.settings.SemgrepLspSettings
import com.semgrep.idea.ui.SemgrepNotifier
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object SemgrepInstaller {
    enum class InstallOption(val binary: String, vararg val args: String) {
        BREW("brew", "install", "semgrep"),
        PIP("pip3", "install", "semgrep");

        fun isInstalled(): Boolean {
            return which(binary) != null
        }

        fun install(project: Project) {
            // Run the whole operation in an IntelliJ background task
            ProgressManager.getInstance().run(object : Task.Backgroundable(
                project,
                "Installing Semgrep CLI",
		true, /* canBeCancelled */
            ) {
                override fun run(indicator: ProgressIndicator) {
                    indicator.text = "Running $binary ${args.joinToString(" ")}"

                    val cmd = GeneralCommandLine(binary).withParameters(*args).apply {
                        isRedirectErrorStream = true
                        withCharset(Charsets.UTF_8)
                    }

                    // CapturingProcessHandler consumes output while the process is alive
                    val handler = CapturingProcessHandler(cmd)
                    val output: ProcessOutput = handler.runProcess(300000)  // timeout in 5 minutes

                    // Switch back to EDT for any UI updates / notifications
                    ApplicationManager.getApplication().invokeLater {
                        val notifier = SemgrepNotifier(project)
                        if (output.exitCode == 0) {
                            notifier.notifyInstallSuccess()
                            LspServerManager.getInstance(project)
                                .stopAndRestartIfNeeded(SemgrepLspServerSupportProvider::class.java)
                        } else {
                            notifier.notifyInstallFailure(output.stdout.trim(), output.exitCode)
                        }
                    }
                }
            })
        }
    }

    fun semgrepInstalled(): Boolean {
        val defaultPath = SemgrepLspSettings().path
        val state = AppState.getInstance().lspSettings
        return state.path != defaultPath || which(defaultPath) != null
    }

    fun getCliVersion(): SemVer? {
        val cmd = GeneralCommandLine("semgrep", "--version")
        val process = cmd.createProcess()
        process.waitFor()
        val out = process.inputStream.bufferedReader().readText().trim()
        return SemVer.parseFromText(out)
    }

    data class VersionCheckInfo(val min: SemVer, val latest: SemVer)

    fun getMostUpToDateCliVersion(): VersionCheckInfo? {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://semgrep.dev/api/check-version"))
            .build()
        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
            val versionObject = Json.parseToJsonElement(response).jsonObject
            val latestVersion = versionObject["latest"].toString().replace("\"", "")
            val minVersion = versionObject["versions"]!!.jsonObject["minimum"].toString().replace("\"", "")
            return VersionCheckInfo(SemVer.parseFromText(minVersion)!!, SemVer.parseFromText(latestVersion)!!)
        } catch (e: Exception) {
            return null
        }
    }

    fun which(binary: String): String? {
        val command = if (isWindows()) "where" else "which"
        val cmd = GeneralCommandLine(command, binary)

        val process = cmd.createProcess()
        process.waitFor()
        val result = process.inputStream.bufferedReader().readLine()

        return if (result == "") null else result
    }

    fun isWindows(): Boolean {
        return SystemInfo.isWindows
    }

    fun getInstallOptions(): List<InstallOption> {
        return InstallOption.values().filter { it.isInstalled() }
    }

    fun getNodeInterpreter(project: Project): NodeJsInterpreter? {
        val interpreter = NodeJsInterpreterManager.getInstance(project).interpreter
        if (interpreter == null) {
            SemgrepNotifier(project).notifyJSInterpreterNeeded()
            return null
        }
        return interpreter

    }

}