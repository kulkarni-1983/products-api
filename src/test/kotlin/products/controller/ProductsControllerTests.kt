package products.controller

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import products.controller.model.ErrorResponse
import products.controller.model.Product
import products.exceptions.ProductDoesNotExist
import products.exceptions.ProductExists
import products.services.ProductsService
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProductsControllerTests {

  private val productsService = mock<ProductsService>()
  private val productsController = ProductsController(productsService)

  @BeforeEach
  fun beforeEach() {
    reset(productsService)
  }

  @Test
  fun getAllProducts() {
    val id = "product:id"
    val product = Product(id, "product-name", "product-description", 10.0, 2.0)
    whenever(productsService.getAllProducts()).thenReturn(listOf(product))

    val result = productsController.getAllProducts()

    verify(productsService, times(1)).getAllProducts()

    assertEquals(listOf(product), result)
  }

  @Test
  fun getProductById() {
    val id = "product:id"
    val product = Product(id, "product-name", "product-description", 10.0, 2.0)
    whenever(productsService.getProductById(id)).thenReturn(product)

    val result = productsController.getProductById(id)

    verify(productsService, times(1)).getProductById(id)
    assertEquals(product, result)
  }

  @Test
  fun getProductById_InvalidId() {
    val id = "product:id"
    whenever(productsService.getProductById(id)).thenThrow(
      ProductDoesNotExist("Product does not exist")
    )

    assertFailsWith<ProductDoesNotExist> {
      productsController.getProductById(id)
    }
    verify(productsService, times(1)).getProductById(id)
  }

  @Test
  fun getProductByName() {
    val id = "product:id"
    val name = "product-name"
    val product = Product(id, name, "product-description", 10.0, 2.0)
    whenever(productsService.getProductByName(name)).thenReturn(listOf(product))

    val result = productsController.getProductByName(name)

    verify(productsService, times(1)).getProductByName(name)
    assertEquals(listOf(product), result)
  }

  @Test
  fun getProductByName_InvalidName() {
    val name = "product-name"
    whenever(productsService.getProductByName(name)).thenThrow(
      ProductDoesNotExist("Product does not exist")
    )

    assertFailsWith<ProductDoesNotExist> {
      productsController.getProductByName(name)
    }
    verify(productsService, times(1)).getProductByName(name)
  }

  @Test
  fun addProduct() {
    val id = "product:id"
    val product = Product(id, "product-name", "product-description", 10.0, 2.0)
    whenever(productsService.addProduct(product)).thenReturn(product)

    val result = productsController.addProduct(product)

    verify(productsService, times(1)).addProduct(product)
    assertEquals(product, result)
  }

  @Test
  fun addProduct_NameExists() {
    val id = "product:id"
    val product = Product(id, "product-name", "product-description", 10.0, 2.0)
    whenever(productsService.addProduct(product)).thenThrow(ProductExists("Product with name already exists"))

    assertFailsWith<ProductExists> {
      productsController.addProduct(product)
    }
    verify(productsService, times(1)).addProduct(product)
  }

  @Test
  fun updateProduct() {
    val id = "product:id"
    val product = Product(id, "product-name", "product-description", 10.0, 2.0)
    whenever(productsService.updateProduct(id, product)).thenReturn(product)

    val result = productsController.updateProduct(id, product)

    verify(productsService, times(1)).updateProduct(id, product)
    assertEquals(product, result)
  }

  @Test
  fun updateProduct_ProductDoesNotExist() {
    val id = "product:id"
    val product = Product(id, "product-name", "product-description", 10.0, 2.0)
    whenever(productsService.updateProduct(id, product)).thenThrow(
      ProductDoesNotExist("Product with name does not exist"))

    assertFailsWith<ProductDoesNotExist> {
      productsController.updateProduct(id, product)
    }
    verify(productsService, times(1)).updateProduct(id, product)
  }

  @Test
  fun deleteProduct() {
    val id = "product:id"
    val product = Product(id, "product-name", "product-description", 10.0, 2.0)
    whenever(productsService.deleteProduct(id)).thenReturn(product)

    val result = productsController.deleteProduct(id)

    verify(productsService, times(1)).deleteProduct(id)
    assertEquals(product, result)
  }

  @Test
  fun deleteProduct_ProductDoesNotExist() {
    val id = "product:id"
    whenever(productsService.deleteProduct(id)).thenThrow(
      ProductDoesNotExist("Product with name does not exist"))

    assertFailsWith<ProductDoesNotExist> {
      productsController.deleteProduct(id)
    }
    verify(productsService, times(1)).deleteProduct(id)
  }

  @Test
  fun handleException() {
    val errorMessage = "Product does not exist"
    val result = productsController.handleException(RuntimeException(errorMessage))
    assertEquals(ErrorResponse(errorMessage), result)
  }
}