package ingsis.snippetmanager.model

enum class ComplianceState(@get:JvmName("getValue") val value: String) {
    COMPLIANT("Compliant"),
    NON_COMPLIANT("Non Compliant"),
    PENDING("Pending")
}