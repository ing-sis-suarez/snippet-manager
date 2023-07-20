package ingsis.snippetmanager.domains.test.controller

import ingsis.snippetmanager.domains.test.dto.CreateTestDTO
import ingsis.snippetmanager.domains.test.dto.TestResponseDTO
import ingsis.snippetmanager.domains.test.dto.TestResultDTO
import ingsis.snippetmanager.domains.test.service.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*


@RestController
@CrossOrigin("*")
class TestController {

    @Autowired
    private var testService: TestService

    @Autowired
    constructor(testService: TestService) {
        this.testService = testService
    }


    @PostMapping("/test")
    fun createTest(
        @RequestHeader("Authorization") token: String,
        principal: Principal,
        @RequestBody testDTO: CreateTestDTO
    ): ResponseEntity<TestResponseDTO> {
        return ResponseEntity(testService.createTest(testDTO, principal.name, token), HttpStatus.CREATED)
    }

    @GetMapping("/test/{id}")
    fun getTest(
        @RequestHeader("Authorization") token: String,
        @PathVariable("id") id: UUID,
        principal: Principal
    ): ResponseEntity<TestResponseDTO> {
        return ResponseEntity(testService.getTest(principal.name, id, token), HttpStatus.OK)
    }

    @GetMapping("/test/run/{id}")
    fun runTest(
        @RequestHeader("Authorization") token: String,
        @PathVariable("id") id: UUID,
        @RequestParam("version") version: String,
        principal: Principal
    ): ResponseEntity<TestResultDTO> {
        return ResponseEntity(testService.runTest(principal.name, id, version, token), HttpStatus.OK)
    }
}