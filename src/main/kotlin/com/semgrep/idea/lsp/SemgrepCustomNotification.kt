package com.semgrep.idea.lsp

class SemgrepCustomNotification {
    companion object {
        data class ScanWorkspaceParams(val full: Boolean)

    }
}