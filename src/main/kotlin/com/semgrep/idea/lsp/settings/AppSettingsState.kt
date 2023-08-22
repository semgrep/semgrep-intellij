package com.semgrep.idea.lsp.settings
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.semgrep.idea.lsp.settings.AppSettingsState",
    storages = [Storage("SemgrepSettings.xml")],
)
class AppSettingsState:PersistentStateComponent<SemgrepLspServerSettings>{
    var settings:SemgrepLspServerSettings = SemgrepLspServerSettings()

    companion object {
        fun getInstance():AppSettingsState{
            return ApplicationManager.getApplication().getService(AppSettingsState::class.java)
        }
    }
    override fun getState(): SemgrepLspServerSettings {
        return this.settings
    }

    override fun loadState(state: SemgrepLspServerSettings) {
        settings = state
    }

}