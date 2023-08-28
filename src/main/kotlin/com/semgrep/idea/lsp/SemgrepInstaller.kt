package com.semgrep.idea.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.util.text.SemVer
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.settings.SemgrepPluginSettings
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
        val defaultPath = SemgrepPluginSettings().path
        val state = AppState.getInstance().appSettings
        return state.path != defaultPath && which(defaultPath) != null
    }

    fun getCliVersion(): SemVer? {
        val cmd = GeneralCommandLine("semgrep", "--version")
        val process = cmd.createProcess()
        process.waitFor()
        val out = process.inputStream.bufferedReader().readText().trim()
        return SemVer.parseFromText(out)
    }

    fun getMostUpToDateCliVersion(): SemVer? {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://semgrep.dev/api/check-version"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
        // Can't figure out how to actually parse this to a string, not a quoted string. Oh well
        val version = Json.parseToJsonElement(response).jsonObject.get("latest").toString().replace("\"", "")
        return SemVer.parseFromText(version)
    }

    fun which(binary: String): String? {
        val cmd = GeneralCommandLine("which", binary)

        val process = cmd.createProcess()
        process.waitFor()
        val result = process.inputStream.bufferedReader().readLine()

        return if (result == "") null else result
    }

    fun getInstallOptions(): List<InstallOption> {
        return InstallOption.values().filter { it.isInstalled() }
    }


}