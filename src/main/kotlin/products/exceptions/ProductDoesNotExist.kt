package products.exceptions

import java.lang.RuntimeException

class ProductDoesNotExist(message: String): RuntimeException(message)