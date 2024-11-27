package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.platform.lsp.api.LspServer
import com.semgrep.idea.lsp.SemgrepService

abstract class LspAction(text: String) : AnAction(text) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val lspServer = SemgrepService.getRunningLspServer(project) ?: return
        actionPerformed(lspServer)
    }

    abstract fun actionPerformed(lspServer: LspServer)
}