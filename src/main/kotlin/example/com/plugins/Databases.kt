package example.com.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    Database.connect("jdbc:mysql://localhost:3306/mercadoapp", driver = "com.mysql.cj.jdbc.Driver",
        user = "root", password = "sql@2024")
}
