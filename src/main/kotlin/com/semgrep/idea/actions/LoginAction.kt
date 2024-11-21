package com.semgrep.idea.actions

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.platform.lsp.api.LspServer
import com.semgrep.idea.lsp.SemgrepLanguageServer
import com.semgrep.idea.lsp.SemgrepService
import kotlinx.coroutines.launch

class LoginAction(private val notification: Notification? = null) : LspAction("Sign In with Semgrep") {
    override fun actionPerformed(lspServer: LspServer) {
        SemgrepService.getInstance(lspServer.project).cs.launch {
            val loginResult = lspServer.sendRequest { (it as SemgrepLanguageServer).login() }
                ?: return@launch
            BrowserUtil.browse(loginResult.url)
            lspServer.sendNotification { (it as SemgrepLanguageServer).loginFinish(loginResult) }
            notification?.expire()
        }
    }
}