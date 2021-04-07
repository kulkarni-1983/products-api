package products.exceptions

import java.lang.RuntimeException

class OptionDoesNotExist(message: String) : RuntimeException(message)