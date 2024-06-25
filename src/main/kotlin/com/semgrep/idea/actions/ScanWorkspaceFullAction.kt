package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.SemgrepCustomNotification

class ScanWorkspaceFullAction : LspAction("Scan Workspace with Semgrep (Including Unmodified Files)") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        val params = SemgrepCustomNotification.Companion.ScanWorkspaceParams(full = true)
        servers.forEach { it.notifyScanWorkspace(params) }
    }

}