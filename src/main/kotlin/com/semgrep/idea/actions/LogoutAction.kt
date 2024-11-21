package com.semgrep.idea.actions

import com.intellij.platform.lsp.api.LspServer
import com.semgrep.idea.lsp.SemgrepLanguageServer

class LogoutAction : LspAction("Sign out of Semgrep") {
    override fun actionPerformed(lspServer: LspServer) {
        lspServer.sendNotification { (it as SemgrepLanguageServer).logout() }
    }
}