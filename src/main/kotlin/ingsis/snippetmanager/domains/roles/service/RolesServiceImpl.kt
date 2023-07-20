package ingsis.snippetmanager.domains.roles.service

import CreateResourceDTO
import DeleteResourceRequestDTO
import UserRolesResponseDTO
import ingsis.roles.error.HTTPError
import ingsis.snippetmanager.domains.lint_rules.dto.IdList
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class RolesServiceImpl : RolesService {

    override fun createResource(
        userId: String,
        resourceType: String,
        resourceId: UUID,
        token: String
    ): ResponseEntity<UUID> {
        val url = System.getenv("ROLES_URI") + "/resource"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val body = CreateResourceDTO(resourceId, userId, resourceType)
        val requestEntity = HttpEntity<CreateResourceDTO>(body, headers)
        try {
            return template.exchange(url, HttpMethod.POST, requestEntity, UUID::class.java)
        } catch (e: HttpClientErrorException) {
            println(e.message)
            throw HTTPError(e.message ?: "", e.statusCode)
        }
    }

    override fun getResourcesByRole(token: String, resourceType: String, role: String): ResponseEntity<IdList> {
        val url = System.getenv("ROLES_URI") + "/resources/$resourceType?role=$role"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val requestEntity = HttpEntity<Void>(headers)
        try {
            return template.exchange(url, HttpMethod.GET, requestEntity, IdList::class.java)
        } catch (e: HttpClientErrorException) {
            println(e.message)
            throw HTTPError(e.message ?: "", e.statusCode)
        }
    }

    override fun getResourceIfExistsByOwnerAndResourceType(
        resourceType: String,
        token: String
    ): ResponseEntity<UUID> {
        val url = System.getenv("ROLES_URI") + "/resource?resourceType=$resourceType"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val requestEntity = HttpEntity<Void>(headers)
        try {
            return template.exchange(url, HttpMethod.GET, requestEntity, UUID::class.java)
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.NOT_FOUND) return ResponseEntity(HttpStatus.NOT_FOUND)
            println(e.message)
            throw HTTPError(e.message ?: "", e.statusCode)
        }
    }

    override fun getRoles(resourceId: UUID, token: String, resourceType: String): ResponseEntity<UserRolesResponseDTO> {
        val url = System.getenv("ROLES_URI") + "/role?resourceId=$resourceId&resourceType=$resourceType"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val requestEntity = HttpEntity<Void>(headers)
        try {
            return template.exchange(url, HttpMethod.GET, requestEntity, UserRolesResponseDTO::class.java)
        } catch (e: HttpClientErrorException) {
            println(e.message)
            throw HTTPError(e.message ?: "", e.statusCode)
        }
    }

    override fun deleteResource(resourceId: UUID, token: String): ResponseEntity<Boolean> {
        val url = System.getenv("ROLES_URI") + "/resource?resourceId=$resourceId&resourceType=snippet"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val body = DeleteResourceRequestDTO(resourceId, "snippet")
        val requestEntity = HttpEntity<DeleteResourceRequestDTO>(body, headers)
        try {
            return template.exchange(url, HttpMethod.DELETE, requestEntity, Boolean::class.java)
        } catch (e: HttpClientErrorException) {
            println(e.message)
            throw HTTPError(e.message ?: "", e.statusCode)
        }
    }

    private fun prepareHeaders(headers: HttpHeaders, token: String) {
        headers.set("Content-Type", "application/json")
        headers.set("Authorization", token)
    }
}