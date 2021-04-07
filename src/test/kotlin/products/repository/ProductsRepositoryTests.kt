package products.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import products.repository.model.Product
import java.lang.NullPointerException
import kotlin.test.*

class ProductsRepositoryTests {

  private val productsRepository = ProductsRepository()

  @BeforeEach
  fun beforeEach() {
    productsRepository.deleteAll()
  }

  @Test
  fun addOrUpdateProduct_add() {
    val productId = "product:id"
    val product = Product(productId, "product:name", "product:description", 10.0, 2.0)

    productsRepository.addOrUpdateProduct(product)

    assertEquals(product, productsRepository.getProduct(productId))
  }

  @Test
  fun addOrUpdateProduct_update() {
    val productId = "product:id"

    val product = Product(productId, "product:name", "product:description", 10.0, 2.0)
    productsRepository.addOrUpdateProduct(product)

    val productUpdated = Product(productId, "product:name", "product:description", 12.0, 3.0)
    productsRepository.addOrUpdateProduct(productUpdated)

    assertEquals(productUpdated, productsRepository.getProduct(productId))
  }

  @Test
  fun filterByName() {

    val productName = "product:name"
    val product = Product("productId", productName, "product:description", 10.0, 2.0)
    productsRepository.addOrUpdateProduct(product)

    assertEquals(listOf(product), productsRepository.filterByName(productName))
  }

  @Test
  fun filterByName_nameNotPresent() {
    val productName = "product:name"
    assertEquals(emptyList(), productsRepository.filterByName(productName))
  }

  @Test
  fun containsProduct() {
    val productId = "product:id"
    val product = Product(productId, "product:name", "product:description", 10.0, 2.0)

    productsRepository.addOrUpdateProduct(product)

    assertTrue(productsRepository.containsProduct(productId))

  }

  @Test
  fun deleteProduct() {
    val productId = "product:id"
    val product = Product(productId, "product:name", "product:description", 10.0, 2.0)

    productsRepository.addOrUpdateProduct(product)
    assertTrue(productsRepository.containsProduct(productId))

    productsRepository.deleteProduct(productId)
    assertFalse(productsRepository.containsProduct(productId))
  }

  @Test
  fun deleteProduct_InvalidId() {
    val productId = "product:id"

    val product = productsRepository.deleteProduct(productId)
    assertNull(product)
  }
}