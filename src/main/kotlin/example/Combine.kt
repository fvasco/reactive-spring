package example

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.reactive.awaitSingle
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@RestController
class Combine {
    @GetMapping("example/helloWorld")
    fun helloWorld(): Mono<String> {
        val hello = getHello()
        return getWorld()
                .flatMap { world ->
                    mono {
                        hello.awaitSingle() + ' ' + world
                    }
                }
    }
}

fun getHello() = mono {
    delay(1, TimeUnit.SECONDS)
    return@mono "Hello"
}

fun getWorld() = mono {
    delay(1, TimeUnit.SECONDS)
    return@mono "world"
}
