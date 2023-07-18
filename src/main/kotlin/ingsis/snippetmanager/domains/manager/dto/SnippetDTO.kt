package ingsis.snippetmanager.domains.manager.dto

import ingsis.snippetmanager.model.ComplianceState
import java.util.*


data class SnippetCreateRequestDTO(val name: String, val content: String, val type: String)

data class SnippetResponseDTO(val id: UUID, val title: String, val content: String, val createdAt: Date, val type: String, val compliance: ComplianceState)