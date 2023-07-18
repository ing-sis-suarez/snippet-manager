package ingsis.snippetmanager.roles.service

import CreateResourceDTO
import DeleteResourceRequestDTO
import UserRolesResponseDTO
import ingsis.roles.error.HTTPError
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class RolesServiceImpl: RolesService {

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
            throw HTTPError(e.message ?: "", e.statusCode)
        }
    }

    override fun getRoles(resourceId: UUID, token: String): ResponseEntity<UserRolesResponseDTO> {
        val url = System.getenv("ROLES_URI") + "/role?resourceId=$resourceId&resourceType=snippet"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val requestEntity = HttpEntity<Void>(headers)
        try {
            return template.exchange(url, HttpMethod.GET, requestEntity, UserRolesResponseDTO::class.java)
        } catch (e: HttpClientErrorException) {
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
            throw HTTPError(e.message ?: "", e.statusCode)
        }
    }

    private fun prepareHeaders(headers: HttpHeaders, token: String) {
        headers.set("Content-Type", "application/json")
        headers.set("Authorization", token)
    }

}