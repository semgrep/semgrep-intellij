package com.semgrep.idea.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {

    private var settingsComponent: AppSettingsComponent? = null

    override fun createComponent(): JComponent? {
        val settings = AppState.getInstance().appSettings
        settingsComponent = AppSettingsComponent(settings)
        return settingsComponent?.getPanel()
    }

    override fun isModified(): Boolean {
        return settingsComponent?.getSettings() != AppState.getInstance().appSettings
    }

    override fun apply() {
        val appSettingsState = AppState.getInstance()
        val newSettings = settingsComponent?.getSettings() ?: return
        appSettingsState.appSettings = newSettings
        settingsComponent?.setFieldValues(appSettingsState.appSettings)
    }

    override fun reset() {
        val settings = AppState.getInstance().appSettings
        settingsComponent?.setFieldValues(settings)
    }

    override fun getDisplayName(): String {
        return "Semgrep"
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}