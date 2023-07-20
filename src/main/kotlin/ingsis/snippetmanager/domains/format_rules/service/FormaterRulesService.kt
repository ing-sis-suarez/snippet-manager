package ingsis.snippetmanager.domains.format_rules.service

import ingsis.snippetmanager.domains.format_rules.dto.FormaterRulesDTO
import ingsis.snippetmanager.domains.model.FormaterRules
import java.util.*

interface FormaterRulesService {

    fun createFormaterRules(ownerId: String, token: String): FormaterRules
    fun getFormaterRules(ownerId: String, token: String, resourceId: UUID): FormaterRules
    fun updateFormaterRules(ownerId: String, token: String, resourceId: UUID, dto: FormaterRulesDTO): FormaterRules
}