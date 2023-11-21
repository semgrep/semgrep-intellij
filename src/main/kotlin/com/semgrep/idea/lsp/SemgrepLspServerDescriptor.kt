package com.semgrep.idea.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerListener
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.settings.SemgrepLspSettings
import com.semgrep.idea.settings.TraceLevel
import org.eclipse.lsp4j.services.LanguageServer

class SemgrepLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Semgrep") {
    override val lsp4jServerClass: Class<out LanguageServer> = SemgrepLanguageServer::class.java
    override fun createCommandLine(): GeneralCommandLine {
        val settingState = AppState.getInstance().lspSettings
        return GeneralCommandLine(settingState.path).apply {
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            withCharset(Charsets.UTF_8)
            addParameter("lsp")
            if (settingState.trace.server == TraceLevel.VERBOSE){
                addParameter("--debug")
            }
        }
    }

    override fun isSupportedFile(file: VirtualFile): Boolean {
        return true
    }

    override fun createInitializationOptions(): Any {
        return AppState.getInstance().lspSettings
    }

    override val lspServerListener: LspServerListener = SemgrepLspServerListener(project)
}