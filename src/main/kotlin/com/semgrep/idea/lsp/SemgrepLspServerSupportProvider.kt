package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.telemetry.SentryWrapper

class SemgrepLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        val settingState = AppState.getInstance().lspSettings
        val sentry = SentryWrapper.getInstance()
        sentry.init()
        // checking if semgrep is installed has failed before... so let's wrap it with Sentry
        sentry.withSentry {
            val installed = settingState.useJS || SemgrepInstaller.semgrepInstalled()
            if (installed || AppState.getInstance().pluginState.handledInstallBanner) {
                serverStarter.ensureServerStarted(SemgrepLspServerDescriptor(project))
            }
        }

    }

}