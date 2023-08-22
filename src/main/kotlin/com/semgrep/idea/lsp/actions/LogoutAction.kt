package com.semgrep.idea.lsp.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.custom_notifications.LogoutNotifcation

class LogoutAction:LspAction(){
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        val logoutNotifcation = LogoutNotifcation(servers.first())
        servers.map {
            it.requestExecutor.sendNotification(LogoutNotifcation(it))
        }
    }
}