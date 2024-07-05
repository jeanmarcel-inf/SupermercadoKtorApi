package example.com

import example.com.plugins.*
import example.com.repository.ProductRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = ProductRepository()

    configureSerialization(repository)
    configureDatabases()
    configureRouting()
}
