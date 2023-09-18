package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.custom_notifications.LogoutNotifcation

class LogoutAction : LspAction("Sign out of Semgrep") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        servers.map {
            LogoutNotifcation(it).sendNotification()
        }
    }
}