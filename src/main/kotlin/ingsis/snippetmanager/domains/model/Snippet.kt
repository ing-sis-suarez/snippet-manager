package ingsis.snippetmanager.domains.model

import ingsis.snippetmanager.domains.snippets.dto.SnippetDataRequestDTO
import ingsis.snippetmanager.domains.snippets.dto.SnippetResponseDTO
import ingsis.snippetmanager.domains.snippets.dto.UpdateSnippetDTO
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


    constructor(createDTO: SnippetDataRequestDTO) {
        this.title = createDTO.name
        this.content = createDTO.content
        this.language = createDTO.type
    }

    fun update(updateDTO: UpdateSnippetDTO) {
        this.title = updateDTO.name ?: this.title
        this.content = updateDTO.content ?: this.content
        this.language = updateDTO.type ?: this.language
        this.compliance = updateDTO.compliance ?: this.compliance
    }

    fun toSnippetResponseDTO(): SnippetResponseDTO {
        return SnippetResponseDTO(
            this.id!!,
            this.title!!,
            this.content!!,
            this.createdAt!!,
            this.language!!,
            this.compliance!!
        )
    }
}