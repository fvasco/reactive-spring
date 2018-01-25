import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.reactive.awaitSingle
import kotlinx.coroutines.experimental.reactor.mono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KProperty

/**
 * Returns a Mono containing only the non-null result of applying the given [transform] function
 * to the optional element in the original Mono.
 */
fun <T : Any, R : Any> Mono<T>.mapNotNull(mapper: suspend (T) -> R?) =
        flatMap { elem -> mono { mapper(elem) } }

/**
 * Returns a Flux containing only the non-null results of applying the given [transform] function
 * to each element in the original Flux.
 */
fun <T : Any, R : Any> Flux<T>.mapNotNull(transform: suspend (T) -> R?) =
        flatMap { elem -> mono { transform(elem) } }

/**
 * Returns a ParallelFlux containing only the non-null results of applying the given [transform] function
 * to each element in the original ParallelFlux.
 */
fun <T : Any, R : Any> ParallelFlux<T>.mapNotNull(transform: suspend (T) -> R?) =
        flatMap { elem -> mono { transform(elem) } }

/**
 * Consume each Flux element in parallel
 */
suspend fun <T : Any, R : Any> Flux<T>.consumeEachParallel(block: suspend (T) -> R?) {
    map {
        launch {
            block(it)
        }
    }
            .collectList()
            .awaitSingle()
            .forEach { it.join() }
}

suspend operator fun <T> CompletableFuture<T>.getValue(thisRef: Any?, property: KProperty<*>): T = await()
