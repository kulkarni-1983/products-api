package products.services

import org.springframework.stereotype.Component
import products.common.generateId
import products.exceptions.FailedToAddOptions
import products.exceptions.OptionDoesNotExist
import products.exceptions.ProductDoesNotExist
import products.controller.model.Option as ControllerOption
import products.repository.OptionsRepository
import products.repository.ProductsRepository
import products.repository.model.Option

@Component
class OptionsService(
    private val optionsRepository: OptionsRepository,
    private val productsRepository: ProductsRepository
) {

    fun addOption(productId: String, option: ControllerOption) = run {
        if(!productsRepository.containsProduct(productId))
            throw ProductDoesNotExist("Product with id: $productId doesn't exist")
        try {
            optionsRepository.addOrUpdateOption(option.toRepositoryOption(productId)).toControllerOption()
        } catch (e: Exception) {
            throw FailedToAddOptions("Failed to add option with product id: $productId")
        }

    }

    fun updateOption(productId: String, id:String, option: ControllerOption) = run {
        if(!productsRepository.containsProduct(productId))
            throw ProductDoesNotExist("Product with id: $productId doesn't exist")
        if(!optionsRepository.containsOption(productId, id))
            throw OptionDoesNotExist("Option with id: $id and product id: $productId doesn't exist")
        try {
            optionsRepository.addOrUpdateOption(option.toRepositoryOption(productId, id)).toControllerOption()
        } catch (e: Exception) {
            throw FailedToAddOptions("Failed to update option with id: $id and product id: $productId")
        }
    }

    fun deleteOption(productId: String, id: String) = try {
        optionsRepository.deleteOption(productId, id).toControllerOption()
    } catch (e: Exception) {
        throw OptionDoesNotExist("Failed to delete option with productId: $productId and option id: $id")
    }

    fun getAllOptionsForProduct(productId: String) = try {
        optionsRepository.getOptionsForProduct(productId).map { it.toControllerOption() }
    } catch (e: Exception) {
        throw OptionDoesNotExist("Options does not exist for productId: $productId")
    }

    fun getOption(productId: String, id: String) = try {
        optionsRepository.getOption(productId, id).toControllerOption()
    } catch (e: Exception) {
        throw OptionDoesNotExist("Option does not exist with productId: $productId and option id: $id")
    }

    companion object {
        private fun ControllerOption.toRepositoryOption(productKey: String, id:String? = null) =
            Option(id ?: generateId(),productKey, name, description)

        private fun Option.toControllerOption() =
            ControllerOption(id, productId, name, description)
    }
}