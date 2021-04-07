package products.services

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import products.exceptions.OptionDoesNotExist
import products.exceptions.ProductDoesNotExist
import products.controller.model.Option as ControllerOption
import products.repository.OptionsRepository
import products.repository.ProductsRepository
import products.repository.model.Option
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class OptionsServiceTests {

  private val optionsRepository = mock<OptionsRepository>()
  private val productsRepository = mock<ProductsRepository>()
  private val optionsService = OptionsService(optionsRepository, productsRepository)

  @Test
  fun addOption() {
    val productId = "product:id"
    val controllerOption =
      ControllerOption(null, null, "option:name", "option:description")

    whenever(productsRepository.containsProduct(productId)).thenReturn(true)
    whenever(optionsRepository.addOrUpdateOption(any())).thenAnswer { it.arguments[0] }
    val option = optionsService.addOption(productId, controllerOption)

    assertEquals(option.name, controllerOption.name)
    assertEquals(option.description, controllerOption.description)
    assertEquals(option.productId, productId)
    assertNotNull(option.id)
  }

  @Test
  fun addOption_ProductNotPresent() {
    val productId = "product:id"
    val controllerOption =
      ControllerOption(null, null, "option:name", "option:description")

    whenever(productsRepository.containsProduct(productId)).thenReturn(false)
    assertFailsWith<ProductDoesNotExist> {
      optionsService.addOption(productId, controllerOption)
    }
  }

  @Test
  fun updateOption() {
    val productId = "product:id"
    val optionId = "option:id"
    val controllerOption =
      ControllerOption(null, null, "option:name", "option:description")

    whenever(productsRepository.containsProduct(productId)).thenReturn(true)
    whenever(optionsRepository.containsOption(productId, optionId)).thenReturn(true)
    whenever(optionsRepository.addOrUpdateOption(any())).thenAnswer { it.arguments[0] }
    val option = optionsService.updateOption(productId, optionId, controllerOption)

    assertEquals(option.name, controllerOption.name)
    assertEquals(option.description, controllerOption.description)
    assertEquals(option.productId, productId)
    assertEquals(option.id, optionId)
  }

  @Test
  fun updateOption_ProductNotPresent() {
    val productId = "product:id"
    val optionId = "option:id"
    val controllerOption =
      ControllerOption(null, null, "option:name", "option:description")

    whenever(productsRepository.containsProduct(productId)).thenReturn(false)
    assertFailsWith<ProductDoesNotExist> {
      optionsService.updateOption(productId, optionId, controllerOption)
    }
  }

  @Test
  fun updateOption_OptionNotPresent() {
    val productId = "product:id"
    val optionId = "option:id"
    val controllerOption =
      ControllerOption(null, null, "option:name", "option:description")

    whenever(productsRepository.containsProduct(productId)).thenReturn(true)
    whenever(optionsRepository.containsOption(productId, optionId)).thenReturn(false)
    assertFailsWith<OptionDoesNotExist> {
      optionsService.updateOption(productId, optionId, controllerOption)
    }
  }

  @Test
  fun deleteOption() {
    val productId = "product:id"
    val optionId = "option:id"
    val option = Option(optionId, productId, "option:Name", "option:Description")
    val controllerOption = ControllerOption(optionId, productId, "option:Name", "option:Description")
    whenever(optionsRepository.deleteOption(productId, optionId)).thenReturn(option)

    val result = optionsService.deleteOption(productId, optionId)

    assertEquals(controllerOption, result)
  }

  @Test
  fun deleteOption_NotPresent() {
    val productId = "product:id"
    val optionId = "option:id"

    whenever(optionsRepository.deleteOption(productId, optionId)).thenThrow(
      RuntimeException("option not present"))

    assertFailsWith<OptionDoesNotExist> {
      optionsService.deleteOption(productId, optionId)
    }
  }

  @Test
  fun getAllOptionsForProduct() {
    val productId = "product:id"
    val option = Option("option:id", productId, "option:Name", "option:Description")
    val controllerOption = ControllerOption("option:id", productId, "option:Name", "option:Description")
    whenever(optionsRepository.getOptionsForProduct(productId)).thenReturn(
      listOf(option)
    )
    val result = optionsService.getAllOptionsForProduct(productId)
    assertEquals(listOf(controllerOption), result)
  }
}