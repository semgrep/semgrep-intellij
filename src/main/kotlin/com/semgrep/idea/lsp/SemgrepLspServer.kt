package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerDescriptor
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.intellij.platform.lsp.api.requests.LspRequestExecutor

class SemgrepLspServer(private val server:LspServer): LspServer {

     companion object{
        fun getInstances(project: Project): List<com.semgrep.idea.lsp.SemgrepLspServer>{
            val manager = LspServerManager.getInstance(project)
            val servers = manager.getServersForProvider(com.semgrep.idea.lsp.SemgrepLspServerSupportProvider::class.java)
            // There's prolly an easier way to do this but oh well
            return servers.filter { it.lsp4jServer is com.semgrep.idea.lsp.SemgrepLanguageServer }.map {
                com.semgrep.idea.lsp.SemgrepLspServer(
                    it
                )
            }
        }
    }

    override val descriptor: LspServerDescriptor
        get() = server.descriptor
    override val lsp4jServer: com.semgrep.idea.lsp.SemgrepLanguageServer
        get() = server.lsp4jServer as com.semgrep.idea.lsp.SemgrepLanguageServer
    override val project: Project
        get() = server.project
    override val requestExecutor: LspRequestExecutor
        get() = server.requestExecutor
    override val serverNotificationsHandler: LspServerNotificationsHandler
        get() = server.serverNotificationsHandler
}