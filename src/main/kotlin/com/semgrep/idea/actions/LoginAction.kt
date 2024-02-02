package com.semgrep.idea.actions

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.custom_notifications.LoginFinishRequest
import com.semgrep.idea.lsp.custom_requests.LoginRequest

class LoginAction(private val notification: Notification? = null) : LspAction("Sign In with Semgrep") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        val loginRequest = LoginRequest(servers.first())
        val response = loginRequest.sendRequest().join() ?: return
        BrowserUtil.browse(response.url)
        servers.forEach {
            LoginFinishRequest(it, response).sendNotification()
        }
        notification?.expire()
    }
}