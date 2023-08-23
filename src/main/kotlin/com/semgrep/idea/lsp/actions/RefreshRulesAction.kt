package com.semgrep.idea.lsp.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.custom_notifications.RefreshRulesNotification
import org.eclipse.lsp4j.DidChangeTextDocumentParams

class RefreshRulesAction: LspAction(){
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        servers.map { it.requestExecutor.sendNotification(RefreshRulesNotification(it))
        it.lsp4jServer.textDocumentService.didChange(DidChangeTextDocumentParams())}
    }
}