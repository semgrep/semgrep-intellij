package com.semgrep.idea.settings

import com.intellij.openapi.options.Configurable
import com.semgrep.idea.telemetry.SentryWrapper
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {

    private var settingsComponent: AppSettingsComponent? = null

    override fun createComponent(): JComponent? {
        val settings = AppState.getInstance().lspSettings
        settingsComponent = AppSettingsComponent(settings)
        return settingsComponent?.getPanel()
    }

    override fun isModified(): Boolean {
        return settingsComponent?.getPanel()?.isModified() ?: false
    }

    override fun apply() {
        settingsComponent?.getPanel()?.apply()
        SentryWrapper.getInstance().setSentryContext()
    }

    override fun reset() {
        settingsComponent?.getPanel()?.reset()
        SentryWrapper.getInstance().setSentryContext()
    }

    override fun getDisplayName(): String {
        return "Semgrep"
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}