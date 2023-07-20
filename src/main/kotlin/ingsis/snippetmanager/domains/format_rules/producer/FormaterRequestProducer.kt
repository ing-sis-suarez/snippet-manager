package ingsis.snippetmanager.domains.format_rules.producer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import spring.mvc.redis.streams.RedisStreamProducer
import java.util.*


@Component
class FormatRequestProducer @Autowired constructor(
    @Value("\${redis.stream.request_formater_key}") streamKey: String,
    redis: RedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {
    suspend fun publishEvent(event: FormatRequestEvent) {
        println("Publishing on stream: $event")
        emit(event)
    }
}

data class FormatRequestEvent(
    val snippetId: UUID,
    val formatRulesId: UUID,
    val token: String
)