package StockImport
import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat
import java.util.Date

import scala.annotation.tailrec
import scala.collection.immutable.{ArraySeq, HashMap}
import scala.io.Source
import java.time.{ LocalDate, ZoneId, ZonedDateTime }



object Main extends App{

  val s = new Stock("TLS.AX")
  val twentye = (s.CalculateEma(50))
  val fiftye = (s.CalculateEma(200))

  def CompareEma(ema1:List[Double], ema2:List[Double]):Unit={
    val safe_small_ema = ema1.takeRight(ema2.length)
    val is_bigger:Boolean = (safe_small_ema.head<ema2.head)
    def recurseCompare(e1:List[Double], e2:List[Double],e2_higher:Boolean):Unit={
      e1 match {
        case h::t=>{
          val e2_h = e2.head
          if(h>e2_h){
            if(e2_higher){
              println("Buy")
            }else{
              println("Hold")
            }
            recurseCompare(t,e2.takeRight(e2.length-1),false)
          }
          else{
            if(e2_higher){
              println("Stay Out")
            }else{
              println("Sell")
            }
            recurseCompare(t,e2.takeRight(e2.length-1),true)
          }
        }
        case Nil=> println("End")
      }
    }
    recurseCompare(safe_small_ema,ema2,is_bigger)

  }

  CompareEma(twentye,fiftye)






  //sort new csv string into sortable class objects





}
