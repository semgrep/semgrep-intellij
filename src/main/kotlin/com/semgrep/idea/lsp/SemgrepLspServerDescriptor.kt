package com.semgrep.idea.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.javascript.nodejs.interpreter.NodeCommandLineConfigurator
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterManager
import com.intellij.lang.javascript.service.JSLanguageServiceUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerListener
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.semgrep.idea.settings.AppState
import com.semgrep.idea.settings.SemgrepLspSettings
import com.semgrep.idea.settings.TraceLevel
import com.semgrep.idea.telemetry.SentryProcessListener
import com.semgrep.idea.telemetry.SentryWrapper
import org.eclipse.lsp4j.services.LanguageServer

class SemgrepLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Semgrep") {
    override val lsp4jServerClass: Class<out LanguageServer> = SemgrepLanguageServer::class.java

    fun getLspJSCommandLine(settingState: SemgrepLspSettings): GeneralCommandLine {
        val interpreter = NodeJsInterpreterManager.getInstance(project).interpreter!!
        val lsp = JSLanguageServiceUtil.getPluginDirectory(javaClass, "lspjs/dist/semgrep-lsp.js")!!

        return GeneralCommandLine().apply {
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            withCharset(Charsets.UTF_8)
            addParameter(lsp.path)
            addParameter("--stdio")
            addParameter("--stack-size=${settingState.stackSizeJS}")
            addParameter("--max-old-space-size=${settingState.heapSizeJS}")
            NodeCommandLineConfigurator.find(interpreter)
                .configure(this, NodeCommandLineConfigurator.defaultOptions(project))
        }
    }

    fun getCLICommandLine(settingState: SemgrepLspSettings): GeneralCommandLine {
        return GeneralCommandLine(settingState.path).apply {
            addParameter("lsp")
            if (settingState.trace.server == TraceLevel.VERBOSE){
                addParameter("--debug")
            }
        }
    }

    override fun createCommandLine(): GeneralCommandLine {
        val settingState = AppState.getInstance().lspSettings
        val commandLine = if (settingState.useJS) {
            getLspJSCommandLine(settingState)
        } else {
            getCLICommandLine(settingState)
        }
        return commandLine.apply {
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            withCharset(Charsets.UTF_8)

        }
    }

    override fun isSupportedFile(file: VirtualFile): Boolean {
        return true
    }

    override fun createInitializationOptions(): Any {
        return AppState.getInstance().lspSettings
    }

    override fun startServerProcess(): OSProcessHandler {
        // wrap process starting with Sentry since it can fail
        val handler = SentryWrapper.getInstance().withSentry {
            val startingCommandLine = createCommandLine()
            val handler = OSProcessHandler(startingCommandLine)
            // add process listener to capture errors when process terminates
            handler.addProcessListener(SentryProcessListener())
            LOG.info("$this: starting LSP server: $startingCommandLine")
            return@withSentry handler
        }
        return handler

    }
    override val lspServerListener: LspServerListener = SemgrepLspServerListener(project)
}