package example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.time.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
class Combine {
    @GetMapping("example/helloWorld")
    fun helloWorld(): Mono<String> {
        val hello = getHello()
        return getWorld()
                .flatMap { world ->
                    GlobalScope.mono {
                        hello.awaitSingle() + ' ' + world
                    }
                }
    }
}

fun getHello() = GlobalScope.mono {
    delay(Duration.ofSeconds(1))
    return@mono "Hello"
}

fun getWorld() = GlobalScope.mono {
    delay(Duration.ofSeconds(1))
    return@mono "world"
}
