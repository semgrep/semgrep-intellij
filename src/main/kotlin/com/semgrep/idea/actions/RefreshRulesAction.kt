package com.semgrep.idea.actions

import com.intellij.platform.lsp.api.LspServer
import com.semgrep.idea.lsp.SemgrepLanguageServer

class RefreshRulesAction : LspAction("Update Semgrep Rules") {
    override fun actionPerformed(lspServer: LspServer) {
        lspServer.sendNotification { (it as SemgrepLanguageServer).refreshRules() }
    }
}