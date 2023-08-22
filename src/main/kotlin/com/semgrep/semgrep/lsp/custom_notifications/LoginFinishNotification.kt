package com.semgrep.semgrep.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification
import com.semgrep.semgrep.lsp.SemgrepLspServer
import com.semgrep.semgrep.lsp.custom_requests.LoginResult

class LoginFinishRequest(private val server:SemgrepLspServer, private val params: LoginResult): LspClientNotification(server) {
    override fun sendNotification() {
        server.lsp4jServer.loginFinish(params)
    }
}
