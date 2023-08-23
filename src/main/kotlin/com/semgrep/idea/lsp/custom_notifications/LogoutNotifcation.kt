package com.semgrep.idea.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification

class LogoutNotifcation(private val server: com.semgrep.idea.lsp.SemgrepLspServer) : LspClientNotification(server) {
    override fun sendNotification() {
        server.lsp4jServer.logout()
    }

}