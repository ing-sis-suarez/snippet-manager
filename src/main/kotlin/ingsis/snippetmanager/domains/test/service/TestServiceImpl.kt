package ingsis.snippetmanager.domains.test.service

import ingsis.roles.error.HTTPError
import ingsis.snippetmanager.domains.model.Test
import ingsis.snippetmanager.domains.roles.service.RolesService
import ingsis.snippetmanager.domains.snippets.repository.SnippetRepository
import ingsis.snippetmanager.domains.snippets.service.SnippetService
import ingsis.snippetmanager.domains.test.dto.CreateTestDTO
import ingsis.snippetmanager.domains.test.dto.TestResponseDTO
import ingsis.snippetmanager.domains.test.dto.TestResultDTO
import ingsis.snippetmanager.domains.test.dto.TestRunRequestDTO
import ingsis.snippetmanager.domains.test.repository.TestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class TestServiceImpl: TestService {

    @Autowired
    private var testRepository: TestRepository

    @Autowired
    private var snippetService: SnippetService

    @Autowired
    private var rolesService: RolesService

    @Autowired
    private var snippetRepository: SnippetRepository

    @Autowired
    constructor(testRepository: TestRepository, snippetService: SnippetService, rolesService: RolesService, snippetRepository: SnippetRepository) {
        this.testRepository = testRepository
        this.snippetService = snippetService
        this.rolesService = rolesService
        this.snippetRepository = snippetRepository
    }

    override fun createTest(testDTO: CreateTestDTO, ownerId: String, token: String): TestResponseDTO {
        val roles = rolesService.getRoles(testDTO.snippetId, token, "snippet")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Snippet not found", HttpStatus.NOT_FOUND)
        if (!roles.body!!.roles.contains("write")) throw HTTPError("You don't have permission to create a test", HttpStatus.UNAUTHORIZED)
        val testFromDTO = prepareEntity(testDTO)
        val test = testRepository.save(testFromDTO)
        val response = rolesService.createResource(ownerId, "test", test.id!!, token)
        if (response.statusCode != HttpStatus.CREATED) {
            testRepository.delete(test)
            throw HTTPError("Error creating test", response.statusCode)
        }
        return test.toDTO()
    }

    override fun getTest(ownerId: String, testId: UUID, token: String): TestResponseDTO {
        val roles = rolesService.getRoles(testId, token, "test")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Test not found", HttpStatus.NOT_FOUND)
        if (!roles.body!!.roles.contains("read")) throw HTTPError("You don't have permission to read this test", HttpStatus.UNAUTHORIZED)
        val test = testRepository.findById(testId)
        if (test.isEmpty) throw HTTPError("Test not found", HttpStatus.NOT_FOUND)
        return test.get().toDTO()
    }

    override fun runTest(ownerId: String, testId: UUID, version: String, token: String): TestResultDTO {
        val roles = rolesService.getRoles(testId, token, "test")
        if (roles.statusCode != HttpStatus.OK) throw HTTPError("Test not found", HttpStatus.NOT_FOUND)
        if (!roles.body!!.roles.contains("run")) throw HTTPError("You don't have permission to read this test", HttpStatus.UNAUTHORIZED)
        val test = testRepository.findById(testId).get()
        val snippet = test.snippet
        val body = TestRunRequestDTO(
            test.id!!,
            test.language!!,
            snippet!!.content!!,
            version,
            test.inputs!!.split("\n"),
            test.output!!
        )
        return runTest(body, token)
    }


    private fun prepareEntity(testDTO: CreateTestDTO): Test {
        val snippet = snippetRepository.findById(testDTO.snippetId)
        if (snippet.isEmpty) throw HTTPError("Snippet not found", HttpStatus.NOT_FOUND)
        val testFromDTO = Test(testDTO)
        testFromDTO.snippet = snippet.get()
        return testFromDTO
    }

    private fun runTest(body: TestRunRequestDTO, token: String): TestResultDTO {
        val url = System.getenv("RUNNER_URI") + "/test"
        val template = RestTemplate()
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        httpHeaders.set("Authorization", token)
        val requestEntity = HttpEntity<TestRunRequestDTO>(body, httpHeaders)
        val response = template.exchange(url, HttpMethod.POST, requestEntity, TestResultDTO::class.java)
        if (response.statusCode != HttpStatus.OK) throw HTTPError("Error running test", response.statusCode)
        return response.body!!
    }
}