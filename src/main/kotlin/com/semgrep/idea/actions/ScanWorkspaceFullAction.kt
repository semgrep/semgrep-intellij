package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent

class ScanWorkspaceFullAction : LspAction("Scan Workspace with Semgrep (Including Unmodified Files)") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        servers.forEach { it.notifyScanWorkspace(full = true) }
    }

}