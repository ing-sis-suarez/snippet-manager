package ingsis.snippetmanager.domains.snippets.service

import ingsis.snippetmanager.domains.snippets.dto.SnippetDataRequestDTO
import ingsis.snippetmanager.domains.snippets.dto.SnippetResponseDTO
import ingsis.snippetmanager.domains.snippets.dto.UpdateSnippetDTO
import java.util.*

interface SnippetService {


    fun setAllToPending(ids: List<UUID>)
    fun createSnippet(dto: SnippetDataRequestDTO, userId: String, token: String): UUID

    fun getSnippet(id: UUID, token: String): SnippetResponseDTO

    fun deleteSnippet(id: UUID, token: String): Boolean

    fun getSnippetsByOwner(ownerId: String, token: String): List<SnippetResponseDTO>
    fun updateSnippet(id: UUID, snippet: UpdateSnippetDTO, ownerId: String?, token: String): SnippetResponseDTO
}