package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent

class LogoutAction : LspAction("Sign out of Semgrep") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        servers.forEach {
            it.notifyLogout()
        }
    }
}