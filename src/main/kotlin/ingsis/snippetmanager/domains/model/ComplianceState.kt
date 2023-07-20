package ingsis.snippetmanager.domains.model

enum class ComplianceState(@get:JvmName("getValue") val value: String) {
    COMPLIANT("COMPLIANT"),
    NON_COMPLIANT("NON_COMPLIANT"),
    PENDING("PENDING")
}