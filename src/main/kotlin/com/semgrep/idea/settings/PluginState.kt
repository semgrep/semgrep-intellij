package com.semgrep.idea.settings

data class PluginState(
    var dismissedLoginNudge: Boolean = false,
    var handledInstallBanner: Boolean = false,
    var loggedIn: Boolean = false,
    var semgrepVersion: String = "unknown"
)