package com.semgrep.idea.telemetry

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.util.Key
import io.sentry.Attachment
import io.sentry.Hint
import java.time.format.DateTimeFormatter
import kotlin.io.path.appendText
import kotlin.io.path.readBytes
import kotlin.io.path.writeText

class SentryProcessListener : ProcessListener {
    private var sentry = SentryWrapper.getInstance()
    private val tmpLog = kotlin.io.path.createTempFile("lsp-output", ".log")

    private fun attachmentOfLog(): Attachment {
        val humanReadableTimestamp = DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.now())
        val processLog = tmpLog.readBytes()
        return Attachment(processLog, "lsp-output-$humanReadableTimestamp.log")
    }

    override fun processTerminated(event: ProcessEvent) {
        super.processTerminated(event)
        println("Process terminated $event")
        if (event.exitCode != 0) {
            val exception = Exception("Lsp process terminated with exit code ${event.exitCode}")
            val attachment = attachmentOfLog()
            // clear tmp log
            tmpLog.writeText("", Charsets.UTF_8)
            sentry.captureException(exception, Hint.withAttachment(attachment))
        }
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        super.onTextAvailable(event, outputType)
        if (outputType.toString() == "stderr") {
            tmpLog.appendText(event.text, Charsets.UTF_8)
        }
    }
}