package ingsis.snippetmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.metrics.export.datadog.EnableDatadogMetrics

@SpringBootApplication
@EnableDatadogMetrics
class SnippetManagerApplication

fun main(args: Array<String>) {
    runApplication<SnippetManagerApplication>(*args)
}
