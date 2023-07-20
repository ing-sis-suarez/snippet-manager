package ingsis.snippetmanager.domains.model

import ingsis.snippetmanager.domains.test.dto.CreateTestDTO
import ingsis.snippetmanager.domains.test.dto.TestResponseDTO
import java.util.*
import javax.persistence.*

@Entity
class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @ManyToOne
    @JoinColumn(name = "snippet_id", nullable = false)
    var snippet: Snippet? = null

    @Column(name = "title", nullable = false)
    var title: String? = null

    @Column(name = "language", nullable = false)
    var language: String? = null

    @Column(name = "inputs")
    var inputs: String? = null

    @Column(name = "output")
    var output: String? = null

    @Column(name = "createdAt", nullable = false)
    var createdAt: Date? = Date()


    constructor(){}

    constructor(createTestDTO: CreateTestDTO) {
        this.title = createTestDTO.title
        this.language = createTestDTO.language
        this.inputs = createTestDTO.inputs.joinToString(separator = "\n")
        this.output = createTestDTO.output
    }

    fun toDTO(): TestResponseDTO {
        return TestResponseDTO(
            id = this.id!!,
            snippetId = this.snippet!!.id!!,
            title = this.title!!,
            language = this.language!!,
            inputs = this.inputs!!.split("\n"),
            output = this.output,
            createdAt = this.createdAt!!
        )
    }
}