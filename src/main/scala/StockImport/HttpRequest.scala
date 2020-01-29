package StockImport
import java.io.InputStream
object HttpRequest {

  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def Get(url: String, connectTimeout: Int = 5000, readTimeout: Int = 5000,
          requestMethod: String = "GET") : String =
  {
    import java.net.{HttpURLConnection, URL}
    var connection : HttpURLConnection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    val inputStream : InputStream  = connection.getInputStream
    val content: String = io.Source.fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close
    content
  }
}
