package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent

class RefreshRulesAction : LspAction("Update Semgrep Rules") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        servers.forEach {
            it.notifyRefreshRules()
        }
    }
}