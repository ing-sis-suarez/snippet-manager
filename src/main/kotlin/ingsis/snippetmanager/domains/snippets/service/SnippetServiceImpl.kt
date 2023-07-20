package ingsis.snippetmanager.domains.snippets.service

import com.fasterxml.jackson.annotation.JsonValue
import ingsis.roles.error.HTTPError
import ingsis.snippetmanager.domains.format_rules.service.FormaterRulesService
import ingsis.snippetmanager.domains.lint_rules.service.LinterRulesService
import ingsis.snippetmanager.domains.model.ComplianceState
import ingsis.snippetmanager.domains.model.Snippet
import ingsis.snippetmanager.domains.roles.service.RolesService
import ingsis.snippetmanager.domains.snippets.dto.SnippetCreationResponseDTO
import ingsis.snippetmanager.domains.snippets.dto.SnippetDataRequestDTO
import ingsis.snippetmanager.domains.snippets.dto.SnippetResponseDTO
import ingsis.snippetmanager.domains.snippets.dto.UpdateSnippetDTO
import ingsis.snippetmanager.domains.snippets.repository.SnippetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class SnippetServiceImpl : SnippetService {

    @Autowired
    private var snippetRepository: SnippetRepository

    @Autowired
    private var rolesService: RolesService

    @Autowired
    private var linterRulesService: LinterRulesService

    @Autowired
    private var formaterRulesService: FormaterRulesService

    @Autowired
    constructor(
        snippetRepository: SnippetRepository,
        rolesService: RolesService,
        LinterRulesService: LinterRulesService,
        formaterRulesService: FormaterRulesService
    ) {
        this.snippetRepository = snippetRepository
        this.rolesService = rolesService
        this.linterRulesService = LinterRulesService
        this.formaterRulesService = formaterRulesService
    }

    override fun getReadableSnippets(token: String): List<SnippetResponseDTO> {
        val readable = rolesService.getResourcesByRole(token, "snippet", "read").body!!.ids
        val owned = rolesService.getResourcesByRole(token, "snippet", "owner").body!!.ids
        val onlyReadable = readable.filter { id -> !owned.contains(id) }
        val snippets = snippetRepository.findAllById(onlyReadable)
        return snippets.map { snippet -> SnippetResponseDTO(
            snippet.id!!,
            snippet.title!!,
            snippet.content!!,
            snippet.createdAt!!,
            snippet.language!!,
            snippet.compliance!!
        ) }
    }

    override fun getOwnedSnippets(token: String): List<SnippetResponseDTO> {
        val response = rolesService.getResourcesByRole(token, "snippet", "owner")
        val snippets = snippetRepository.findAllById(response.body!!.ids)
        return snippets.map { snippet -> SnippetResponseDTO(
            snippet.id!!,
            snippet.title!!,
            snippet.content!!,
            snippet.createdAt!!,
            snippet.language!!,
            snippet.compliance!!
        ) }
    }

    override fun setAllToPending(ids: List<UUID>) {
        val snippets = snippetRepository.findAllById(ids)
        val pendingSnippets = snippets.map { snippet ->
            snippet.compliance = ComplianceState.PENDING
            snippet
        }
        snippetRepository.saveAll(pendingSnippets)
    }


    override fun createSnippet(dto: SnippetDataRequestDTO, userId: String, token: String): SnippetCreationResponseDTO {
        val linterRules = rolesService.getResourceIfExistsByOwnerAndResourceType("linter_rules", token)
        if (linterRules.statusCode != HttpStatus.OK) linterRulesService.createLinterRules(userId, token)
        val formaterRules = rolesService.getResourceIfExistsByOwnerAndResourceType("formater_rules", token)
        if (formaterRules.statusCode != HttpStatus.OK) formaterRulesService.createFormaterRules(userId, token)
        val snippet = Snippet(dto)
        val savedSnippet = snippetRepository.save(snippet)
        val response = rolesService.createResource(userId, "snippet", savedSnippet.id!!, token)
        if (response.statusCode == HttpStatus.CREATED) {
            return SnippetCreationResponseDTO(savedSnippet.toSnippetResponseDTO(), linterRules.body!!, formaterRules.body!!)
        } else {
            snippetRepository.delete(savedSnippet)
            throw HTTPError("Error creating resource", response.statusCode)
        }
    }

    override fun getSnippet(id: UUID, token: String): SnippetResponseDTO {
        val roles = rolesService.getRoles(id, token, "snippet")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        if (!roles.body!!.roles.contains("read")) throw HTTPError("Unauthorized", HttpStatus.UNAUTHORIZED)
        val snippet =
            snippetRepository.findById(id).orElseThrow { HTTPError("Snippet not found", HttpStatus.NOT_FOUND) }
        return SnippetResponseDTO(
            snippet.id!!,
            snippet.title!!,
            snippet.content!!,
            snippet.createdAt!!,
            snippet.language!!,
            snippet.compliance!!
        )
    }

    override fun deleteSnippet(id: UUID, token: String): Boolean {
        val roles = rolesService.getRoles(id, token, "snippet")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        if (!roles.body!!.roles.contains("owner")) throw HTTPError("Unauthorized", HttpStatus.UNAUTHORIZED)
        val snippet =
            snippetRepository.findById(id).orElseThrow { HTTPError("Snippet not found", HttpStatus.NOT_FOUND) }
        val deleteResponse = rolesService.deleteResource(id, token)
        if (deleteResponse.statusCode != HttpStatus.OK) throw HTTPError(
            "Error deleting resource",
            deleteResponse.statusCode
        )
        snippetRepository.delete(snippet)
        return true
    }

    override fun getSnippetsByOwner(ownerId: String, token: String): List<SnippetResponseDTO> {
        val roles = rolesService.getResourcesByRole(token, "snippet", "owner")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        val snippets = snippetRepository.findAllById(roles.body!!.ids)
        return snippets.map {
            SnippetResponseDTO(
                it.id!!,
                it.title!!,
                it.content!!,
                it.createdAt!!,
                it.language!!,
                it.compliance!!
            )
        }
    }

    override fun updateSnippet(
        id: UUID,
        dto: UpdateSnippetDTO,
        ownerId: String?,
        token: String
    ): SnippetResponseDTO {
        val roles = rolesService.getRoles(id, token, "snippet")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        if (!roles.body!!.roles.contains("write")) throw HTTPError("Unauthorized", HttpStatus.UNAUTHORIZED)
        val oldSnippet =
            snippetRepository.findById(id).orElseThrow { HTTPError("Snippet not found", HttpStatus.NOT_FOUND) }
        oldSnippet.update(dto)
        val updatedSnippet = snippetRepository.save(oldSnippet)
        return SnippetResponseDTO(
            updatedSnippet.id!!,
            updatedSnippet.title!!,
            updatedSnippet.content!!,
            updatedSnippet.createdAt!!,
            updatedSnippet.language!!,
            updatedSnippet.compliance!!
        )
    }
}

enum class SnippetRole(@get:JsonValue val value: String? = null) {
    OWNER("owner"),
    WRITE("write"),
    RUN("run"),
    READ("read")
}