package ingsis.snippetmanager.domains.model

import com.fasterxml.jackson.annotation.JsonValue
import ingsis.snippetmanager.domains.lint_rules.dto.LinterRulesDTO
import java.util.*
import javax.persistence.*

@Entity
class LinterRules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "case_convention", nullable = false)
    var caseConvention: CaseConvention? = CaseConvention.CAMEL_CASE

    @Column(name = "print_expressions_enabled", nullable = false)
    var printExpressionsEnabled: Boolean? = false


    constructor() {}
    constructor(caseConvention: CaseConvention, printExpressionsEnabled: Boolean) {
        this.caseConvention = caseConvention
        this.printExpressionsEnabled = printExpressionsEnabled
    }

    fun update(dto: LinterRulesDTO) {
        this.caseConvention = dto.caseConvention
        this.printExpressionsEnabled = dto.printExpressionsEnabled
    }
}


enum class CaseConvention(@get:JsonValue val value: String) {
    CAMEL_CASE("camel_case"),
    SNAKE_CASE("snake_case"),
}