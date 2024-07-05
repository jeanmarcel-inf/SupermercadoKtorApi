package example.com.db

import example.com.model.Product
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object ProductTable : IntIdTable("product", "ProductId" ) {
    val name = varchar("ProductName", 100)
    val price = float("Price")
    val category = varchar("Category", 40)
}

class ProductDAO(productId: EntityID<Int>) : IntEntity(productId) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var name by ProductTable.name
    var price by ProductTable.price
    var category by ProductTable.category
}

// DOCS -> suspendTransaction() takes a block of code and runs it within a database transaction, through the IO Dispatcher. This is designed to offload blocking jobs of work onto a thread pool.
suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

// DOCS -> daoToModel() transforms an instance of the DAO type to the object.
fun daoToModel(dao: ProductDAO) = Product(
    dao.name,
    dao.price,
    dao.category
)