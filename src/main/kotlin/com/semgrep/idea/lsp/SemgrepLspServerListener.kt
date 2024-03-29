package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
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
        val settings = AppState.getInstance()
        if (settings.lspSettings.useJS) return
        val current = SemgrepInstaller.getCliVersion()
        val needed = SemVer.parseFromText(SemgrepLspServer.MIN_SEMGREP_VERSION)
        val latest = SemgrepInstaller.getMostUpToDateCliVersion()
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
        SemgrepLspServer.getInstances(project).forEach {
            project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, FileSaveManager(it))
        }
        checkNudge()
        checkVersion()
        checkNewInstall()
    }
}