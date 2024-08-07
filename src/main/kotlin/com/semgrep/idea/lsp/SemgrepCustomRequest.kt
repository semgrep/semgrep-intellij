package com.semgrep.idea.lsp

class SemgrepCustomRequest {
    companion object {
        data class LoginStatusResult(val loggedIn: Boolean)

        data class LoginResult(val url: String, val sessionId: String)

    }
}