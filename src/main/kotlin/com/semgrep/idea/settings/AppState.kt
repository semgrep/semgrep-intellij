package com.semgrep.idea.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

data class PluginSettings(var dismissedLoginNudge: Boolean = false)
data class AppStateData(
    var appSettings: SemgrepPluginSettings = SemgrepPluginSettings(),
    var pluginState: PluginSettings = PluginSettings()
)

@State(
    name = "com.semgrep.idea.lsp.settings.AppSettingsState",
    storages = [Storage("SemgrepSettings.xml")],
)
class AppState : PersistentStateComponent<AppStateData> {
    var appSettings: SemgrepPluginSettings = SemgrepPluginSettings()
    var pluginState: PluginSettings = PluginSettings()

    companion object {
        fun getInstance(): AppState {
            return ApplicationManager.getApplication().getService(AppState::class.java)
        }
    }

    override fun getState(): AppStateData {
        return AppStateData(this.appSettings, this.pluginState)
    }

    override fun loadState(state: AppStateData) {
        this.appSettings = state.appSettings
        this.pluginState = state.pluginState
    }


}