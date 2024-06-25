package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.SemgrepCustomNotification

class ScanWorkspaceAction : LspAction("Scan Workspace with Semgrep") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        val params = SemgrepCustomNotification.Companion.ScanWorkspaceParams(full = false)
        servers.forEach {
            it.notifyScanWorkspace(params)
        }
    }

}