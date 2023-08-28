package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.semgrep.idea.settings.AppState

class SemgrepLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        val installed = SemgrepInstaller.semgrepInstalled()
        if (installed || AppState.getInstance().pluginState.handledInstallBanner) {
            serverStarter.ensureServerStarted(SemgrepLspServerDescriptor(project))
        }
    }

}