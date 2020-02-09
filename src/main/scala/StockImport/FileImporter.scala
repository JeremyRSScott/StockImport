package StockImport

import java.io.{File, PrintWriter}

object FileImporter {

  def MakeFile(code:String): Unit ={
    def mkFile(filepath:String):Unit={
      if(!new java.io.File(filepath).exists){
        new java.io.File(filepath).createNewFile
        val file:File = new File(filepath)
        val printWriter:PrintWriter = new PrintWriter(file)
        printWriter.close()
      }
    }

    mkFile(GetProcessedFilePath(code))
    mkFile(GetRawFilePath(code))
  }

  def UpdateRaw(code:String, content:String): Unit ={
    UpdateFile(GetRawFilePath(code),content)
  }

  def UpdateProcessed(code:String, content:String): Unit ={
    UpdateFile(GetProcessedFilePath(code),content)
  }

  private def UpdateFile(filepath:String, content:String): Unit ={
    val file:File = new File(filepath)
    val printWriter:PrintWriter = new PrintWriter(file)
    printWriter.write(content)
    printWriter.close()
  }

  def GetRawFilePath(code:String): String ={
    "D:\\Projects\\test\\RAW\\"+code+".csv"
  }

  def GetProcessedFilePath(code:String):String = {
    "D:\\Projects\\test\\PROCESSED\\"+code+".csv"
  }

  def FileExists(code:String): Boolean ={
    new java.io.File(GetProcessedFilePath(code)).exists && new java.io.File(GetRawFilePath(code)).exists
  }

  def LoadStockDays(code:String): List[StockDay]={
    List()
  }
}
