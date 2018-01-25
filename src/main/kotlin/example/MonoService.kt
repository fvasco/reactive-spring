package example

import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.reactor.mono
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.CompletableFuture

class MonoService {

    fun single() = "hello"

    fun optional() = Optional.of("hello")

    fun future() = CompletableFuture.completedFuture("hello")

    fun emptyMono() = Mono.empty<String>()

    fun mono() = Mono.just("hello")

    fun futureGenerator() = future { "hello" }

    fun emptyMonoGenerator() = mono { null }

    fun monoGenerator() = mono { "hello" }
}
