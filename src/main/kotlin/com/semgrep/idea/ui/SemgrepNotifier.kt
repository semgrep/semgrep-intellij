package com.semgrep.idea.ui

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.util.text.SemVer
import com.semgrep.idea.actions.DismissLoginNudgeAction
import com.semgrep.idea.actions.LoginAction

class SemgrepNotifier(private val project: Project) {
    fun notifyLoginNudge() {
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

    fun notifyInstallSuccess() {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Install Group")
            .createNotification(
                "Semgrep has been installed successfully",
                NotificationType.INFORMATION
            ).apply {
                notify(project)
            }
    }

    fun notifyInstallFailure(output: String, exitCode: Int) {
        val message = "Semgrep failed to install with exit code $exitCode and output:\n$output"
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Install Group")
            .createNotification(
                message,
                NotificationType.ERROR
            ).apply {
                notify(project)
            }
    }

    fun notifyWindowsNotSupported(){
        val message = "Semgrep is not supported on Windows currently, please use WSL or a VM"

        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Install Group")
            .createNotification(
                message,
                NotificationType.ERROR
            ).apply {
                notify(project)
            }
    }

    fun notifyUpdateNeeded(needed: SemVer, current: SemVer) {
        val message =
            "The Semgrep Extension requires a Semgrep CLI version $needed, the current installed version is $current, please update"
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Install Group")
            .createNotification(
                message,
                NotificationType.WARNING
            ).apply {
                notify(project)
            }
    }

    fun notifyUpdateAvailable(current: SemVer, latest: SemVer) {
        val message =
            "Some features of the Semgrep Extension require a Semgrep CLI version ${latest}, but the current installed version is ${current}, some features may be disabled, please upgrade."
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Install Group")
            .createNotification(
                message,
                NotificationType.INFORMATION
            ).apply {
                notify(project)
            }
    }
}