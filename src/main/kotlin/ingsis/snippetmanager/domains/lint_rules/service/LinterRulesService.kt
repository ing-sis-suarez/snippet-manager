package ingsis.snippetmanager.domains.lint_rules.service

import ingsis.snippetmanager.domains.lint_rules.dto.LinterRulesDTO
import ingsis.snippetmanager.domains.model.LinterRules
import java.util.*

interface LinterRulesService {

    fun createLinterRules(ownerId: String, token: String): LinterRules

    fun getLinterRules(ownerId: String, token: String, resourceId: UUID): LinterRules
    fun getLinterRules(ownerId: String, token: String): LinterRules
    fun updateLinterRules(ownerId: String, token: String, resourceId: UUID, dto: LinterRulesDTO): LinterRules
}