package com.semgrep.semgrep.lsp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.semgrep.lsp.SemgrepLspServer
import com.semgrep.semgrep.lsp.custom_notifications.ScanWorkspaceNotification
import com.semgrep.semgrep.lsp.custom_notifications.ScanWorkspaceParams

class ScanWorkspaceAction: LspAction() {
    override fun actionPerformed(e: AnActionEvent, servers: List<SemgrepLspServer>) {
        val params = ScanWorkspaceParams(full = false)
        servers.map { it.requestExecutor.sendNotification(ScanWorkspaceNotification(it, params)) }
    }

}