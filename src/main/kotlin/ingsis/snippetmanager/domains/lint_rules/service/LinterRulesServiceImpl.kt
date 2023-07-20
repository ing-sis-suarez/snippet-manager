package ingsis.snippetmanager.domains.lint_rules.service

import com.fasterxml.jackson.annotation.JsonValue
import ingsis.roles.error.HTTPError
import ingsis.snippetmanager.domains.lint_rules.dto.LinterRulesDTO
import ingsis.snippetmanager.domains.lint_rules.repository.LinterRulesRepository
import ingsis.snippetmanager.domains.model.LinterRules
import ingsis.snippetmanager.domains.roles.service.RolesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*


@Service
class LinterRulesServiceImpl : LinterRulesService {

    @Autowired
    private var linterRulesRepository: LinterRulesRepository

    @Autowired
    private var rolesService: RolesService

    @Autowired
    constructor(linterRulesRepository: LinterRulesRepository, rolesService: RolesService) {
        this.linterRulesRepository = linterRulesRepository
        this.rolesService = rolesService
    }

    override fun createLinterRules(ownerId: String, token: String): LinterRules {
        val savedRules = linterRulesRepository.save(LinterRules())
        val response = rolesService.createResource(ownerId, "linter_rules", savedRules.id!!, token)
        if (response.statusCode != HttpStatus.CREATED) {
            linterRulesRepository.delete(savedRules)
            throw HTTPError("Error creating resource", response.statusCode)
        }
        return savedRules
    }

    override fun getLinterRules(ownerId: String, token: String, resourceId: UUID): LinterRules {
        val roles = rolesService.getRoles(resourceId, token, "linter_rules")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        if (!roles.body!!.roles.contains("read")) throw HTTPError("Unauthorized", HttpStatus.UNAUTHORIZED)
        return linterRulesRepository.findById(resourceId)
            .orElseThrow { HTTPError("Resource not found", HttpStatus.NOT_FOUND) }
    }

    override fun updateLinterRules(ownerId: String, token: String, resourceId: UUID, dto: LinterRulesDTO): LinterRules {
        val roles = rolesService.getRoles(resourceId, token, "linter_rules")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Error getting roles", roles.statusCode)
        if (!roles.body!!.roles.contains("write")) throw HTTPError("Unauthorized", HttpStatus.UNAUTHORIZED)
        val rules = linterRulesRepository.findById(resourceId)
            .orElseThrow { HTTPError("Resource not found", HttpStatus.NOT_FOUND) }
        rules.update(dto)
        return linterRulesRepository.save(rules)
    }
}

enum class LinterRulesRole(@get:JsonValue val value: String) {
    OWNER("owner"),
    WRITE("write"),
    READ("read")
}