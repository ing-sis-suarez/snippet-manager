package ingsis.snippetmanager.domains.format_rules.repository

import ingsis.snippetmanager.domains.model.FormaterRules
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FormaterRulesRepository : JpaRepository<FormaterRules, UUID> {
}