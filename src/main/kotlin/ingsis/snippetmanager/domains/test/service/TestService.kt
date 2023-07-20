package ingsis.snippetmanager.domains.test.service

import ingsis.snippetmanager.domains.test.dto.CreateTestDTO
import ingsis.snippetmanager.domains.test.dto.TestResponseDTO
import ingsis.snippetmanager.domains.test.dto.TestResultDTO
import java.util.*

interface TestService {

    fun createTest(testDTO: CreateTestDTO, ownerId: String, token: String): TestResponseDTO

    fun getTest(ownerId: String, testId: UUID, token: String): TestResponseDTO

    fun runTest(ownerId: String, testId: UUID, version: String, token: String): TestResultDTO
}