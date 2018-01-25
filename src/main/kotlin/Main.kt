@file:JvmName("Main")

import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<web.Application>(*args)
}
