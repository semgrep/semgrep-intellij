package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerDescriptor
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.intellij.platform.lsp.api.requests.LspRequestExecutor

class SemgrepLspServer(private val server: LspServer) : LspServer {
    companion object {
        fun getInstances(project: Project): List<SemgrepLspServer> {
            val manager = LspServerManager.getInstance(project)
            val servers = manager.getServersForProvider(SemgrepLspServerSupportProvider::class.java)
            // There's prolly an easier way to do this but oh well
            return servers.filter { it.lsp4jServer is SemgrepLanguageServer }.map {
                SemgrepLspServer(
                    it
                )
            }
        }
    }

    override val descriptor: LspServerDescriptor
        get() = server.descriptor
    override val lsp4jServer: SemgrepLanguageServer
        get() = server.lsp4jServer as SemgrepLanguageServer
    override val project: Project
        get() = server.project
    override val requestExecutor: LspRequestExecutor
        get() = server.requestExecutor
    override val serverNotificationsHandler: LspServerNotificationsHandler
        get() = server.serverNotificationsHandler
}