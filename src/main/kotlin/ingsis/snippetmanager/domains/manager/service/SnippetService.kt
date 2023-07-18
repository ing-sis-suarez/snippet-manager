package ingsis.snippetmanager.domains.manager.service

import ingsis.snippetmanager.domains.manager.dto.SnippetCreateRequestDTO
import ingsis.snippetmanager.domains.manager.dto.SnippetResponseDTO
import java.util.*

interface SnippetService {

    fun createSnippet(dto: SnippetCreateRequestDTO, userId: String, token: String): UUID

    fun getSnippet(id: UUID, token: String): SnippetResponseDTO

    fun deleteSnippet(id: UUID, token: String): Boolean

}