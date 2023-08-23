package com.semgrep.idea.lsp

import com.semgrep.idea.lsp.custom_notifications.ScanWorkspaceParams
import com.semgrep.idea.lsp.custom_requests.LoginResult
import com.semgrep.idea.lsp.custom_requests.LoginStatusResult
import org.eclipse.lsp4j.InitializedParams
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
    fun loginFinish(params: LoginResult)

    // We need the List<Void> because of a bug in lsp4j
    // when params are not defined, they get set to null (instead of removing the field)
    // and the server throws an exception
    @JsonNotification("semgrep/logout")
    fun logout(params: List<Void> = emptyList())


    @JsonNotification("semgrep/scanWorkspace")
    fun scanWorkspace(params: ScanWorkspaceParams)

    @JsonNotification("semgrep/refreshRules")
    fun refreshRules(params: List<Void> = emptyList())
}