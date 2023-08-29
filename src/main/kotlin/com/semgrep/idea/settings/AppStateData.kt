package com.semgrep.idea.settings

data class AppStateData(
    var appSettings: SemgrepLspSettings = SemgrepLspSettings(),
    var pluginState: PluginState = PluginState()
)