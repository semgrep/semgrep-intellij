package com.semgrep.idea.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification
import com.semgrep.idea.lsp.custom_requests.LoginResult

class LoginFinishRequest(private val server: com.semgrep.idea.lsp.SemgrepLspServer, private val params: LoginResult) :
    LspClientNotification(server) {
    override fun sendNotification() {
        server.lsp4jServer.loginFinish(params)
    }
}
