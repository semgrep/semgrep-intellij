package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerListener
import com.semgrep.idea.lsp.custom_requests.LoginStatusRequest
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.ui.SemgrepNotifier
import org.eclipse.lsp4j.InitializeResult

class SemgrepLspServerListener(val project: Project) : LspServerListener {
    override fun serverInitialized(params: InitializeResult) {
        super.serverInitialized(params)
        val settings = AppState.getInstance()
        val servers = SemgrepLspServer.getInstances(project)
        val first = servers.firstOrNull()
        if (first != null && !settings.pluginState.dismissedLoginNudge) {
            val loginStatusRequest = LoginStatusRequest(first)
            loginStatusRequest.sendRequest().handle({ it, _ ->
                if (!it.loggedIn) {
                    SemgrepNotifier.notifyLoginNudge(project)
                }
            })
        }
    }
}