package ingsis.snippetmanager.model

import java.util.*
import javax.persistence.*

@Entity
class LinterRules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "case_convention", nullable = false)
    open var caseConvention: CaseConvention? = CaseConvention.CAMEL_CASE

    @Column(name = "print_expressions_enabled", nullable = false)
    open var printExpressionsEnabled: Boolean? = false
}


enum class CaseConvention {
    CAMEL_CASE,
    SNAKE_CASE
}