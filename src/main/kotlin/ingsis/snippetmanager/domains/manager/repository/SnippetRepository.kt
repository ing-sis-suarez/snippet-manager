package ingsis.snippetmanager.domains.manager.repository

import ingsis.snippetmanager.model.Snippet
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SnippetRepository : JpaRepository<Snippet, UUID> {

    override fun findById(id: UUID): Optional<Snippet>
}