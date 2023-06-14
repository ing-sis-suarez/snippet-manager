package printscript.snippetmanager

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class SnippetController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello, world!"
    }
}