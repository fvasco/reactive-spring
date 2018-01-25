package example

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.reactor.flux
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

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

fun getValue(id: Int) = mono {
    delay(1, TimeUnit.SECONDS)
    return@mono id
}

fun getIds() = flux {
    repeat(1000) { i ->
        send(i)
    }
}
