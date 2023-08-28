package com.semgrep.idea.ui

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import com.semgrep.idea.lsp.SemgrepInstaller
import com.semgrep.idea.settings.AppState

class SemgrepInstallBannerProvider : EditorNotifications.Provider<SemgrepInstallBannerProvider.SemgrepInstallBanner>(),
    DumbAware {

    class SemgrepInstallBanner(project: Project) : EditorNotificationPanel(Status.Error) {
        init {
            val installOptions = SemgrepInstaller.getInstallOptions()
            installOptions.forEach {
                createActionLabel("Install with ${it.name.lowercase()}") {
                    it.install(project)
                    AppState.getInstance().pluginState.handledInstallBanner = true
                    EditorNotifications.getInstance(project).updateAllNotifications()
                }
            }
            createActionLabel("Ignore Extension") {
                AppState.getInstance().pluginState.handledInstallBanner = true
                EditorNotifications.getInstance(project).updateAllNotifications()

            }
            text("Semgrep is not installed")
        }
    }

    override fun createNotificationPanel(
        file: VirtualFile,
        fileEditor: FileEditor,
        project: Project
    ): SemgrepInstallBanner? {
        if (SemgrepInstaller.semgrepInstalled() || AppState.getInstance().pluginState.handledInstallBanner) {
            return null
        }
        return SemgrepInstallBanner(project)
    }

    override fun getKey(): Key<SemgrepInstallBanner> {
        return KEY
    }

    companion object {
        val KEY = Key.create<SemgrepInstallBanner>("semgrep.install.banner")
    }
}