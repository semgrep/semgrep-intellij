package com.semgrep.idea.actions

import com.intellij.platform.lsp.api.LspServer
import com.semgrep.idea.lsp.SemgrepLanguageServer

class ScanWorkspaceAction : LspAction("Scan Workspace with Semgrep") {
    override fun actionPerformed(lspServer: LspServer) {
        val params = SemgrepLanguageServer.ScanWorkspaceParams(full = false)
        lspServer.sendNotification { (it as SemgrepLanguageServer).scanWorkspace(params) }
    }
}