package StockImport
import scalaj.http.Http
import java.sql.{Connection, DriverManager, ResultSet}
import scala.collection.immutable.HashMap

object DatabaseStrategy{

  classOf[org.postgresql.Driver]
  val con_str = "jdbc:postgresql://localhost:5432/DB_NAME?user=DB_USER"
  val conn = DriverManager.getConnection(con_str)

  def InsertQuery(qry:String,seq:Seq[Any]) ={

    try {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
      val rs = stm.executeQuery(qry)
    }
    finally {
      conn.commit
      conn.close
    }
  }
  def SelectQuery(qry:String):Any={
    try {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      val rs = stm.executeQuery(qry)
      conn.close()
      rs
    }
  }
}

object Main extends App{
  var resp = HttpRequest.Get("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=full&apikey=O6AA0MHLJIH01UAA")
  val result = JsonParser.parse(resp)

  //println(result)
  result("Meta Data") match{
    case m:HashMap[String,Any]=>{
      val symbols = "MSFT,AAPL"
      val t:String  = m("2. Symbol").asInstanceOf[String]
      t match{
        case _ if symbols.contains(t)=> println("Hello")
        case _=> println(t)
      }
    }
  }
  var r:HashMap[String,HashMap[String,Double]]= result("Time Series (Daily)").asInstanceOf[HashMap[String,HashMap[String,Double]]]
  r.keys.foreach(k=>{
    val s:String="SELECT * FROM "
    k match{
      case x if s.contains(k)=>
        println("contained")
      case _=>
        DatabaseStrategy.InsertQuery("")
    }



  })

}
