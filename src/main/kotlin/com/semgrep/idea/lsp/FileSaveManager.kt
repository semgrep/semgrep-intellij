package com.semgrep.idea.lsp

import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import org.eclipse.lsp4j.DidSaveTextDocumentParams
import org.eclipse.lsp4j.TextDocumentIdentifier
import java.net.URI

class FileSaveManager(private val semgrepLspServer: SemgrepLspServer) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        val saveEvents = events.filter { it.isFromSave }
        saveEvents.forEach {
            val uri = URI.create(it.path).toString()
            val textDocumentIdentifier = TextDocumentIdentifier(uri)
            val params = DidSaveTextDocumentParams(textDocumentIdentifier)
            semgrepLspServer.lsp4jServer.textDocumentService.didSave(params)
        }
        super.after(events)

    }
}