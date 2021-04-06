package products.repository.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val deliveryPrice: Double
) {
}