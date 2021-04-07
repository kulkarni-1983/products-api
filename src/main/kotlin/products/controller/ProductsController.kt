package products.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import products.controller.model.Product
import products.controller.model.toErrorResponse
import products.exceptions.ProductDoesNotExist
import products.exceptions.ProductExists

import products.services.ProductsService
import java.lang.RuntimeException

@RestController
class ProductsController(private val productsService: ProductsService) {

  @GetMapping("/products")
  fun getAllProducts() = productsService.getAllProducts()

  @GetMapping("/products/{id}")
  fun getProductById(
    @PathVariable id: String
  ): Product = productsService.getProductById(id)

  @GetMapping("/products/name={name}")
  fun getProductByName(
    @PathVariable name: String
  ): Product = productsService.getProductByName(name)

  @PostMapping("/products")
  fun addProduct(
    @RequestBody product: Product
  ): Product = productsService.addProduct(product)

  @PutMapping("/products/{id}")
  fun updateProduct(
    @PathVariable id: String,
    @RequestBody product: Product
  ): Product = productsService.updateProduct(id, product)

  @DeleteMapping("/products/{id}")
  fun deleteProduct(
    @PathVariable id: String
  ): Product = productsService.deleteProduct(id)

  @ExceptionHandler(ProductExists::class, ProductDoesNotExist::class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  fun handleException(exception: RuntimeException) = exception.toErrorResponse()
}