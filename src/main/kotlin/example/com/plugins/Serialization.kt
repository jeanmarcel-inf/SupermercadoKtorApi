package example.com.plugins

import example.com.model.Product
import example.com.repository.ProductRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(repository: ProductRepository) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/products") {

            get {
                val products = repository.allProducts()
                call.respond(HttpStatusCode.OK, products)
            }

            post {
                try {
                    val product = call.receive<Product>()
                    repository.addProduct(product)
                    call.respond(status = HttpStatusCode.OK, product)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

//            delete {
//                try {
//                    val product = call.receive<Product>()
//                    repository.removeProduct(product)
//                    call.respond(status = HttpStatusCode.OK, "Registro deletado com sucesso!")
//                } catch (ex: Exception) {
//                    call.respond(status = HttpStatusCode.BadRequest, message = "Deu erro")
//                }
//            }

        }
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
            }
    }
}
