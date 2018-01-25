package dao

import data.Sensor
import data.SensorType
import kotlinx.coroutines.experimental.reactor.flux
import kotlinx.coroutines.experimental.time.delay
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class IotClient {

    fun query(sensor: Sensor) = flux {
        var step = 1
        while (isActive) {
            delay(latency + Duration.ofMillis(sensor.id.toLong()))
            val value = when (sensor.type to sensor.context) {
                SensorType.SALINITY to "FRESH" -> generate(step, sensor.id, 0.0..0.5)
                SensorType.SALINITY to "BRACKISH" -> generate(step, sensor.id, 0.5..30.0)
                SensorType.SALINITY to "SALT" -> generate(step, sensor.id, 30.0..50.0)
                SensorType.TEMPERATURE to "POLAR" -> generate(step, sensor.id, -33.0..-7.0)
                SensorType.TEMPERATURE to "TEMPERATE" -> generate(step, sensor.id, -7.0..22.0)
                SensorType.TEMPERATURE to "TROPICAL" -> generate(step, sensor.id, 22.0..26.0)
                else -> error("Unsupported sensor $sensor")
            }
            send(value)
            step++
        }
    }

    private companion object {
        val latency: Duration = Duration.ofSeconds(1)

        fun generate(step: Int, id: Int, range: ClosedFloatingPointRange<Double>) =
                when (id) {
                    7 -> step.toDouble()
                    17 -> Double.NaN
                    step -> Double.NaN
                    else -> range.start + Random(step.toLong() xor id.toLong().shl(32) xor range.start.toBits()).nextDouble() * (range.endInclusive - range.start)
                }
    }
}