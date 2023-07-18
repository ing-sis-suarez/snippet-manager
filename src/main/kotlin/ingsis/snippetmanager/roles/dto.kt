import java.util.*

data class CreateResourceDTO(val resourceId: UUID, val userId: String, val resourceType: String)

data class UserRolesResponseDTO(val roles: List<String>)

data class DeleteResourceRequestDTO(val resourceId: UUID, val resourceType: String)
