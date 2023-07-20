package ingsis.snippetmanager.domains.test.repository

import ingsis.snippetmanager.domains.model.Test
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TestRepository: JpaRepository<Test, UUID> {
}