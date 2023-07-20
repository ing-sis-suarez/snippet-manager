package ingsis.snippetmanager.domains.model

import java.util.*
import javax.persistence.*

@Entity
class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Int? = null

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
}