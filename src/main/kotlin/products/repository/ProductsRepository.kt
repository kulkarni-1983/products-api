package products.repository

import org.springframework.stereotype.Component
import products.repository.model.Product

@Component
class ProductsRepository(

) {
  private val productMap = HashMap<String, Product>()

  fun addOrUpdateProduct(product: Product) = run {
    productMap[product.id] = product
    product
  }

  fun getProduct(id: String) = productMap[id]
  fun filterByName(productName: String) = productMap.filter { product ->
    product.value.name == productName
  }.values.toList()

  fun containsProduct(id: String) = productMap.containsKey(id)

  fun deleteProduct(id: String) = productMap.remove(id)

  fun getAllProducts(): List<Product> = productMap.values.toList()

  fun deleteAll() = productMap.clear()
}