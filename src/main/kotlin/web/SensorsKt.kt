package web

import dao.ControlUnit
import dao.Database
import dao.IotClient
import data.Sensor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.ParallelFlux

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
    fun allSensors(): Flux<Sensor> = TODO()


    /**
     * Return only sensor with valid (finite) value
     */
    @GetMapping("/sensorsKt/active")
    fun countActive(): ParallelFlux<Sensor> = TODO()

    /**
     * Associate each sensor with the average over three values out of range.
     */
    @GetMapping("/sensorsKt/check")
    fun checkSensor(): ParallelFlux<Map.Entry<Sensor, Double>> = TODO()
}
