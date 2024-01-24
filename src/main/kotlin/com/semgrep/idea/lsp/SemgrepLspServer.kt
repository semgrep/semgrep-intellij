package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerManager


class SemgrepLspServer(val server: LspServer) {
    companion object {
        val MIN_SEMGREP_VERSION = "1.21.0"

        // These should probably be split off into SemgrepLspManager or something
        private fun getManager(project: Project): LspServerManager {
            return LspServerManager.getInstance(project)
        }

        fun startServersIfNeeded(project: Project) {
            val manager = getManager(project)
            manager.stopAndRestartIfNeeded(SemgrepLspServerSupportProvider::class.java)
        }

        fun getInstances(project: Project): List<SemgrepLspServer> {
            val manager = getManager(project)
            val servers = manager.getServersForProvider(SemgrepLspServerSupportProvider::class.java)
            // There's prolly an easier way to do this but oh well
            return servers.filter { it.lsp4jServer is SemgrepLanguageServer }.map {
                SemgrepLspServer(
                    it
                )
            }
        }
    }

    val lsp4jServer = server.lsp4jServer as SemgrepLanguageServer
}