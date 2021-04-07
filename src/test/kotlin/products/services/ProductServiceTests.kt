package products.services

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import products.exceptions.ProductDoesNotExist
import products.exceptions.ProductExists
import products.controller.model.Product as ControllerProduct
import products.repository.ProductsRepository
import products.repository.model.Product
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class ProductServiceTests {

  private val productsRepository = mock<ProductsRepository>()
  private val productsService = ProductsService(productsRepository)

  @Test
  fun addProduct() {
    val controllerProduct = ControllerProduct(
      null, "product:name", "product:description", 10.0, 2.0)

    whenever(productsRepository.addOrUpdateProduct(any())).thenAnswer { it.arguments[0] }

    val result = productsService.addProduct(controllerProduct)
    assertEquals(controllerProduct.name, result.name)
    assertEquals(controllerProduct.description, result.description)
    assertEquals(controllerProduct.price, result.price)
    assertEquals(controllerProduct.deliveryPrice, result.deliveryPrice)
    assertNotNull(result.id)
  }

  @Test
  fun addProduct_productExists() {
    val controllerProduct = ControllerProduct(
      "product:id", "product:name", "product:description", 10.0, 2.0)

    whenever(productsRepository.containsProduct(controllerProduct.id!!)).thenReturn(true)

    assertFailsWith<ProductExists> {
      productsService.addProduct(controllerProduct)
    }
  }

  @Test
  fun getProductById() {
    val productId = "product:id"
    val product = Product(
      productId, "product:name", "product:description", 10.0, 2.0)
    val controllerProduct = ControllerProduct(
      productId, "product:name", "product:description", 10.0, 2.0
    )
    whenever(productsRepository.getProduct(productId)).thenReturn(product)

    val result = productsService.getProductById(productId)

    assertEquals(controllerProduct, result)
  }

  @Test
  fun getProductById_doesNotExist() {
    val productId = "product:id"

    whenever(productsRepository.getProduct(productId)).thenReturn(null)

    assertFailsWith<ProductDoesNotExist> {
      productsService.getProductById(productId)
    }
  }

  @Test
  fun getProductByName() {
    val productId = "product:id"
    val productName = "product:name"
    val product = Product(
      productId, productName, "product:description", 10.0, 2.0)
    val controllerProduct = ControllerProduct(
      productId, productName, "product:description", 10.0, 2.0
    )
    whenever(productsRepository.filterByName(productName)).thenReturn(listOf(product))

    val result = productsService.getProductByName(productName)

    assertEquals(listOf(controllerProduct), result)
  }

  @Test
  fun getProductByName_doesNotExist() {
    val productId = "product:id"
    val productName = "product:name"
    whenever(productsRepository.filterByName(productName)).thenReturn(listOf())

    assertFailsWith<ProductDoesNotExist> {
      productsService.getProductByName(productId)
    }
  }

  @Test
  fun updateProduct() {
    val productId = "product:id"
    val controllerProduct = ControllerProduct(
      productId, "product:name", "product:description", 10.0, 2.0)
    val product = Product(
      productId, "product:name", "product:description", 10.0, 2.0)
    whenever(productsRepository.getProduct(controllerProduct.id!!)).thenReturn(product)
    whenever(productsRepository.addOrUpdateProduct(any())).thenAnswer { it.arguments[0] }

    val result = productsService.updateProduct(productId, controllerProduct)
    assertEquals(controllerProduct, result)
  }

  @Test
  fun updateProduct_productDoesNotExists() {
    val productId = "product:id"
    val controllerProduct = ControllerProduct(
      productId, "product:name", "product:description", 10.0, 2.0)

    whenever(productsRepository.getProduct(controllerProduct.id!!)).thenReturn(null)

    assertFailsWith<ProductDoesNotExist> {
      productsService.updateProduct(productId, controllerProduct)
    }
  }

  @Test
  fun getAllProducts() {
    val productId = "product:id"
    val controllerProduct = ControllerProduct(
      productId, "product:name", "product:description", 10.0, 2.0)
    val product = Product(
      productId, "product:name", "product:description", 10.0, 2.0)

    whenever(productsRepository.getAllProducts()).thenReturn(listOf(product))

    val result = productsService.getAllProducts()

    assertEquals(listOf(controllerProduct), result)
  }

  @Test
  fun deleteProduct() {
    val productId = "product:id"
    val controllerProduct = ControllerProduct(
      productId, "product:name", "product:description", 10.0, 2.0)
    val product = Product(
      productId, "product:name", "product:description", 10.0, 2.0)

    whenever(productsRepository.getProduct(controllerProduct.id!!)).thenReturn(product)
    whenever(productsRepository.deleteProduct(productId)).thenAnswer { product }

    val result = productsService.deleteProduct(productId)

    assertEquals(controllerProduct, result)
  }

  @Test
  fun deleteProduct_doesNotExist() {
    val productId = "product:id"

    whenever(productsRepository.getProduct(productId)).thenReturn(null)

    assertFailsWith<ProductDoesNotExist> {
      productsService.deleteProduct(productId)
    }
  }
  
}