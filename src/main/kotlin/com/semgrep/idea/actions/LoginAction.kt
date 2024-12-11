package com.semgrep.idea.actions

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.platform.lsp.api.LspServer
import com.intellij.util.text.SemVer
import com.semgrep.idea.lsp.SemgrepInstaller
import com.semgrep.idea.lsp.SemgrepLanguageServer
import com.semgrep.idea.lsp.SemgrepService
import kotlinx.coroutines.launch

class LoginAction(private val notification: Notification? = null) : LspAction("Sign In with Semgrep") {
    @Suppress("DEPRECATION")
    override fun actionPerformed(lspServer: LspServer) {
        SemgrepService.getInstance(lspServer.project).cs.launch {
            if (SemVer("1.100.0",1, 100, 0) > SemgrepInstaller.getCliVersion()) {
                val loginResult = lspServer.sendRequest { (it as SemgrepLanguageServer).loginStart() }
                    ?: return@launch
                BrowserUtil.browse(loginResult.url)
                lspServer.sendRequest { (it as SemgrepLanguageServer).loginFinish(loginResult) }
            } else {
                val loginResult = lspServer.sendRequest { (it as SemgrepLanguageServer).legacyLoginStart() }
                    ?: return@launch
                BrowserUtil.browse(loginResult.url)
                lspServer.sendNotification {
                    (it as SemgrepLanguageServer).legacyLoginFinish(loginResult)
                }
            }
            notification?.expire()
        }
    }
}
