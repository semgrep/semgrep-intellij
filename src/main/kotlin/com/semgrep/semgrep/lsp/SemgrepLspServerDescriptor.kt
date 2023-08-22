package com.semgrep.semgrep.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.semgrep.semgrep.lsp.settings.AppSettingsState
import org.eclipse.lsp4j.services.LanguageServer

class SemgrepLspServerDescriptor(project:Project):ProjectWideLspServerDescriptor(project,"Semgrep") {
    override val lsp4jServerClass: Class<out LanguageServer> = SemgrepLanguageServer::class.java
    override fun createCommandLine(): GeneralCommandLine {
        val settingState = AppSettingsState.getInstance().settings
        return GeneralCommandLine(settingState.path).apply {
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            withCharset(Charsets.UTF_8)
            addParameter("lsp")
        }
    }

    override fun isSupportedFile(file: VirtualFile): Boolean {
        return true
    }

    override fun createInitializationOptions(): Any {
        return AppSettingsState.getInstance().settings
    }
}