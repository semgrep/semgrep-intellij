package com.semgrep.idea.lsp.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {

    private var settingsComponent: AppSettingsComponent? = null

    override fun createComponent(): JComponent? {
        val settings = AppSettingsState.getInstance().settings
        settingsComponent = AppSettingsComponent(settings)
        return settingsComponent?.getPanel()
    }

    override fun isModified(): Boolean {
        return settingsComponent?.getSettings() != AppSettingsState.getInstance().settings
    }

    override fun apply() {
        val appSettingsState = AppSettingsState.getInstance()
        val newSettings = settingsComponent?.getSettings() ?: return
        appSettingsState.settings = newSettings
        settingsComponent?.setFieldValues(appSettingsState.settings)
    }

    override fun reset() {
        val settings = AppSettingsState.getInstance().settings
        settingsComponent?.setFieldValues(settings)
    }

    override fun getDisplayName(): String {
        return "Semgrep"
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}