package com.semgrep.idea.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.platform.lsp.api.LspServer
import com.semgrep.idea.lsp.custom_notifications.LoginFinishRequest
import com.semgrep.idea.lsp.custom_requests.LoginRequest

class LoginAction : LspAction("Sign In to Semgrep") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {
        val loginRequest = LoginRequest(servers.first())
        val response = (servers.first() as LspServer).requestExecutor.sendRequestSync(loginRequest) ?: return
        BrowserUtil.browse(response.url)
        servers.forEach {
            it.requestExecutor.sendNotification(LoginFinishRequest(it, response))
        }
    }
}