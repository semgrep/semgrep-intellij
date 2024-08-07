package com.semgrep.idea.lsp

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.services.LanguageServer
import java.util.concurrent.CompletableFuture

interface SemgrepLanguageServer : LanguageServer {

    // Requests
    @JsonRequest("semgrep/login")
    fun login(params: List<Void> = emptyList()): CompletableFuture<SemgrepCustomRequest.Companion.LoginResult>

    @JsonRequest("semgrep/loginStatus")
    fun loginStatus(params: List<Void> = emptyList()): CompletableFuture<SemgrepCustomRequest.Companion.LoginStatusResult>

    // Notifications
    @JsonNotification("semgrep/loginFinish")
    fun loginFinish(params: SemgrepCustomRequest.Companion.LoginResult)

    // We need the List<Void> because of a bug in lsp4j
    // when params are not defined, they get set to null (instead of removing the field)
    // and the server throws an exception
    @JsonNotification("semgrep/logout")
    fun logout(params: List<Void> = emptyList())


    @JsonNotification("semgrep/scanWorkspace")
    fun scanWorkspace(params: SemgrepCustomNotification.Companion.ScanWorkspaceParams)

    @JsonNotification("semgrep/refreshRules")
    fun refreshRules(params: List<Void> = emptyList())

}