package com.semgrep.idea.ui

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup
import com.semgrep.idea.actions.SemgrepActionGroup

class SemgrepStatusBarPopUp : StatusBarWidgetFactory {

    override fun getId(): String {
        return "semgrepStatusBarPopUp"
    }

    override fun getDisplayName(): String {
        return "Semgrep"
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return SemgrepStatusBarWidget(project)
    }

    override fun isEnabledByDefault(): Boolean {
        return true
    }

    class SemgrepStatusBarWidget(project: Project) : EditorBasedStatusBarPopup(project, false) {
        override fun ID(): String {
            return "semgrepStatusBarPopUp"
        }

        override fun createInstance(project: Project): StatusBarWidget {
            return SemgrepStatusBarWidget(project)
        }

        override fun createPopup(context: DataContext): ListPopup {
            val popup = JBPopupFactory.getInstance().createActionGroupPopup(
                "Semgrep",
                SemgrepActionGroup(),
                context,
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true,
            )
            return popup
        }

        override fun getWidgetState(file: VirtualFile?): WidgetState {
            return WidgetState("Semgrep", null, true).apply {
                icon = IconLoader.getIcon("/META-INF/pluginLogo.svg", SemgrepStatusBarWidget::class.java)
            }
        }

    }

}