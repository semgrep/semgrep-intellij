package com.semgrep.idea.actions

import com.intellij.notification.Notification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.semgrep.idea.settings.AppState

class DismissLoginNudgeAction(private val notification: Notification) : AnAction("Don't ask again") {
    override fun actionPerformed(e: AnActionEvent) {
        AppState.getInstance().pluginState.dismissedLoginNudge = true
        notification.expire()
    }
}