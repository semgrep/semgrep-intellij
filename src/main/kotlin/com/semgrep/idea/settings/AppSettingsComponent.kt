package com.semgrep.idea.settings

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class AppSettingsComponent(settings: SemgrepLspSettings) {
    private var panel: JPanel? = null

    // Really we should use reflection to generate this, but I'm lazy
    private val traceChooser = ComboBox(arrayOf("off", "messages", "verbose"))
    private val pathTextField = JBTextField()

    init {
        panel = FormBuilder.createFormBuilder().apply {
            addLabeledComponent("Trace Level", traceChooser)
            addLabeledComponent("Semgrep Path", pathTextField)
            addComponentFillVertically(JPanel(), 0)
        }.panel
        setFieldValues(settings)
    }

    fun getPanel(): JPanel? {
        return panel
    }

    fun getSettings(): SemgrepLspSettings {
        val traceLevelStr = traceChooser.selectedItem as String
        val traceLevel = TraceLevel.valueOf(traceLevelStr.uppercase())
        return SemgrepLspSettings(trace = SemgrepLspSettings.Trace(traceLevel), path = pathTextField.text)
    }

    fun setFieldValues(settings: SemgrepLspSettings) {
        traceChooser.selectedItem = settings.trace.server.name.lowercase()
        pathTextField.text = settings.path
    }

}