package products.exceptions

import java.lang.RuntimeException

class ProductExists(message: String) : RuntimeException(message)