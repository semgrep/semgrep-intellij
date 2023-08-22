package com.semgrep.semgrep.lsp.custom_requests

import com.intellij.platform.lsp.api.requests.LspRequest
import com.semgrep.semgrep.lsp.SemgrepLspServer
import java.util.concurrent.CompletableFuture

data class LoginStatusResult(val loggedIn: Boolean)

class LoginStatusRequest(private val server: SemgrepLspServer): LspRequest<LoginStatusResult,LoginStatusResult>(server) {
    override fun preprocessResponse(serverResponse: LoginStatusResult): LoginStatusResult {
        return serverResponse
    }

    override fun sendRequest(): CompletableFuture<LoginStatusResult> {
        return server.lsp4jServer.loginStatus()
    }

}