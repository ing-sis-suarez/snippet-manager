package ingsis.snippetmanager.domains.lint_rules.producer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import spring.mvc.redis.streams.RedisStreamProducer
import java.util.*


@Component
class LintRequestProducer @Autowired constructor(
    @Value("\${redis.stream.request_linter_key}") streamKey: String,
    redis: RedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {
    suspend fun publishEvent(event: LintRequestEvent) {
        println("Publishing on stream: $event")
        emit(event)
    }
}

data class LintRequestEvent(
    val snippetId: UUID,
    val lintRulesId: UUID,
    val token: String
)