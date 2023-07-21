package ingsis.snippetmanager.domains.format_rules.controller

import ingsis.snippetmanager.domains.format_rules.dto.FormaterRulesDTO
import ingsis.snippetmanager.domains.format_rules.producer.FormatRequestEvent
import ingsis.snippetmanager.domains.format_rules.producer.FormatRequestProducer
import ingsis.snippetmanager.domains.format_rules.service.FormaterRulesService
import ingsis.snippetmanager.domains.model.FormaterRules
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
class FormaterRulesController {

    @Autowired
    private var formatRequestProducer: FormatRequestProducer

    @Autowired
    private var formaterRulesService: FormaterRulesService

    @Autowired
    private var snippetService: SnippetService

    @Autowired
    constructor(
        formaterRulesService: FormaterRulesService,
        formatRequestProducer: FormatRequestProducer,
        snippetService: SnippetService
    ) {
        this.formaterRulesService = formaterRulesService
        this.formatRequestProducer = formatRequestProducer
        this.snippetService = snippetService
    }

    @PostMapping("/formater_rules")
    fun createFormaterRules(
        @RequestHeader("Authorization") token: String,
        principal: Principal
    ): ResponseEntity<FormaterRules> {
        return ResponseEntity(formaterRulesService.createFormaterRules(principal.name, token), HttpStatus.CREATED)
    }

    @GetMapping("/formater_rules/{id}")
    fun getFormaterRules(
        @RequestHeader("Authorization") token: String,
        @PathVariable("id") id: UUID,
        principal: Principal
    ): ResponseEntity<FormaterRules> {
        return ResponseEntity(formaterRulesService.getFormaterRules(principal.name, token, id), HttpStatus.OK)
    }

    @PutMapping("/formater_rules/{id}")
    suspend fun updateFormaterRules(
        @RequestHeader("Authorization") token: String,
        @PathVariable("id") id: UUID,
        @RequestBody formaterRules: FormaterRulesDTO,
        principal: Principal
    ): ResponseEntity<FormaterRules> {
        val newRules = ResponseEntity(
            formaterRulesService.updateFormaterRules(principal.name, token, id, formaterRules),
            HttpStatus.OK
        )
        val snippets = snippetService.getSnippetsByOwner(principal.name, token)
        snippetService.setAllToPending(snippets.map { it.id })
        snippets.forEach {
            formatRequestProducer.publishEvent(FormatRequestEvent(it.id, id, token))
        }
        return newRules
    }


    @GetMapping("/formater_rules")
    fun getLinterRules(
        @RequestHeader("Authorization") token: String,
        principal: Principal
    ): ResponseEntity<FormaterRules> {
        return ResponseEntity(formaterRulesService.getFormaterRules(principal.name, token), HttpStatus.OK)
    }

    @PostMapping("/formater_rules/redis")
    suspend fun testRedis(@RequestHeader("Authorization") token: String, principal: Principal): ResponseEntity<Void> {
        formatRequestProducer.publishEvent(FormatRequestEvent(UUID.randomUUID(), UUID.randomUUID(), token))
        return ResponseEntity(HttpStatus.OK)
    }


}
