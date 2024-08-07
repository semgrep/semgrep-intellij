package com.semgrep.idea.lsp

import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerManager
import org.eclipse.lsp4j.DidSaveTextDocumentParams
import org.eclipse.lsp4j.TextDocumentIdentifier


class SemgrepLspServer(private val server: LspServer) {
    companion object {
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
            return servers.filter { it.descriptor is SemgrepLspServerDescriptor }.map {
                SemgrepLspServer(
                    it
                )
            }
        }
    }

    // notifications
    fun notifyLogout() {
        server.sendNotification {
            (server as SemgrepLanguageServer).logout()
        }
    }

    fun notifyLoginFinish(url: String, sessionId: String) {
        val params = SemgrepCustomRequest.Companion.LoginResult(url, sessionId)
        server.sendNotification {
            (it as SemgrepLanguageServer).loginFinish(params)
        }
    }

    fun notifyRefreshRules() {
        server.sendNotification {
            (it as SemgrepLanguageServer).refreshRules()
        }
    }

    fun notifyScanWorkspace(full: Boolean) {
        val params = SemgrepCustomNotification.Companion.ScanWorkspaceParams(full)
        server.sendNotification {
            (it as SemgrepLanguageServer).scanWorkspace(params)
        }
    }

    fun notifyDidSave(uri: String) {
        val params = DidSaveTextDocumentParams(TextDocumentIdentifier(uri))
        server.sendNotification {
            it.textDocumentService.didSave(params)
        }
    }

    // requests
    suspend fun requestLogin(): SemgrepCustomRequest.Companion.LoginResult? {
        return server.sendRequest {
            (it as SemgrepLanguageServer).login()
        }
    }

    suspend fun requestLoginStatus(): SemgrepCustomRequest.Companion.LoginStatusResult? {
        return server.sendRequest {
            (it as SemgrepLanguageServer).loginStatus()
        }
    }



}