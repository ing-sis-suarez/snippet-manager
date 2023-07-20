package ingsis.snippetmanager.domains.roles.service

import UserRolesResponseDTO
import ingsis.snippetmanager.domains.lint_rules.dto.IdList
import org.springframework.http.ResponseEntity
import java.util.*

interface RolesService {


    fun createResource(
        userId: String,
        resourceType: String,
        resourceId: UUID,
        token: String
    ): ResponseEntity<UUID>

    fun getResourcesByRole(token: String, resourceType: String, role: String): ResponseEntity<IdList>

    fun getResourceIfExistsByOwnerAndResourceType(
        resourceType: String,
        token: String
    ): ResponseEntity<UUID>

    fun deleteResource(resourceId: UUID, token: String): ResponseEntity<Boolean>

    fun getRoles(resourceId: UUID, token: String, resourceType: String): ResponseEntity<UserRolesResponseDTO>
}