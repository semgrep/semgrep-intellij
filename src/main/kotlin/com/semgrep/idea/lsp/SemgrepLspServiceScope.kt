package com.semgrep.idea.lsp

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Service
class SemgrepLspServiceScope(
    private val cs: CoroutineScope
) {
    companion object {
        fun getInstance(): SemgrepLspServiceScope {
            return ApplicationManager.getApplication().getService(SemgrepLspServiceScope::class.java)
        }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        cs.launch(block = block)
    }
}