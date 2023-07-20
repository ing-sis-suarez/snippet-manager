package ingsis.snippetmanager.domains.snippets.dto

import ingsis.snippetmanager.domains.model.ComplianceState
import java.util.*


data class SnippetDataRequestDTO(val name: String, val content: String, val type: String)


data class SnippetResponseDTO(
    val id: UUID,
    val title: String,
    val content: String,
    val createdAt: Date,
    val type: String,
    val compliance: ComplianceState
)

data class UpdateSnippetDTO(
    val name: String?,
    val content: String?,
    val type: String?,
    val compliance: ComplianceState?
)

data class SnippetCreationResponseDTO(val snippetId: UUID, val linterRules: UUID, val formaterRules: UUID)