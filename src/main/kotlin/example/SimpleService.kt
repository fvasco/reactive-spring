package example

import java.time.Instant
import java.util.*
import java.util.concurrent.CompletableFuture

class SimpleService {

    fun now() = Instant.now()

    fun optional() = Optional.of("hello")

    fun list() = Arrays.asList("java", "util", "List")

    fun future() = CompletableFuture.completedFuture("hello")
}
