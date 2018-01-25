package example

import kotlinx.coroutines.experimental.reactor.mono
import kotlinx.coroutines.experimental.yield

fun routine() {
    val mono = mono {
        System.err.println("--> mono")
        Thread.dumpStack()
        subroutine()
    }
    mono.block()
}

suspend fun subroutine() {
    System.err.println("--> subroutine1")
    Thread.dumpStack()
    yield()
    System.err.println("--> subroutine2")
    Thread.dumpStack()
}
