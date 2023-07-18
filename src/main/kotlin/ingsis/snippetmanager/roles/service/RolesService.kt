package ingsis.snippetmanager.roles.service

import UserRolesResponseDTO
import org.springframework.http.ResponseEntity
import java.util.*

interface RolesService {

    fun createResource(
        userId: String,
        resourceType: String,
        resourceId: UUID,
        token: String
    ): ResponseEntity<UUID>

    fun deleteResource(resourceId: UUID, token: String): ResponseEntity<Boolean>

    fun getRoles(resourceId: UUID, token: String): ResponseEntity<UserRolesResponseDTO>
}