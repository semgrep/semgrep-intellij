package com.semgrep.semgrep.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification
import com.semgrep.semgrep.lsp.SemgrepLspServer

class LogoutNotifcation(private val server: SemgrepLspServer): LspClientNotification(server){
    override fun sendNotification() {
        server.lsp4jServer.logout()
    }

}