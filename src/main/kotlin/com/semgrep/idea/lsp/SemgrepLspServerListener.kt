package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.platform.lsp.api.LspServerListener
import com.semgrep.idea.lsp.custom_requests.LoginStatusRequest
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.telemetry.SentryWrapper
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
            loginStatusRequest.sendRequest().handle { it, _ ->
                settings.pluginState.loggedIn = it.loggedIn
                SentryWrapper.getInstance().setSentryContext()
                if (!it.loggedIn) {
                    SemgrepNotifier(project).notifyLoginNudge()
                }
            }

        }
    }

    fun checkVersion() {
        val settings = AppState.getInstance()
        if (settings.lspSettings.useJS) return
        val current = SemgrepInstaller.getCliVersion()
        val versionInfo = SemgrepInstaller.getMostUpToDateCliVersion()
        if (versionInfo != null && current != null && !settings.lspSettings.ignoreCliVersion) {
            val latest = versionInfo.latest
            val needed = versionInfo.min
            settings.pluginState.semgrepVersion = current.toString()
            SentryWrapper.getInstance().setSentryContext()
            if (current < needed) {
                SemgrepNotifier(project).notifyUpdateNeeded(needed, current)
            } else if (current < latest) {
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
        val sentry = SentryWrapper.getInstance()

        SemgrepLspServer.getInstances(project).forEach {
            project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, FileSaveManager(it))
        }
        sentry.withSentry {
            checkNudge()
            checkVersion()
            checkNewInstall()
        }
    }
}