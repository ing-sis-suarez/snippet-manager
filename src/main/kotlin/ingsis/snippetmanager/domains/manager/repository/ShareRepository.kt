package ingsis.snippetmanager.domains.manager.repository

import ingsis.snippetmanager.domains.manager.model.Snippet
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ShareRepository : JpaRepository<Snippet, UUID> {

    override fun findById(id: UUID): Optional<Snippet>
}