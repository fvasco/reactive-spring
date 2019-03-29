package web

import dao.ControlUnit
import dao.Database
import dao.IotClient
import data.Sensor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.reactor.flux
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.*

@Suppress("unused")
@RestController
class SensorsKt(
        private val database: Database,
        private val controlUnit: ControlUnit,
        private val iotClient: IotClient) {

    /**
     * Return all sensors
     */
    @GetMapping("/sensorsKt")
    fun allSensors(): Flux<Sensor> = controlUnit.getSensors()


    /**
     * Return only sensor with valid (finite) value
     */
    @GetMapping("/sensorsKt/active")
    fun countActive(): Flux<Sensor> = GlobalScope.flux {
        controlUnit.getSensors().consumeEach { sensor ->
            launch {
                val value = iotClient.query(sensor).awaitFirst()
                if (value.isFinite()) send(sensor)
            }
        }
    }

    /**
     * Associate each sensor with the average over three values out of range.
     */
    @GetMapping("/sensorsKt/check")
    fun checkSensor() = GlobalScope.flux {
        val configurationDeferred = async { database.loadConfiguration().collectList().awaitFirst() }
        controlUnit.getSensors().consumeEach { sensor ->
            launch {
                val average = iotClient.query(sensor)
                        .take(3)
                        .collectList().awaitFirst()
                        .filter { it.isFinite() }
                        .takeIf { it.isNotEmpty() }
                        ?.average()

                val configuration =
                        checkNotNull(configurationDeferred.await().find { it.sensorType == sensor.type && it.context == sensor.context })
                if (average != null && average !in configuration.validRange) {
                    send(AbstractMap.SimpleEntry(sensor, average)) // sent to Flux
                }
            }
        }
    }
}
