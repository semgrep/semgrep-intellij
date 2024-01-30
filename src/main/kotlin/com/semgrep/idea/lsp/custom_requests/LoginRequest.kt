package com.semgrep.idea.lsp.custom_requests

import com.intellij.platform.lsp.api.requests.LspRequest
import java.util.concurrent.CompletableFuture

data class LoginResult(val url: String, val sessionId: String)

class LoginRequest(private val semgrepServer: com.semgrep.idea.lsp.SemgrepLspServer) :
    LspRequest<LoginResult, LoginResult>(semgrepServer.server) {
    override fun preprocessResponse(serverResponse: LoginResult): LoginResult {
        return serverResponse
    }

    override fun sendRequest(): CompletableFuture<LoginResult> {
        return semgrepServer.lsp4jServer.login()
    }
}