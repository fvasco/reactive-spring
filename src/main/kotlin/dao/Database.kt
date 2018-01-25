package dao

import data.SensorConfiguration
import data.SensorType
import kotlinx.coroutines.experimental.reactor.flux
import kotlinx.coroutines.experimental.time.delay
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class Database {

    fun loadConfiguration() = flux {
        delay(latency)
        send(SensorConfiguration(SensorType.SALINITY, "FRESH", 0.0..0.5))
        send(SensorConfiguration(SensorType.SALINITY, "BRACKISH", 0.5..30.0))
        send(SensorConfiguration(SensorType.SALINITY, "SALT", 30.0..50.0))
        send(SensorConfiguration(SensorType.TEMPERATURE, "POLAR", -33.0..-7.0))
        send(SensorConfiguration(SensorType.TEMPERATURE, "TEMPERATE", -7.0..22.0))
        send(SensorConfiguration(SensorType.TEMPERATURE, "TROPICAL", 22.0..26.0))
    }

    private companion object {
        val latency: Duration = Duration.ofMillis(400)
    }
}