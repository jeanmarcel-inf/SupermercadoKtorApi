package example.com.repository

import example.com.db.*
import example.com.model.Product
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ProductRepository : IProductRepository {

    override suspend fun allProducts(): List<Product> = suspendTransaction {
        ProductDAO.all().map(::daoToModel)
    }

    override suspend fun addProduct(product: Product): Product = suspendTransaction {
        val generatedId = ProductTable.insertAndGetId {
            it[name] = product.name
            it[price] = product.price
            it[category] = product.category
        }.value // Obtém o ID gerado após a inserção

        product.copy(productId = generatedId) // Retorna o objeto Product com o ID preenchido
    }

    override suspend fun deleteProduct(productId: Int): Boolean {
        return try {
            transaction {
                val deletedRows = ProductTable.deleteWhere { ProductTable.id eq productId }
                deletedRows > 0
            }
        } catch (e: Exception) {
            // Log the exception or handle it as needed
            false
        }
    }
}