package ingsis.snippetmanager.domains.manager.controller

import ingsis.snippetmanager.domains.manager.dto.SnippetCreateRequestDTO
import ingsis.snippetmanager.domains.manager.dto.SnippetResponseDTO
import ingsis.snippetmanager.domains.manager.service.SnippetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@CrossOrigin("*")
class SnippetController {

    @Autowired
    private var snippetService: SnippetService

    @Autowired
    constructor(snippetService: SnippetService) {
        this.snippetService = snippetService
    }

    @PostMapping("/snippet")
    fun createSnippet(
        @RequestHeader("Authorization") token: String,
        principal: Principal,
        @RequestBody snippet: SnippetCreateRequestDTO
    ): ResponseEntity<UUID> {
        return ResponseEntity(snippetService.createSnippet(snippet, principal.name, token), HttpStatus.CREATED)
    }

    @GetMapping("/snippet/{id}")
    fun getSnippet(
        @PathVariable id: UUID,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<SnippetResponseDTO> {
        return ResponseEntity(snippetService.getSnippet(id, token), HttpStatus.OK)
    }

    @DeleteMapping("/snippet/{id}")
    fun deleteSnippet(
        @PathVariable id: UUID,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Boolean> {
        return ResponseEntity(snippetService.deleteSnippet(id, token), HttpStatus.OK)
    }
}