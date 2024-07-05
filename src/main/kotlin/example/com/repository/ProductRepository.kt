package example.com.repository

import example.com.db.ProductDAO
import example.com.db.ProductTable
import example.com.db.daoToModel
import example.com.db.suspendTransaction
import example.com.model.Product
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

class ProductRepository : IProductRepository {
    override suspend fun allProducts(): List<Product> = suspendTransaction {
        ProductDAO.all().map(::daoToModel)
    }

    override suspend fun addProduct(product: Product) : Unit = suspendTransaction {
        ProductDAO.new {
            name = product.name
            price = product.price
            category = product.category
        }

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