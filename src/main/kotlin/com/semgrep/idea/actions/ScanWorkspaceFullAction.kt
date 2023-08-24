package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.custom_notifications.ScanWorkspaceNotification
import com.semgrep.idea.lsp.custom_notifications.ScanWorkspaceParams

class ScanWorkspaceFullAction : LspAction("Scan Workspace with Semgrep (Full)") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        val params = ScanWorkspaceParams(full = true)
        servers.map { it.requestExecutor.sendNotification(ScanWorkspaceNotification(it, params)) }
    }

}