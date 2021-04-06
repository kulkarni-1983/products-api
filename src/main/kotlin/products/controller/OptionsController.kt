package products.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import products.controller.model.Option
import products.controller.model.Product
import products.controller.model.toErrorResponse
import products.exceptions.FailedToAddOptions
import products.exceptions.OptionDoesNotExist
import products.exceptions.ProductDoesNotExist
import products.exceptions.ProductExists
import products.services.OptionsService
import java.lang.RuntimeException

@RestController
class OptionsController(private val optionsService: OptionsService) {

    @GetMapping("/products/{id}/options")
    fun getAllOptionsForProduct(
        @PathVariable id: String
    ): List<Option> = optionsService.getAllOptionsForProduct(id)

    @GetMapping("/products/{id}/options/{optionId}")
    fun getOption(
        @PathVariable id: String,
        @PathVariable optionId: String
    ): Option = optionsService.getOption(id, optionId)

    @PostMapping("/products/{id}/options")
    fun addOption(
        @PathVariable id: String,
        @RequestBody option: Option
    ): Option = optionsService.addOption(id, option)

    @PutMapping("/products/{id}/options/{optionId}")
    fun updateOption(
        @PathVariable id: String,
        @PathVariable optionId: String,
        @RequestBody option: Option
    ): Option = optionsService.updateOption(id, optionId, option)

    @DeleteMapping("/products/{id}/options/{optionId}")
    fun deleteOption(
        @PathVariable id: String,
        @PathVariable optionId: String
    ): Option = optionsService.deleteOption(id, optionId)

    @ExceptionHandler(ProductDoesNotExist::class, OptionDoesNotExist::class, FailedToAddOptions::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleProductExists(exception: RuntimeException) = exception.toErrorResponse()
}