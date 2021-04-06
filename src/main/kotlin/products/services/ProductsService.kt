package products.services

import org.springframework.stereotype.Component
import products.common.generateId
import products.exceptions.ProductDoesNotExist
import products.controller.model.Product as ControllerProduct
import products.exceptions.ProductExists

import products.repository.ProductsRepository
import products.repository.model.Product

@Component
class ProductsService(private val productsRepository: ProductsRepository) {

    fun addProducts(product: ControllerProduct) = run {
        if(productsRepository.filterByName(product.name).isNotEmpty()) {
            throw ProductExists("Product cannot be added: Product with ${product.name} already exists")
        }
        productsRepository.addOrUpdateProduct(product.toRepositoryProduct()).toControllerProduct()
    }

    fun getProductById(id: String) = try {
        productsRepository.getProduct(id)!!.toControllerProduct()
    } catch (e: Exception) {
        throw ProductDoesNotExist("Product with id: $id doesn't exist")
    }

    fun getProductByName(name: String) = run {
        val products = productsRepository.filterByName(name)
        if(products.isEmpty()) {
            throw ProductDoesNotExist("Product with name: $name doesn't exist")
        }
        products[0].toControllerProduct()
    }

    fun updateProduct(id: String, product: ControllerProduct) = run {
        productsRepository.getProduct(id)?.run {
            productsRepository.addOrUpdateProduct(product.toRepositoryProduct(id)).toControllerProduct()
        } ?: throw ProductDoesNotExist("Product with id: $id doesn't exist")
    }

    fun getAllProducts() = productsRepository.getAllProducts().map{ it.toControllerProduct()}

    companion object {
        private fun ControllerProduct.toRepositoryProduct(id:String? = null) =
            Product(id ?: generateId() , name, description, price, deliveryPrice)

        private fun Product.toControllerProduct() =
            ControllerProduct(id, name, description, price, deliveryPrice)
    }

}