package StockImport
import java.io.{File, PrintWriter}

import scalaj.http.Http
import java.sql.{Connection, DriverManager, ResultSet}

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.io.Source



object Main extends App{
  var resp = HttpRequest.Get("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=full&apikey=O6AA0MHLJIH01UAA")
  val result = JsonParser.parse(resp)
  val filepath:String = "D:\\Projects\\test\\DATA.csv"

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
  val src = Source.fromFile(filepath)
  val fileContents:String = src.getLines().mkString("\n")
  var days:HashMap[String,HashMap[String,Double]]= result("Time Series (Daily)").asInstanceOf[HashMap[String,HashMap[String,Double]]]
  val keys:List[String] = days.keySet.toList
  src.close()

  @tailrec def BuildString(fileContents:String, keys:List[String], m:HashMap[String,HashMap[String,Double]]):String={
    keys match{
      case h::t=>
        h match{
          case f if fileContents.contains(h) =>
            BuildString(fileContents,t, m)
          case _=>
            val dayValues:HashMap[String,Double] = m(h):HashMap[String, Double]
            val new_string = fileContents+"\n"+h+","+dayValues.values.toList.mkString(",")
            BuildString(new_string, t,m)

        }
      case Nil=>fileContents
    }
  }
  val newFileString:String = BuildString(fileContents,keys,days)
  val file:File = new File(filepath)
  val printWriter:PrintWriter = new PrintWriter(file)
  printWriter.write(newFileString)
  printWriter.close()
}
