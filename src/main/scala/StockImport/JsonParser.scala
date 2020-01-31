package StockImport
import scala.util.parsing.combinator._
class JsonParser extends JavaTokenParsers{
  def obj: Parser[Map[String,Any]] =
    "{" ~> repsep(member,",") <~ "}" ^^ (Map() ++ _)

  def array : Parser[List[Any]] =
    "[" ~> repsep(value, ",") <~ "]"

  def member : Parser[(String,Any)] =
     stringLiteral ~ ":" ~ value ^^
       {
         case  name ~ ":" ~ value =>
          (name.substring(1,name.length-1),value)
       }

  def value : Parser[Any] = (
    obj
      | array
      | stringLiteral ^^ {str => str.substring(1,str.length-1)}
      | floatingPointNumber ^^ (_.toDouble)
      | "true"
      | "false"
    )

  def parse(str:String): Map[String,Any]=
    {
      val result :Map[String,Any] = parseAll(obj, str).get
      result
    }
}
object JsonParser extends JsonParser{ }
