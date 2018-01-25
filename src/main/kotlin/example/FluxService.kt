package example

import kotlinx.coroutines.experimental.reactor.flux
import reactor.core.publisher.Flux
import java.util.*
import java.util.stream.Stream

class FluxService {

    fun collection() = Arrays.asList(1, 2, 3)

    fun stream() = Stream.of(1, 2, 3)

    fun flux() = Flux.just(1, 2, 3)

    fun emptyFlux() = Flux.empty<Int>()

    fun fluxGenerator() = flux {
        for (i in 1..3)
            send(i)
    }
}
