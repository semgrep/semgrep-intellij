package com.semgrep.idea.lsp

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerState
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class SemgrepService(val cs: CoroutineScope) {
    companion object {
        fun getInstance(project: Project): SemgrepService = project.service<SemgrepService>()

        // There might be no more than one running server
        // because `SemgrepLspServerDescriptor` extends `ProjectWideLspServerDescriptor`
        fun getRunningLspServer(project: Project): LspServer? =
            LspServerManager.getInstance(project)
                .getServersForProvider(SemgrepLspServerSupportProvider::class.java)
                .firstOrNull { it.state == LspServerState.Running }
    }
}
