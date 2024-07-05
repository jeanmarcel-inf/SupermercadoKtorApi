package example.com.repository

import example.com.db.ProductDAO
import example.com.db.ProductTable
import example.com.db.daoToModel
import example.com.db.suspendTransaction
import example.com.model.Product
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

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

    override suspend fun removeProduct(product: Product): Boolean = suspendTransaction {
        val rowsDeleted = ProductTable.deleteWhere {
            ProductTable.name eq name
        }
        rowsDeleted == 1
    }
}