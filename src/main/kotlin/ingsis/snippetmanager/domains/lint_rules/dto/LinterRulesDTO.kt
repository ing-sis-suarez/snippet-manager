package ingsis.snippetmanager.domains.lint_rules.dto

import ingsis.snippetmanager.domains.model.CaseConvention

data class LinterRulesDTO(val caseConvention: CaseConvention, val printExpressionsEnabled: Boolean)