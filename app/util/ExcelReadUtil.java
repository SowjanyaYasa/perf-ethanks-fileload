package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.*;


public class ExcelReadUtil {


   public List<String> getIds(String fileName)
   {
    System.out.println(" I am in ExcelReadUtil");
    InputStream inp = null;
    try {
        inp = new FileInputStream("/Users/sowjanya.yasa/uploadedFiles/"+fileName);

        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);
        Header header = sheet.getHeader();

        int rowsCount = sheet.getLastRowNum();
        System.out.println("Total Number of Rows: " + (rowsCount + 1));
        for (int i = 0; i <= rowsCount; i++) {
            Row row = sheet.getRow(i);
            int colCounts = row.getLastCellNum();
            System.out.println("Total Number of Cols: " + colCounts);
            for (int j = 0; j < colCounts; j++) {
                Cell cell = row.getCell(j);
                System.out.println("[" + i + "," + j + "]=" + cell.getStringCellValue());
            }
        }

    } catch (Exception ex) {
        //java.util.logging.Logger.getLogger(FieldController.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            inp.close();
        } catch (IOException ex) {
            //java.util.logging.Logger.getLogger(FieldController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       return null;
   }

}
