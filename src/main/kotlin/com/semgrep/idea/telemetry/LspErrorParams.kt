package com.semgrep.idea.telemetry

data class LspErrorParams(val message: String, val name: String, val stack: String)