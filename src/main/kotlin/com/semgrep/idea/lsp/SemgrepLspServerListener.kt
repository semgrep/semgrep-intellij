package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerListener
import com.intellij.util.text.SemVer
import com.semgrep.idea.lsp.custom_requests.LoginStatusRequest
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.ui.SemgrepNotifier
import org.eclipse.lsp4j.InitializeResult

class SemgrepLspServerListener(val project: Project) : LspServerListener {

    fun checkNudge() {
        val settings = AppState.getInstance()
        val servers = SemgrepLspServer.getInstances(project)
        val first = servers.firstOrNull()
        // Check if we've bugged them about logging in
        if (first != null && !settings.pluginState.dismissedLoginNudge) {
            val loginStatusRequest = LoginStatusRequest(first)
            loginStatusRequest.sendRequest().handle({ it, _ ->
                if (!it.loggedIn) {
                    SemgrepNotifier(project).notifyLoginNudge()
                }
            })

        }
    }

    fun checkVersion() {
        val current = SemgrepInstaller.getCliVersion()
        val needed = SemVer.parseFromText(SemgrepLspServer.MIN_SEMGREP_VERSION)
        val latest = SemgrepInstaller.getMostUpToDateCliVersion()
        val settings = AppState.getInstance()
        if (current != null && !settings.lspSettings.ignoreCliVersion) {
            if (needed != null && current < needed) {
                SemgrepNotifier(project).notifyUpdateNeeded(needed, current)
            } else if (latest != null && current < latest) {
                SemgrepNotifier(project).notifyUpdateAvailable(current, latest)
            }
        }
    }

    fun checkNewInstall() {
        val settings = AppState.getInstance()
        if (settings.lspSettings.metrics.isNewAppInstall) {
            settings.lspSettings.metrics.isNewAppInstall = false
        }
    }

    override fun serverInitialized(params: InitializeResult) {
        super.serverInitialized(params)
        checkNudge()
        checkVersion()
        checkNewInstall()
    }
}