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
@RequestMapping("/products")
class ProductsController(private val productsService: ProductsService) {

    @GetMapping
    fun getAllProducts() = productsService.getAllProducts()

    @GetMapping("/{id}")
    fun getProductById(
        @PathVariable id: String
    ): Product = productsService.getProductById(id)

    @GetMapping("/name={name}")
    fun getProductByName(
    @PathVariable name: String
    ): Product = productsService.getProductByName(name)

    @PostMapping
    fun addProduct(
        @RequestBody product: Product
    ): Product = productsService.addProducts(product)

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: String,
        @RequestBody product: Product
    ): Product = productsService.updateProduct(id, product)

    @ExceptionHandler(ProductExists::class, ProductDoesNotExist::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleProductExists(exception: RuntimeException) = exception.toErrorResponse()
}