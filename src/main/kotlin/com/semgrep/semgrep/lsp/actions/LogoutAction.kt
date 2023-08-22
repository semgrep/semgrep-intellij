package com.semgrep.semgrep.lsp.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.semgrep.lsp.SemgrepLspServer
import com.semgrep.semgrep.lsp.custom_notifications.LogoutNotifcation

class LogoutAction:LspAction(){
    override fun actionPerformed(e: AnActionEvent, servers: List<SemgrepLspServer>) {
        val logoutNotifcation = LogoutNotifcation(servers.first())
        servers.map {
            it.requestExecutor.sendNotification(LogoutNotifcation(it))
        }
    }
}