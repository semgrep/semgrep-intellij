package com.semgrep.idea.lsp

import com.semgrep.idea.lsp.custom_notifications.ScanWorkspaceParams
import com.semgrep.idea.lsp.custom_requests.LoginResult
import com.semgrep.idea.lsp.custom_requests.LoginStatusResult
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.services.LanguageServer
import java.util.concurrent.CompletableFuture

interface SemgrepLanguageServer:LanguageServer {
    // Requests
    @JsonRequest("semgrep/login")
    fun login():CompletableFuture<LoginResult>

    @JsonRequest("semgrep/loginStatus")
    fun loginStatus():CompletableFuture<LoginStatusResult>

    // Notifications
    @JsonNotification("semgrep/loginFinish")
    fun loginFinish(params: LoginResult):CompletableFuture<Void>


    @JsonNotification("semgrep/logout")
    fun logout():CompletableFuture<Void>


    @JsonNotification("semgrep/scanWorkspace")
    fun scanWorkspace(params: ScanWorkspaceParams):CompletableFuture<Void>

    @JsonNotification("semgrep/refreshRules")
    fun refreshRules():CompletableFuture<Void>
}