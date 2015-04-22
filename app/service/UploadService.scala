package  service

import java.io.File

import com.google.inject.Inject
import util.ExcelReadUtil

class UploadService @Inject() (excelReadUtil: ExcelReadUtil){

  def readExcel(fileName: String) = {
    excelReadUtil.getIds(fileName)
  }
}
