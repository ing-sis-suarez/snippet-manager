package ingsis.snippetmanager.domains.format_rules.service

import com.fasterxml.jackson.annotation.JsonValue
import ingsis.roles.error.HTTPError
import ingsis.snippetmanager.domains.format_rules.dto.FormaterRulesDTO
import ingsis.snippetmanager.domains.format_rules.repository.FormaterRulesRepository
import ingsis.snippetmanager.domains.model.FormaterRules
import ingsis.snippetmanager.domains.roles.service.RolesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*


@Service
class FormaterRulesServiceImpl : FormaterRulesService {

    @Autowired
    private var formaterRulesRepository: FormaterRulesRepository

    @Autowired
    private var rolesService: RolesService

    @Autowired
    constructor(formaterRulesRepository: FormaterRulesRepository, rolesService: RolesService) {
        this.formaterRulesRepository = formaterRulesRepository
        this.rolesService = rolesService
    }

    override fun createFormaterRules(ownerId: String, token: String): FormaterRules {
        val savedRules = formaterRulesRepository.save(FormaterRules())
        val response = rolesService.createResource(ownerId, "formater_rules", savedRules.id!!, token)
        if (response.statusCode != HttpStatus.CREATED) {
            formaterRulesRepository.delete(savedRules)
            throw HTTPError("Error creating resource", response.statusCode)
        }
        return savedRules
    }

    override fun getFormaterRules(ownerId: String, token: String, resourceId: UUID): FormaterRules {
        val roles = rolesService.getRoles(resourceId, token, "formater_rules")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        if (!roles.body!!.roles.contains("read")) throw HTTPError("Unauthorized", HttpStatus.UNAUTHORIZED)
        return formaterRulesRepository.findById(resourceId)
            .orElseThrow { HTTPError("Resource not found", HttpStatus.NOT_FOUND) }
    }

    override fun updateFormaterRules(
        ownerId: String,
        token: String,
        resourceId: UUID,
        dto: FormaterRulesDTO
    ): FormaterRules {
        val roles = rolesService.getRoles(resourceId, token, "formater_rules")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        if (!roles.body!!.roles.contains("write")) throw HTTPError("Unauthorized", HttpStatus.UNAUTHORIZED)
        val rules = formaterRulesRepository.findById(resourceId)
            .orElseThrow { HTTPError("Resource not found", HttpStatus.NOT_FOUND) }
        rules.update(dto)
        return formaterRulesRepository.save(rules)
    }
}

enum class FormaterRulesRole(@get:JsonValue val value: String) {
    OWNER("owner"),
    WRITE("write"),
    READ("read")
}