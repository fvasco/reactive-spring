package example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactor.mono

fun routine() {
    val mono = GlobalScope.mono {
        System.err.println("--> mono")
        Thread.dumpStack()
        subroutine()
    }
    mono.block()
}

suspend fun subroutine() {
    System.err.println("--> subroutine1")
    Thread.dumpStack()
    System.err.println("--> subroutine2")
    Thread.dumpStack()
}
