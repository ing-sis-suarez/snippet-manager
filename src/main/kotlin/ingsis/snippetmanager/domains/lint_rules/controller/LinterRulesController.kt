package ingsis.snippetmanager.domains.lint_rules.controller

import ingsis.snippetmanager.domains.lint_rules.dto.LinterRulesDTO
import ingsis.snippetmanager.domains.lint_rules.producer.LintRequestEvent
import ingsis.snippetmanager.domains.lint_rules.producer.LintRequestProducer
import ingsis.snippetmanager.domains.lint_rules.service.LinterRulesService
import ingsis.snippetmanager.domains.model.LinterRules
import ingsis.snippetmanager.domains.snippets.service.SnippetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*


@RestController
@CrossOrigin("*")
class LinterRulesController {

    @Autowired
    private var lintRequestProducer: LintRequestProducer

    @Autowired
    private var linterRulesService: LinterRulesService

    @Autowired
    private var snippetService: SnippetService

    @Autowired
    constructor(
        linterRulesService: LinterRulesService,
        lintRequestProducer: LintRequestProducer,
        snippetService: SnippetService
    ) {
        this.linterRulesService = linterRulesService
        this.lintRequestProducer = lintRequestProducer
        this.snippetService = snippetService
    }

    @PostMapping("/linter_rules")
    fun createLinterRules(
        @RequestHeader("Authorization") token: String,
        principal: Principal
    ): ResponseEntity<LinterRules> {
        return ResponseEntity(linterRulesService.createLinterRules(principal.name, token), HttpStatus.CREATED)
    }

    @GetMapping("/linter_rules/{id}")
    fun getLinterRules(
        @RequestHeader("Authorization") token: String,
        @PathVariable("id") id: UUID,
        principal: Principal
    ): ResponseEntity<LinterRules> {
        return ResponseEntity(linterRulesService.getLinterRules(principal.name, token, id), HttpStatus.OK)
    }

    @PutMapping("/linter_rules/{id}")
    suspend fun updateLinterRules(
        @RequestHeader("Authorization") token: String,
        @PathVariable("id") id: UUID,
        @RequestBody linterRules: LinterRulesDTO,
        principal: Principal
    ): ResponseEntity<LinterRules> {
        val newRules =
            ResponseEntity(linterRulesService.updateLinterRules(principal.name, token, id, linterRules), HttpStatus.OK)
        val snippets = snippetService.getSnippetsByOwner(principal.name, token)
        snippetService.setAllToPending(snippets.map { it.id })
        snippets.forEach {
            lintRequestProducer.publishEvent(LintRequestEvent(it.id, id, token))
        }
        return newRules
    }

    @PostMapping("/linter_rules/redis")
    suspend fun testRedis(@RequestHeader("Authorization") token: String, principal: Principal): ResponseEntity<Void> {
        lintRequestProducer.publishEvent(LintRequestEvent(UUID.randomUUID(), UUID.randomUUID(), token))
        return ResponseEntity(HttpStatus.OK)
    }


}
