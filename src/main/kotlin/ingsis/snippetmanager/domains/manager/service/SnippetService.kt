package ingsis.snippetmanager.domains.manager.service

import ingsis.snippetmanager.domains.manager.dto.SnippetDTO
import ingsis.snippetmanager.domains.manager.model.Snippet
import java.util.*

interface SnippetService   {

    fun createSnippet(snippet: SnippetDTO, userId: String): Snippet

    fun getSnippet(id: UUID): Snippet

}