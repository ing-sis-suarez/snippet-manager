package ingsis.snippetmanager.domains.lint_rules.repository

import ingsis.snippetmanager.domains.model.LinterRules
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LinterRulesRepository : JpaRepository<LinterRules, UUID> {
}