package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

abstract class LspAction(text: String) : AnAction(text) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val servers = com.semgrep.idea.lsp.SemgrepLspServer.getInstances(project)
        if (servers.isEmpty()) {
            return
        }
        actionPerformed(e, servers)
    }

    abstract fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>)
}