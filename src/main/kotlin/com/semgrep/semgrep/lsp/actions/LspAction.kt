package com.semgrep.semgrep.lsp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.semgrep.lsp.SemgrepLspServer

abstract class LspAction:AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val servers = SemgrepLspServer.getInstances(project)
        if (servers.isEmpty()){
            return
        }
        actionPerformed(e, servers)
    }
    abstract fun actionPerformed(e: AnActionEvent, servers: List<SemgrepLspServer>);
}