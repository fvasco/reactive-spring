package example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.CompletableFuture

class MonoService {

    fun single() = "hello"

    fun optional() = Optional.of("hello")

    fun future() = CompletableFuture.completedFuture("hello")

    fun emptyMono() = Mono.empty<String>()

    fun mono() = Mono.just("hello")

    fun futureGenerator() = GlobalScope.future { "hello" }

    fun emptyMonoGenerator() = GlobalScope.mono { null }

    fun monoGenerator() = GlobalScope.mono { "hello" }
}
