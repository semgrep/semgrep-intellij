package com.semgrep.idea.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification

data class ScanWorkspaceParams(val full: Boolean)
class ScanWorkspaceNotification(
    private val server: com.semgrep.idea.lsp.SemgrepLspServer,
    private val params: ScanWorkspaceParams
) : LspClientNotification(server) {
    override fun sendNotification() {
        server.lsp4jServer.scanWorkspace(params)
    }
}