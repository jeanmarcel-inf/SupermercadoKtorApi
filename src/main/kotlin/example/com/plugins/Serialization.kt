package example.com.plugins

import example.com.db.ProductDAO
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
                    val createdProduct = runBlocking { repository.addProduct(product) }
                    call.respond(HttpStatusCode.Created, createdProduct)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Failed to add product: ${e.message}")
                }
            }

            route("/{id}") {
                delete {
                    val productId = call.parameters["id"]?.toIntOrNull()
                    if (productId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                        return@delete
                    }

                    val deleted = repository.deleteProduct(productId)
                    if (deleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Product not found or couldn't be deleted")
                    }
                }
            }

        }
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
            }
    }
}
