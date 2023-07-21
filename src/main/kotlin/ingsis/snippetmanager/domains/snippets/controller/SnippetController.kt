package ingsis.snippetmanager.domains.snippets.controller

import ingsis.snippetmanager.domains.snippets.dto.SnippetCreationResponseDTO
import ingsis.snippetmanager.domains.snippets.dto.SnippetDataRequestDTO
import ingsis.snippetmanager.domains.snippets.dto.SnippetResponseDTO
import ingsis.snippetmanager.domains.snippets.dto.UpdateSnippetDTO
import ingsis.snippetmanager.domains.snippets.service.SnippetService
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
        @RequestBody snippet: SnippetDataRequestDTO,
        principal: Principal
    ): ResponseEntity<SnippetCreationResponseDTO> {
        return ResponseEntity(snippetService.createSnippet(snippet, principal.name, token), HttpStatus.CREATED)
    }

    @GetMapping("/owned-snippets")
    fun getOwnedSnippets(
        @RequestHeader("Authorization") token: String,
        principal: Principal
    ): ResponseEntity<List<SnippetResponseDTO>> {
        return ResponseEntity(snippetService.getOwnedSnippets(token), HttpStatus.OK)
    }

    @GetMapping("/readable-snippets")
    fun getReadableSnippets(
        @RequestHeader("Authorization") token: String,
        principal: Principal
    ): ResponseEntity<List<SnippetResponseDTO>> {
        return ResponseEntity(snippetService.getReadableSnippets(token), HttpStatus.OK)
    }

    @GetMapping("/all-snippets")
    fun getAllSnippets(
        @RequestHeader("Authorization") token: String,
        principal: Principal
    ): ResponseEntity<List<SnippetResponseDTO>> {
        return ResponseEntity(snippetService.getAllSnippets(token), HttpStatus.OK)
    }

    @GetMapping("/snippet/{id}")
    fun getSnippet(
        @PathVariable id: UUID,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<SnippetResponseDTO> {
        return ResponseEntity(snippetService.getSnippet(id, token), HttpStatus.OK)
    }


    @PutMapping("/snippet/{id}")
    fun updateSnippet(
        @PathVariable id: UUID,
        @RequestHeader("Authorization") token: String,
        @RequestBody snippet: UpdateSnippetDTO,
        principal: Principal
    ): ResponseEntity<SnippetResponseDTO> {
        return ResponseEntity(snippetService.updateSnippet(id, snippet, principal.name, token), HttpStatus.OK)
    }

    @DeleteMapping("/snippet/{id}")
    fun deleteSnippet(
        @PathVariable id: UUID,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Boolean> {
        return ResponseEntity(snippetService.deleteSnippet(id, token), HttpStatus.OK)
    }
}