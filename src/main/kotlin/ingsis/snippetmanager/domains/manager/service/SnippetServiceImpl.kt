package ingsis.snippetmanager.domains.manager.service

import com.fasterxml.jackson.annotation.JsonValue
import ingsis.roles.error.HTTPError
import ingsis.snippetmanager.domains.manager.dto.SnippetCreateRequestDTO
import ingsis.snippetmanager.domains.manager.dto.SnippetResponseDTO
import ingsis.snippetmanager.domains.manager.repository.SnippetRepository
import ingsis.snippetmanager.model.Snippet
import ingsis.snippetmanager.roles.service.RolesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class SnippetServiceImpl : SnippetService {

    @Autowired
    private var snippetRepository: SnippetRepository

    @Autowired
    private var rolesService: RolesService

    @Autowired
    constructor(snippetRepository: SnippetRepository, rolesService: RolesService) {
        this.snippetRepository = snippetRepository
        this.rolesService = rolesService
    }


    override fun createSnippet(dto: SnippetCreateRequestDTO, userId: String, token: String): UUID {
        val snippet = Snippet(dto)
        val savedSnippet = snippetRepository.save(snippet)
        val response = rolesService.createResource(userId, "snippet", savedSnippet.id!!, token)
        if (response.statusCode == HttpStatus.CREATED) {
            return savedSnippet.id!!
        } else {
            snippetRepository.delete(savedSnippet)
            throw Exception("Error creating resource")
        }
    }

    override fun getSnippet(id: UUID, token: String): SnippetResponseDTO {
        val roles = rolesService.getRoles(id, token)
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
        val roles = rolesService.getRoles(id, token)
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


    fun getSharedWithMeSnippetsIds(token: String): List<UUID> {
        //val env = Dotenv.load()
        //val url = env["SHARE_URI"] + "/shared_with_me/id"
        val url = System.getenv("SHARE_URI") + "/"
        val template = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", token)
        val requestEntity = HttpEntity<Void>(headers)
        val response = template.exchange(url, HttpMethod.GET, requestEntity, Array<UUID>::class.java)
        return response.body!!.toList()
    }
}

enum class SnippetRole(@get:JsonValue val value: String? = null) {
    OWNER("owner"),
    WRITE("write"),
    RUN("run"),
    READ("read")
}