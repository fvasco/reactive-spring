package example

import kotlinx.coroutines.experimental.future.await
import mapNotNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class Combine2 {
    @GetMapping("example/helloWorld2")
    fun helloWorld(): Mono<String> {
        val hello = getHello().toFuture()
        return getWorld().mapNotNull { world ->
            hello.await() + ' ' + world
        }
    }
}
