package com.semgrep.idea.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {

    private var settingsComponent: AppSettingsComponent? = null

    override fun createComponent(): JComponent? {
        val settings = AppState.getInstance().lspSettings
        settingsComponent = AppSettingsComponent(settings)
        return settingsComponent?.getPanel()
    }

    override fun isModified(): Boolean {
        return settingsComponent?.getSettings() != AppState.getInstance().lspSettings
    }

    override fun apply() {
        val appSettingsState = AppState.getInstance()
        val newSettings = settingsComponent?.getSettings() ?: return
        appSettingsState.lspSettings = newSettings
        settingsComponent?.setFieldValues(appSettingsState.lspSettings)
    }

    override fun reset() {
        val settings = AppState.getInstance().lspSettings
        settingsComponent?.setFieldValues(settings)
    }

    override fun getDisplayName(): String {
        return "Semgrep"
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}