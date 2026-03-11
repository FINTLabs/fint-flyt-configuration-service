package no.novari

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "tokenAuditorAware")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
