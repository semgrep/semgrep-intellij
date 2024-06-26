package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent

class ScanWorkspaceAction : LspAction("Scan Workspace with Semgrep") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        servers.forEach {
            it.notifyScanWorkspace(full = false)
        }
    }

}