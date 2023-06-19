package ingsis.snippetmanager.domains.manager.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "snippet")
class Snippet {
    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "content", nullable = false)
    var content: String? = null

    @Column(name = "createdAt", nullable = false)
    var createdAt: Date? = Date()


    constructor(id: UUID?, createdAt: Date?) {
        this.id = id
        this.createdAt = createdAt
    }

}