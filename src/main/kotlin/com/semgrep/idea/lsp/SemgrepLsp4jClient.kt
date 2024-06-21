package com.semgrep.idea.lsp

import com.google.gson.internal.LinkedTreeMap
import com.intellij.platform.lsp.api.Lsp4jClient
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.semgrep.idea.telemetry.LspErrorParams
import com.semgrep.idea.telemetry.SentryWrapper

class SemgrepLsp4jClient(serverNotificationsHandler: LspServerNotificationsHandler) : Lsp4jClient(
    serverNotificationsHandler
) {
    private val sentryInstance = SentryWrapper.getInstance()
    override fun telemetryEvent(`object`: Any) {
        val map = `object` as LinkedTreeMap<*, *>
        val params = LspErrorParams(
            map["message"] as String,
            map["name"] as String,
            map["stack"] as String
        )
        sentryInstance.captureLspError(params)
    }
}