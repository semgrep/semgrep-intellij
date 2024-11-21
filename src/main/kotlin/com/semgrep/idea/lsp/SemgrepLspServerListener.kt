package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.platform.lsp.api.LspServerListener
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.telemetry.SentryWrapper
import com.semgrep.idea.ui.SemgrepNotifier
import kotlinx.coroutines.launch
import org.eclipse.lsp4j.InitializeResult

class SemgrepLspServerListener(
    private val project: Project,
    private val nativeDidSaveSupport: Boolean,
) : LspServerListener {

    fun checkNudge() {
        val settings = AppState.getInstance()
        // Check if we've bugged them about logging in
        if (settings.pluginState.dismissedLoginNudge) return
        val lspServer = SemgrepService.getRunningLspServer(project) ?: return
        SemgrepService.getInstance(project).cs.launch {
            val loginStatusResult = lspServer.sendRequest { (it as SemgrepLanguageServer).loginStatus() }
            val loggedIn = loginStatusResult != null && loginStatusResult.loggedIn
            settings.pluginState.loggedIn = loggedIn
            SentryWrapper.getInstance().setSentryContext()
            if (!loggedIn) {
                SemgrepNotifier(project).notifyLoginNudge()
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
        if (!nativeDidSaveSupport) {
            project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, FileSaveManager(project))
        }
        val sentry = SentryWrapper.getInstance()
        sentry.withSentry {
            checkNudge()
            checkVersion()
            checkNewInstall()
        }
    }
}