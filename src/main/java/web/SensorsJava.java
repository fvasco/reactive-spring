package web;

import dao.ControlUnit;
import dao.Database;
import dao.IotClient;
import data.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;

import java.util.Map;

@RestController
@SuppressWarnings("unused")
public class SensorsJava {

    @Autowired
    private Database database;

    @Autowired
    private ControlUnit controlUnit;

    @Autowired
    private IotClient iotClient;

    /**
     * Return all sensors
     */
    @GetMapping("/sensorsJava")
    public Flux<Sensor> allSensors() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return only sensor with valid (finite) value
     */
    @GetMapping("/sensorsJava/active")
    public ParallelFlux<Sensor> countActive() {
        throw new UnsupportedOperationException();
    }

    /**
     * Associate each sensor with the average over three values out of range.
     */
    @GetMapping("/sensorsJava/check")
    public ParallelFlux<Map.Entry<Sensor, Double>> checkSensor() {
        throw new UnsupportedOperationException();
    }
}

