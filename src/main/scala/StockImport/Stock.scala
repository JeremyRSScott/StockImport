package StockImport

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.concurrent.Future
import scala.io.Source

class Stock(val StockCode:String){
  val StockDays:List[StockDay] = LoadStockDays()


  def LoadStockDays(): List[StockDay] ={
    if(!FileImporter.FileExists(StockCode)){
      FileImporter.MakeFile(StockCode)
    }

    UpdateData()
  }

  // def GetLastUpdatedDate(): LocalDate ={
  //  LocalDate
  //}

  def LoadData():List[StockDay]={
    val src = Source.fromFile(FileImporter.GetRawFilePath(StockCode))
    val fileContents:String = src.getLines().mkString("\n")
    val stockdays = CreateSortableList(fileContents.split("\n").toList).sortBy(_.Date)
    stockdays
  }


  def CreateSortableList(days:List[String]):List[StockDay]={
    val stckDayList = List()
    @tailrec def mkList(days:List[String],sortedDays:List[StockDay]):List[StockDay]={
      days match{
        case h::t=>
          val dayValues = h.split(",")
          mkList(t, sortedDays ++ List(new StockDay(LocalDate.parse(dayValues(0)), dayValues(1).toDouble, dayValues(2).toDouble, dayValues(3).toDouble, dayValues(4).toDouble, dayValues(5).toLong)))
        case Nil => sortedDays
      }
    }
    mkList(days,stckDayList).sortBy(_.Date)(Ordering[LocalDate].reverse)
    //list.sortBy(_.Date)
  }

  def ConvertDaysToSortedString(stockdays:List[StockDay]): String ={
    stockdays match{
      case h::t=> h.ToCsvString + ConvertDaysToSortedString(t)
      case Nil=>""
    }
  }

  private def UpdateData():List[StockDay]={
    @tailrec def BuildCsvString(fileContents:String, keys:List[String], m:HashMap[String,HashMap[String,Double]]):String={
      keys match{
        case h::t=>
          if(fileContents contains h){
            BuildCsvString(fileContents,t, m)
          }else{
            val dayValues:HashMap[String,Double] = m(h):HashMap[String, Double]
            val new_string = fileContents+"\n"+h+","+dayValues.values.toList.mkString(",")
            BuildCsvString(new_string, t,m)
          }
        case Nil=>fileContents
      }
    }

    val daydata:HashMap[String,HashMap[String,Double]] = AlphaVantageApi.GetData(StockCode)
    val src = Source.fromFile(FileImporter.GetRawFilePath(StockCode))
    val fileContents:String = src.getLines().mkString("\n")
    src.close()
    val fileString = fileContents match{
      case "\n"=> ""
      case _ => fileContents
    }

    val keys:List[String] = daydata.keySet.toList
    val newFileString:String = BuildCsvString(fileString,keys,daydata)

    new Thread {
      override def run(): Unit = {
        FileImporter.UpdateRaw(StockCode,newFileString)
      }
    }.start()

    val stockdays = CreateSortableList(newFileString.split("\n").toList)
    val sorted_data = ConvertDaysToSortedString(stockdays)
    new Thread {
      override def run(): Unit = {
        FileImporter.UpdateProcessed(StockCode,sorted_data)
      }
    }.start()
    stockdays
  }
}

