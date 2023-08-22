package com.semgrep.semgrep.lsp.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.semgrep.lsp.SemgrepLspServer
import com.semgrep.semgrep.lsp.custom_notifications.RefreshRulesNotification

class RefreshRulesAction: LspAction(){
    override fun actionPerformed(e: AnActionEvent, servers: List<SemgrepLspServer>) {
        servers.map { it.requestExecutor.sendNotification(RefreshRulesNotification(it)) }
    }
}