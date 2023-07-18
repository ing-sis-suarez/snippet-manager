package ingsis.snippetmanager.model

import java.util.*
import javax.persistence.*

@Entity
class FormaterRules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "spaces_between_tokens", nullable = false)
    var spacesBetweenTokens: Int? = 1

    @Column(name = "indentation", nullable = false)
    var indentation: Int? = 4

    @Column(name = "max_line_length", nullable = false)
    var maxLineLength: Int? = 80
}