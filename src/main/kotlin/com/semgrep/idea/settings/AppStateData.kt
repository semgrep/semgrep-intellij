package com.semgrep.idea.settings

import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreter

data class AppStateData(
    var appSettings: SemgrepLspSettings = SemgrepLspSettings(),
    var pluginState: PluginState = PluginState(),
    var nodeJsInterpreter: NodeJsInterpreter? = null
)