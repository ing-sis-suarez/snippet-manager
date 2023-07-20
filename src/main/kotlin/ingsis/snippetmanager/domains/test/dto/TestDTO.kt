package ingsis.snippetmanager.domains.test.dto

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class CreateTestDTO(
    val snippetId: UUID,
    val title: String,
    val language: String,
    val inputs: List<String>,
    val output: String
)

data class TestResponseDTO(
    val id: UUID,
    val snippetId: UUID,
    val title: String,
    val language: String,
    val inputs: List<String>,
    val output: String?,
    val createdAt: Date
)

data class TestRunRequestDTO(
    val id: UUID,
    val language: String,
    val content: String,
    val version: String,
    val inputs: List<String>,
    val output: String?,
)

data class TestResultDTO(val state: TestResultState, val message: String?)


enum class TestResultState(@get: JsonValue val state: String) {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE")
}