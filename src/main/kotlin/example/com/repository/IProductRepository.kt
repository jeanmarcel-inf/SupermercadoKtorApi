package example.com.repository

import example.com.model.Product

interface IProductRepository {
    suspend fun allProducts(): List<Product>
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(productId: Int): Boolean
}