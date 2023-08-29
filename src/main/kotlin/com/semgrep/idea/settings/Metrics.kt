package com.semgrep.idea.settings

data class Metrics(
    val machineId: String,
    val isNewAppInstall: Boolean,
    val sessionId: String,
    val extensionVersion: String,
    val extenstionType: String = "intellij",
    var enabled: Boolean = true,
)