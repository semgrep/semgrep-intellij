package com.semgrep.idea.actions

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.lsp.SemgrepLspServiceScope

class LoginAction(private val notification: Notification? = null) : LspAction("Sign In with Semgrep") {
    override fun actionPerformed(e: AnActionEvent, servers: List<com.semgrep.idea.lsp.SemgrepLspServer>) {

        SemgrepLspServiceScope.getInstance().launch {
            servers.forEach {
                val response = it.requestLogin()!!
                BrowserUtil.browse(response.url)
                servers.forEach {
                    it.notifyLoginFinish(response.url, response.sessionId)
                }
                notification?.expire()
            }
        }

    }

}