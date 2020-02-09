package StockImport
import java.time.LocalDate

class StockDay(val Date:LocalDate, val Open: Double, val High: Double, val Low: Double, val Close: Double, val Volume:Long){
  def ToCsvString():String={
    Date.toString + ","+Open+","+High+","+Low+","+Close+","+Volume+"\n"
  }
}