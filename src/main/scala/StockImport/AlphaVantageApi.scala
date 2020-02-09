package StockImport

import scala.collection.immutable.HashMap

object AlphaVantageApi {
  def GetData(code:String): HashMap[String,HashMap[String,Double]] ={
    val resp = HttpRequest.Get("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+code+"&outputsize=full&apikey=O6AA0MHLJIH01UAA")
    val result = JsonParser.parse(resp)
    result("Time Series (Daily)").asInstanceOf[HashMap[String,HashMap[String,Double]]]
  }

}
