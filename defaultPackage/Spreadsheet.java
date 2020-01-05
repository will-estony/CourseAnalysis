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
import java.util.HashMap;

public class Spreadsheet{

    /*
    There are currently two export methods this one just exports a set (linear list)
    of xc team names stored as string into a single excel spreadsheet column
    this will become deprecated as I realized it is more useful to include urls 
    in the spreadsheet as well.
    */
    public static void exportTeams(HashSet<String> set){
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook(); 
         
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Teams");

        int rowNum = 1;
        for (String name : set){
            Row row = sheet.createRow(rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(name);
            rowNum++;
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

    /*
    In addition to putting a team name in the first column of the spreadsheet,
    this function will also populate columns B and C with the urls to the men's and 
    women's teams respectively should they exist. This is not hard to do, I just ran
    out of time on account of all the football on today. 

    The goal is to read in this data in two ways, one is just a giant HashSet of urls
    to check if the entered team url is exists

    The other is to read it in as a HashMap with the team name as a key, and the links
    to mens and women's url as a value 


    */
    public static void exportTeams(HashMap<String, HashMap<String, String>> teams){
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook(); 
         
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Teams");

        int rowNum = 1;
        for (String team : teams.keySet()){
            Row row = sheet.createRow(rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(team);
            int cellNum = 1;
            for(String url: teams.get(team).values()){
                cell = row.createCell(cellNum);
                cell.setCellValue(url);
                cellNum++;
            }
            rowNum++;
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

    public static HashMap<String,String>[] importTeams(){
        HashMap<String,String> mens = new HashMap<>();
        HashMap<String,String> womens = new HashMap<>();

        HashMap<String,String>[] both = new HashMap[2];
        both[0] = mens;
        both[1] = womens;

        return both;
}