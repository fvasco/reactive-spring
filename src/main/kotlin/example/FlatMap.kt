package example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactor.flux
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.time.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
class FlatMap {
    @GetMapping("example/get1")
    fun get1() =
            getValue(1)

    @GetMapping("example/getAll")
    fun getAll() =
            getIds()
                    .parallel()
                    .flatMap { id -> getValue(id) }
}

fun getValue(id: Int) = GlobalScope.mono {
    delay(Duration.ofSeconds(1))
    return@mono id
}

fun getIds() = GlobalScope.flux {
    repeat(1000) { i ->
        send(i)
    }
}
