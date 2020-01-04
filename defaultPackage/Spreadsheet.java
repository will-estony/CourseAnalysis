package defaultPackage;

//import statements
import org.apache.poi.hssf.usermodel.*; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream; 
import java.io.FileInputStream;  
import java.awt.Desktop; 
import java.io.File; 
import java.io.IOException; 
import java.util.HashSet;

public class Spreadsheet{
    public Spreadsheet(HashSet<String> set){
        exportToSpreadSheet(set);
    }
    
    public void exportToSpreadSheet(HashSet<String> set){
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook(); 
         
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Teams");

        int rownum = 0;
        for (String name : set){
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(0);
            cell.setCellValue(name);
        }
        try{
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("XCTeams.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("XCTeams.xlsx written successfully on disk.");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}