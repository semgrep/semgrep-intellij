package com.semgrep.idea.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification

class LogoutNotifcation(private val semgrepServer: com.semgrep.idea.lsp.SemgrepLspServer) :
    LspClientNotification(semgrepServer.server) {
    override fun sendNotification() {
        semgrepServer.lsp4jServer.logout()
    }

}