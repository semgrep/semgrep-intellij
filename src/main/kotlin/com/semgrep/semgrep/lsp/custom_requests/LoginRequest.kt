package com.semgrep.semgrep.lsp.custom_requests

import com.intellij.platform.lsp.api.requests.LspRequest
import com.semgrep.semgrep.lsp.SemgrepLspServer
import java.util.concurrent.CompletableFuture
data class LoginResult(val url:String, val sessionId:String)

class LoginRequest(private val server:SemgrepLspServer): LspRequest<LoginResult, LoginResult>(server) {
    override fun preprocessResponse(serverResponse: LoginResult): LoginResult {
        return serverResponse
    }

    override fun sendRequest(): CompletableFuture<LoginResult> {
        return server.lsp4jServer.login()
    }
}