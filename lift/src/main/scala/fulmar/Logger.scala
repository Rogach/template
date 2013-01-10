package fulmar

trait Logger {
  def log(message: String): Unit =
    println("L# [%tT] %s" format (new java.util.Date, message))

  def log(message: String, args: Any*): Unit = log(message format (args:_*))
}
