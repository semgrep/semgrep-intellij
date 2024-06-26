package com.semgrep.idea.lsp

import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.net.URLEncoder

class FileSaveManager(private val semgrepLspServer: SemgrepLspServer) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        val saveEvents = events.filter { it.isFromSave }
        saveEvents.forEach {
            val uri = URLEncoder.encode(it.path, "UTF-8")
            semgrepLspServer.notifyDidSave(uri)
        }
        super.after(events)

    }
}