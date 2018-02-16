package web

import dao.ControlUnit
import dao.Database
import dao.IotClient
import data.Sensor
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.reactive.awaitFirst
import kotlinx.coroutines.experimental.reactive.awaitSingle
import mapNotNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.ParallelFlux
import java.util.*

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
    fun countActive(): ParallelFlux<Sensor> = controlUnit
            .getSensors()
            .parallel()
            .mapNotNull { sensor ->
                val value = iotClient.query(sensor).awaitFirst()
                sensor.takeIf { value.isFinite() }
            }

    /**
     * Associate each sensor with the average over three values out of range.
     */
    @GetMapping("/sensorsKt/check")
    fun checkSensor(): ParallelFlux<Map.Entry<Sensor, Double>> {
        val rangeMapFuture = database.loadConfiguration()
                .collectMap({ it.sensorType to it.context }, { it.validRange })
                .toFuture()

        return controlUnit
                .getSensors()
                .parallel()
                .mapNotNull { sensor ->
                    val average = iotClient.query(sensor)
                            .take(3)
                            .collectList()
                            .awaitSingle()
                            .filter { it.isFinite() }
                            .average()
                    val rangeMap = rangeMapFuture.await()
                    if (average !in rangeMap.getValue(sensor.type to sensor.context))
                        AbstractMap.SimpleEntry(sensor, average)
                    else null
                }
    }
}
