package ingsis.snippetmanager.model

import ingsis.snippetmanager.domains.manager.dto.SnippetCreateRequestDTO
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "snippet")
class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "title", nullable = false)
    var title: String? = null

    @Column(name = "language", nullable = false)
    var language: String? = null

    @Column(name = "compliance", nullable = false)
    var compliance: ComplianceState? = ComplianceState.PENDING

    @Column(name = "content", nullable = false)
    var content: String? = null

    @Column(name = "createdAt", nullable = false)
    var createdAt: Date? = Date()


    constructor(createDTO: SnippetCreateRequestDTO) {
        this.title = createDTO.name
        this.content = createDTO.content
        this.language = createDTO.type
    }

}