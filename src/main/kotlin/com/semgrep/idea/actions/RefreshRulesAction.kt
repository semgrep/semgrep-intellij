package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.custom_notifications.RefreshRulesNotification

class RefreshRulesAction : LspAction("Update Semgrep Rules") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        servers.map {
            RefreshRulesNotification(it).sendNotification()
        }
    }
}