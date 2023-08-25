package com.semgrep.idea.ui

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.semgrep.idea.actions.DismissLoginNudgeAction
import com.semgrep.idea.actions.LoginAction

class SemgrepNotifier {
    companion object {
        fun notifyLoginNudge(project: Project) {
            NotificationGroupManager
                .getInstance()
                .getNotificationGroup("Login Nudge Group")
                .createNotification(
                    "Sign in to use your team's shared Semgrep rule configuration",
                    NotificationType.INFORMATION
                ).apply {
                    addAction(LoginAction(this))
                    addAction(DismissLoginNudgeAction(this))
                    notify(project)
                }
        }
    }
}