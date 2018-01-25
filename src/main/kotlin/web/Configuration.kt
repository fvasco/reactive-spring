package web

import dao.Database
import data.SensorConfiguration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class Configuration(private val database: Database) {

    @GetMapping("/configuration")
    fun count(): Flux<SensorConfiguration> = database.loadConfiguration()
}