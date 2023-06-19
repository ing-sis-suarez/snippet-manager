package ingsis.snippetmanager.domains.manager.service

import ingsis.snippetmanager.domains.manager.dto.SnippetDTO
import ingsis.snippetmanager.domains.manager.model.Snippet
import ingsis.snippetmanager.domains.manager.repository.ShareRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SnippetServiceImpl: SnippetService {

    @Autowired
    private var shareRepository: ShareRepository

    constructor(shareRepository: ShareRepository) {
        this.shareRepository = shareRepository
    }

    override fun createSnippet(snippet: SnippetDTO, userId: String): Snippet {
        TODO("Not yet implemented")
    }

    override fun getSnippet(id: UUID): Snippet {
        return shareRepository.findById(id).get()
    }
}