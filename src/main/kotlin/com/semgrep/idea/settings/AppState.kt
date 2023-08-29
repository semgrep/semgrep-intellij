package com.semgrep.idea.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.semgrep.idea.lsp.settings.AppSettingsState",
    storages = [Storage("SemgrepSettings.xml")],
)
class AppState : PersistentStateComponent<AppStateData> {
    var lspSettings: SemgrepLspSettings = SemgrepLspSettings()
    var pluginState: PluginState = PluginState()

    companion object {
        fun getInstance(): AppState {
            return ApplicationManager.getApplication().getService(AppState::class.java)
        }
    }

    override fun getState(): AppStateData {
        return AppStateData(this.lspSettings, this.pluginState)
    }

    override fun loadState(state: AppStateData) {
        this.lspSettings = state.appSettings
        this.pluginState = state.pluginState
    }


}