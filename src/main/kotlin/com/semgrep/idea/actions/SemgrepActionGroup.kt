package com.semgrep.idea.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class SemgrepActionGroup : ActionGroup() {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return arrayOf(
            LoginAction(),
            LogoutAction(),
            ScanWorkspaceAction(),
            ScanWorkspaceFullAction(),
            RefreshRulesAction()
        )
    }

}