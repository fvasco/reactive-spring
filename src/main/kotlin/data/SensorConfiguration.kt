package data

data class SensorConfiguration(
        val sensorType: SensorType,
        val context: String,
        val validRange: ClosedFloatingPointRange<Double>
)