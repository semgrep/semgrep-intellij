package com.semgrep.idea.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification
import com.semgrep.idea.lsp.custom_requests.LoginResult

class LoginFinishRequest(
    private val semgrepServer: com.semgrep.idea.lsp.SemgrepLspServer,
    private val params: LoginResult
) :
    LspClientNotification(semgrepServer.server) {
    override fun sendNotification() {
        semgrepServer.lsp4jServer.loginFinish(params)
    }
}
