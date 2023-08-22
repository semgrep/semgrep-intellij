package com.semgrep.semgrep.lsp.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.platform.lsp.api.LspServer
import com.semgrep.semgrep.lsp.SemgrepLspServer
import com.semgrep.semgrep.lsp.custom_notifications.LoginFinishRequest
import com.semgrep.semgrep.lsp.custom_requests.LoginRequest

class LoginAction: LspAction() {
    override fun actionPerformed(e: AnActionEvent, servers: List<SemgrepLspServer>) {
        val loginRequest = LoginRequest(servers.first())
        val response = (servers.first() as LspServer).requestExecutor.sendRequestSync(loginRequest)?: return
        BrowserUtil.browse(response.url)
        servers.forEach {
            it.requestExecutor.sendNotification(LoginFinishRequest(it, response))
        }
    }
}