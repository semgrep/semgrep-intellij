package com.semgrep.idea.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.util.text.SemVer
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.settings.SemgrepPluginSettings
import com.semgrep.idea.ui.SemgrepNotifier

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