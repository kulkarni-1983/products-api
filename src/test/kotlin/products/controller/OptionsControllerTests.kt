package products.controller

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import products.controller.model.ErrorResponse
import products.controller.model.Option
import products.exceptions.OptionDoesNotExist
import products.exceptions.ProductDoesNotExist
import products.services.OptionsService
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OptionsControllerTests {

  private val optionsService = mock<OptionsService>()
  private val optionsController: OptionsController = OptionsController(optionsService)

  @BeforeEach
  fun beforeEach() {
    reset(optionsService);
  }

  @Test
  fun getAllOptionsForProduct() {
    val id = "test:id"
    val productId = "product:id"
    val option = Option(id, productId, "option-name", "option-description")
    whenever(optionsService.getAllOptionsForProduct(productId)).thenReturn(listOf(option))
    val result = optionsController.getAllOptionsForProduct(productId)

    verify(optionsService, times(1)).getAllOptionsForProduct(productId)
    assertEquals(listOf(option), result)
  }

  @Test
  fun getAllOptionsForProduct_invalidProductId() {
    val errorMessage = "Product does not exist"
    val id = "test:id"
    whenever(optionsService.getAllOptionsForProduct(id)).thenThrow(
      ProductDoesNotExist(errorMessage)
    )
    assertFailsWith<ProductDoesNotExist> {
      optionsController.getAllOptionsForProduct(id)
    }
    verify(optionsService, times(1)).getAllOptionsForProduct(id)
  }

  @Test
  fun getOption() {
    val id = "test:id"
    val productId = "product:id"
    val option = Option(id, productId, "option-name", "option-description")
    whenever(optionsService.getOption(productId, id)).thenReturn(option)
    val result = optionsController.getOption(productId, id)

    verify(optionsService, times(1)).getOption(productId, id)
    assertEquals(option, result)
  }

  @Test
  fun getOption_failToFetch() {
    val id = "test:id"
    val productId = "product:id"
    whenever(optionsService.getOption(productId, id)).thenThrow(
      OptionDoesNotExist("Option does not exist")
    )

    assertFailsWith<OptionDoesNotExist> {
      optionsController.getOption(productId, id)
    }
    verify(optionsService, times(1)).getOption(productId, id)
  }

  @Test
  fun addOption() {
    val id = "test:id"
    val productId = "product:id"
    val option = Option(id, productId, "option-name", "option-description")
    whenever(optionsService.addOption(productId, option)).thenReturn(option)
    val result = optionsController.addOption(productId, option)

    verify(optionsService, times(1)).addOption(productId, option)
    assertEquals(option, result)
  }

  @Test
  fun updateOption() {
    val id = "test:id"
    val productId = "product:id"
    val option = Option(id, productId, "option-name", "option-description")

    whenever(optionsService.updateOption(productId, id, option)).thenReturn(option)
    val result = optionsController.updateOption(productId, id, option)

    verify(optionsService, times(1)).updateOption(productId, id, option)
    assertEquals(option, result)
  }

  @Test
  fun updateOption_productDoesNotExist() {
    val id = "test:id"
    val productId = "product:id"
    val option = Option(id, productId, "option-name", "option-description")

    whenever(optionsService.updateOption(productId, id, option)).thenThrow(
      ProductDoesNotExist("Product does not exist")
    )
    assertFailsWith<ProductDoesNotExist> {
      optionsController.updateOption(productId, id, option)
    }
    verify(optionsService, times(1)).updateOption(productId, id, option)
  }

  @Test
  fun updateOption_optionDoesNotExist() {
    val id = "test:id"
    val productId = "product:id"
    val option = Option(id, productId, "option-name", "option-description")

    whenever(optionsService.updateOption(productId, id, option)).thenThrow(
      OptionDoesNotExist("Option does not exist")
    )
    assertFailsWith<OptionDoesNotExist> {
      optionsController.updateOption(productId, id, option)
    }
    verify(optionsService, times(1)).updateOption(productId, id, option)
  }

  @Test
  fun deleteOption() {
    val id = "test:id"
    val productId = "product:id"
    val option = Option(id, productId, "option-name", "option-description")

    whenever(optionsService.deleteOption(productId, id)).thenReturn(option)
    val result = optionsController.deleteOption(productId, id)

    verify(optionsService, times(1)).deleteOption(productId, id)
    assertEquals(option, result)
  }

  @Test
  fun deleteOption_optionDoesNotExist() {
    val id = "test:id"
    val productId = "product:id"

    whenever(optionsService.deleteOption(productId, id)).thenThrow(
      OptionDoesNotExist("Option does not exist")
    )
    assertFailsWith<OptionDoesNotExist> {
      optionsController.deleteOption(productId, id)
    }
    verify(optionsService, times(1)).deleteOption(productId, id)
  }

  @Test
  fun handleException() {
    val errorMessage = "Product does not exist"
    val result = optionsController.handleException(RuntimeException(errorMessage))
    assertEquals(ErrorResponse(errorMessage), result)
  }
}