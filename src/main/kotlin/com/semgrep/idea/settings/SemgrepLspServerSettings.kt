package com.semgrep.idea.settings

enum class TraceLevel {
    OFF,
    MESSAGES,
    VERBOSE,
}

data class SemgrepLspServerSettings(
    var trace: Trace = Trace(),
    var path: String = "semgrep",
    var ignoreCliVersion: Boolean = false,
    var scan: Scan = Scan(),
    var doHover: Boolean = false,
    var metrics: Metrics = Metrics(),
) {

    data class Trace(var server: TraceLevel = TraceLevel.OFF)
    data class Scan(
        var configuration: Array<String> = arrayOf(),
        var exclude: Array<String> = arrayOf(),
        var include: Array<String> = arrayOf(),
        var jobs: Int = 1,
        var maxMemory: Int = 0,
        var maxTargetBytes: Int = 1000000,
        var timeout: Int = 30,
        var timeoutThreshold: Int = 3,
        var onlyGitDirty: Boolean = true,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Scan

            if (!configuration.contentEquals(other.configuration)) return false
            if (!exclude.contentEquals(other.exclude)) return false
            if (!include.contentEquals(other.include)) return false
            if (jobs != other.jobs) return false
            if (maxMemory != other.maxMemory) return false
            if (maxTargetBytes != other.maxTargetBytes) return false
            if (timeout != other.timeout) return false
            if (timeoutThreshold != other.timeoutThreshold) return false
            if (onlyGitDirty != other.onlyGitDirty) return false

            return true
        }

        override fun hashCode(): Int {
            var result = configuration.contentHashCode()
            result = 31 * result + exclude.contentHashCode()
            result = 31 * result + include.contentHashCode()
            result = 31 * result + jobs
            result = 31 * result + maxMemory
            result = 31 * result + maxTargetBytes
            result = 31 * result + timeout
            result = 31 * result + timeoutThreshold
            result = 31 * result + onlyGitDirty.hashCode()
            return result
        }
    }

    data class Metrics(
        var enabled: Boolean = true,
    )
}