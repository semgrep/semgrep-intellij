package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider

class SemgrepLspServerSupportProvider: LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        serverStarter.ensureServerStarted(SemgrepLspServerDescriptor(project))
    }

}