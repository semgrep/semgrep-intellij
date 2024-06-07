package com.semgrep.idea.telemetry

import com.semgrep.idea.settings.AppState
import io.sentry.Hint
import io.sentry.Sentry
import io.sentry.SentryOptions
import io.sentry.protocol.User

val SKIP_FIELDS = arrayOf(
    "metrics.*",
    "scan.include",
    "scan.exclude",
    "scan.configuration",
    "path"
)
const val DSN = "https://4d1a49b9d3a6cd498af148cfdd479820@o77510.ingest.us.sentry.io/4507370251288576"

class SentryWrapper {
    companion object {
        private var instance: SentryWrapper? = null

        fun getInstance() = instance ?: SentryWrapper().also { instance = it }
    }

    private var appState: AppState = AppState.getInstance()
    private fun enabled() = appState.lspSettings.metrics.enabled
    private var initialized = false

    fun init() {
        if (enabled() && !initialized) {
            Sentry.init { options ->
                options.dsn = DSN
                // Set traces_sample_rate to 1.0 to capture 100%
                // of transactions for performance monitoring.
                // We recommend adjusting this value in production.
                options.tracesSampleRate = 1.0
            }
            this.initialized = true
            // set user context
            val user = User.fromMap(
                mapOf(
                    "id" to appState.lspSettings.metrics.machineId,
                ), SentryOptions()
            )
            Sentry.setUser(user)
            this.setSentryContext()
        }
    }

    fun setSentryContext() {
        if (!initialized || !enabled()) {
            return
        }
        val baseMap = mapOf(
            "semgrepVersion" to appState.pluginState.semgrepVersion,
            "isNewAppInstall" to appState.lspSettings.metrics.isNewAppInstall,
            "extensionVersion" to appState.lspSettings.metrics.extensionVersion,
            "loggedIn" to appState.pluginState.loggedIn,
            "jsInterpreterVersion" to appState.state.nodeJsInterpreter?.cachedVersion.toString(),
            "jsInterpreterName" to appState.state.nodeJsInterpreter?.referenceName
        )
        val settingsMap = appState.lspSettings.toFlattenedMap().filter { (t, _) ->
            SKIP_FIELDS.none { t.matches(it.toRegex()) }
        }
        (baseMap + settingsMap)
            .forEach { (t, u) ->
                Sentry.setTag(t, u.toString())
            }
    }

    fun captureException(e: Exception) {
        if (!initialized || !enabled()) {
            return
        }
        Sentry.captureException(e)
    }

    fun captureException(e: Exception, hint: Hint?) {
        if (!initialized || !enabled()) {
            return
        }
        Sentry.captureException(e, hint)
    }

    fun <T> withSentry(f: () -> T): T {
        try {
            return f()
        } catch (e: Exception) {
            captureException(e)
            throw e
        }
    }

}