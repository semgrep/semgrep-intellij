package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerListener
import com.intellij.util.text.SemVer
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
                    SemgrepNotifier(project).notifyLoginNudge()
                }
            })
            val current = SemgrepInstaller.getCliVersion()
            val needed = SemVer.parseFromText(SemgrepLspServer.MIN_SEMGREP_VERSION)
            val latest = SemVer.parseFromText(SemgrepLspServer.LATEST_SEMGREP_VERSION)
            if (current != null){
                if (needed!= null && current < needed) {
                    SemgrepNotifier(project).notifyUpdateNeeded(needed,current)
                } else if (latest!=null && current < latest) {
                    SemgrepNotifier(project).notifyUpdateAvailable(current, latest)
                }
            }
        }
    }
}