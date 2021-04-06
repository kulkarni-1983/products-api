package products.repository

import org.springframework.stereotype.Component
import products.repository.model.Option
import products.repository.model.Product

@Component
class OptionsRepository(

) {
    private val productAndOptionMap: MutableMap<String, MutableMap<String, Option>> = HashMap()

    fun addOrUpdateOption(option: Option) = run {
        if(!productAndOptionMap.containsKey(option.productId)) {
            productAndOptionMap[option.productId] = HashMap()
        }
        productAndOptionMap[option.productId]!![option.id] = option
        option
    }

    fun getOptionsForProduct(productId: String) =
        productAndOptionMap[productId]!!.values.toList()

    fun getOption(productId: String, id: String) = productAndOptionMap[productId]!![id]!!

    fun containsOption(productId: String, id: String) =
        productAndOptionMap.containsKey(productId) && productAndOptionMap[productId]!!.containsKey(id)

    fun deleteOption(productId: String, id: String) = productAndOptionMap[productId]!!.remove(id)!!

}