package trevaka

import fi.espoo.evaka.Main
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
    SpringApplicationBuilder()
        .sources(Main::class.java)
        .run(*args)
}
