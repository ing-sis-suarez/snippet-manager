package ingsis.snippetmanager.domains.users.controller

import ingsis.snippetmanager.domains.users.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@CrossOrigin("*")
class UserController {

    @Autowired
    private var userService: UserService

    @Autowired
    constructor(userService: UserService) {
        this.userService = userService
    }

    @GetMapping("/users")
    fun findUsers(): Any {
        return userService.findUsers()
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }
}