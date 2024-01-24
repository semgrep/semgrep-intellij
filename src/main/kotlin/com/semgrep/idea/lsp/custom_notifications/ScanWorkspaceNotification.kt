package com.semgrep.idea.lsp.custom_notifications

import com.intellij.platform.lsp.api.requests.LspClientNotification

data class ScanWorkspaceParams(val full: Boolean)
class ScanWorkspaceNotification(
    private val semgrepServer: com.semgrep.idea.lsp.SemgrepLspServer,
    private val params: ScanWorkspaceParams
) : LspClientNotification(semgrepServer.server) {
    override fun sendNotification() {
        semgrepServer.lsp4jServer.scanWorkspace(params)
    }
}