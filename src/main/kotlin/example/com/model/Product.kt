package example.com.model
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val productId: Int,
    val name: String,
    val price: Float,
    val category: String
)
