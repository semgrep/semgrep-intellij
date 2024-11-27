package com.semgrep.idea.actions

import com.intellij.platform.lsp.api.LspServer
import com.semgrep.idea.lsp.SemgrepLanguageServer

class ScanWorkspaceFullAction : LspAction("Scan Workspace with Semgrep (Including Unmodified Files)") {
    override fun actionPerformed(lspServer: LspServer) {
        val params = SemgrepLanguageServer.ScanWorkspaceParams(full = true)
        lspServer.sendNotification { (it as SemgrepLanguageServer).scanWorkspace(params) }
    }
}