package ingsis.snippetmanager.domains.manager.controller

import ingsis.snippetmanager.domains.manager.dto.SnippetDTO
import ingsis.snippetmanager.domains.manager.model.Snippet
import ingsis.snippetmanager.domains.manager.service.SnippetService
import org.springframework.beans.factory.annotation.Autowired
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


    @GetMapping("hello")
    fun hello() = snippetService.getSnippet(UUID.fromString("d4c4939f-73fc-4cf9-bd8d-f7b74c4bbc7a"))
}