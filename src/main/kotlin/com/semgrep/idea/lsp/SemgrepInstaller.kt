package com.semgrep.idea.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreter
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
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
    enum class InstallOption(val binary: String, val installCommand: String) {
        BREW("brew", "brew install semgrep"),
        PIP("pip3", "pip3 install semgrep");

        fun isInstalled(): Boolean {
            return which(binary) != null
        }

        fun install(project: Project) {
            val cmd = GeneralCommandLine("sh", "-c", installCommand)
            val process = cmd.createProcess()
            val ret = process.waitFor()
            val out = process.inputStream.bufferedReader().readText()
            val semgrepNotifier = SemgrepNotifier(project)
            if (ret == 0) {
                semgrepNotifier.notifyInstallSuccess()
                SemgrepLspServer.startServersIfNeeded(project)
            } else {
                semgrepNotifier.notifyInstallFailure(out, ret)
            }
        }
    }

    fun semgrepInstalled(): Boolean {
        val defaultPath = SemgrepLspSettings().path
        val state = AppState.getInstance().lspSettings
        return state.path.isEmpty() || (state.path == "semgrep" && which(state.path) != null)
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
        val cmd = GeneralCommandLine("which", binary)

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