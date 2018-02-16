package web;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import dao.ControlUnit;
import dao.Database;
import dao.IotClient;
import data.Sensor;
import data.SensorConfiguration;
import data.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

@RestController
public class SensorsJava {

    @Autowired
    private ControlUnit controlUnit;

    @Autowired
    private Database database;

    @Autowired
    private IotClient iotClient;

    @GetMapping("/sensorsJava")
    public Flux<Sensor> allSensors() {
        return controlUnit.getSensors();
    }

    @GetMapping("/sensorsJava/active")
    public ParallelFlux<Sensor> countActive() {
        return controlUnit.getSensors()
                          .parallel()
                          .flatMap(sensor -> iotClient.query(sensor)
                                                      .take(1)
                                                      .flatMap(d -> (d.isNaN()) ? Mono.empty() : Mono.fromSupplier(() -> sensor)));
    }

    /**
     * Associate each sensor with the average over three values out of range.
     */
    @GetMapping("/sensorsJava/check")
    public ParallelFlux<Map.Entry<Sensor, Double>> checkSensor() {
        Mono<Map<SensorTypeContext, SensorConfiguration>> rangeMap = database.loadConfiguration()
                                                                             .collectMap(SensorsJava::configToSTC)
                                                                             .cache();

        Mono.from(rangeMap).subscribe(rm -> System.out.println("fetched configuration.. : " + rm));

        return controlUnit.getSensors()
                          .parallel()
                          .flatMap(sensor -> {
                              return iotClient.query(sensor)
                                              .take(3)
                                              .filter(d -> !d.isNaN())
                                              .collectList()
                                              .map(list -> (list.isEmpty()) ? Double.NaN : list.stream().collect(Collectors.averagingDouble(d -> d)))
                                              .filterWhen(d -> Mono.from(rangeMap).<Boolean> map(rm -> !rm.get(new SensorTypeContext(sensor.getType(), sensor.getContext())).getValidRange().contains(d)))
                                              .map(d -> new AbstractMap.SimpleEntry<>(sensor, d));
            }
        );
    }

    public static SensorTypeContext configToSTC(SensorConfiguration config) {
        return new SensorTypeContext(config.getSensorType(), config.getContext());
    }

    public static class SensorTypeContext {

        private final SensorType sensorType;
        private final String context;

        public SensorTypeContext(SensorType sensorType, String context) {
            this.sensorType = sensorType;
            this.context = context;
        }

        public SensorType getSensorType() {
            return sensorType;
        }

        public String getContext() {
            return context;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((context == null) ? 0 : context.hashCode());
            result = prime * result + ((sensorType == null) ? 0 : sensorType.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SensorTypeContext other = (SensorTypeContext) obj;
            if (context == null) {
                if (other.context != null)
                    return false;
            } else if (!context.equals(other.context))
                return false;
            if (sensorType != other.sensorType)
                return false;
            return true;
        }

    }
}
