package products.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import products.exceptions.ProductDoesNotExist
import products.repository.model.Option
import java.lang.NullPointerException
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OptionRepositoryTests {
  private val optionsRepository = OptionsRepository()

  @BeforeEach
  fun beforeEach() {
    optionsRepository.deleteAll()
  }

  @Test
  fun addOrUpdateOption() {
    val option = Option("option:id", "product:id", "option:name", "option:description")
    val result = optionsRepository.addOrUpdateOption(option)

    assertEquals(option, result)
    assertEquals(listOf(option), optionsRepository.getOptionsForProduct("product:id"))
  }

  @Test
  fun addOrUpdateOption_multipleOptions() {
    val productId = "product:id"

    val option1 = Option("option:id1", productId, "option:name1", "option:description1")
    val result1 = optionsRepository.addOrUpdateOption(option1)
    assertEquals(option1, result1)


    val option2 = Option("option:id2", productId, "option:name2", "option:description2")
    val result2 = optionsRepository.addOrUpdateOption(option2)
    assertEquals(option2, result2)
    assertEquals(listOf(option2, option1), optionsRepository.getOptionsForProduct("product:id"))
  }

  @Test
  fun getOption() {
    val option = Option("option:id", "product:id", "option:name", "option:description")
    optionsRepository.addOrUpdateOption(option)

    assertEquals(option, optionsRepository.getOption("product:id", "option:id"))
  }

  @Test
  fun getOption_throwsExceptionIfNotPresent() {
    assertFailsWith<NullPointerException> {
      optionsRepository.getOption("product:id", "option:id")
    }
  }

  @Test
  fun containsOption() {
    val option = Option("option:id", "product:id", "option:name", "option:description")
    optionsRepository.addOrUpdateOption(option)

    assertTrue(optionsRepository.containsOption("product:id", "option:id"))
  }

  @Test
  fun containsOption_False() {
    assertFalse(optionsRepository.containsOption("product:id", "option:id"))
  }

  @Test
  fun deleteOption() {
    val option = Option("option:id", "product:id", "option:name", "option:description")
    optionsRepository.addOrUpdateOption(option)

    assertTrue(optionsRepository.containsOption("product:id", "option:id"))
    optionsRepository.deleteOption("product:id", "option:id")

    assertFalse(optionsRepository.containsOption("product:id", "option:id"))
  }
}