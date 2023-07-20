package ingsis.snippetmanager.domains.snippets.repository

import ingsis.snippetmanager.domains.model.Snippet
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SnippetRepository : JpaRepository<Snippet, UUID> {


    override fun findById(id: UUID): Optional<Snippet>
}