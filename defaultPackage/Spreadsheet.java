package defaultPackage;

//import statements
import org.apache.poi.hssf.usermodel.*; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy; 
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream; 
import java.io.FileInputStream;  
import java.awt.Desktop; 
import java.io.File; 
import java.io.IOException; 
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class Spreadsheet{


    private final static int teamNameIndex = 0;
    private final static int womensUrlIndex = 1;
    private final static int mensUrlIndex = 2;
    
    private final static String fileName = "XCTeams.xlsx";

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
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            System.out.println(fileName + " written successfully on disk.");
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
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            System.out.println(fileName + " written successfully on disk.");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<HashMap<String,String>> importTeams(){
        HashMap<String,String> mens = new HashMap<>();
        HashMap<String,String> womens = new HashMap<>();
        ArrayList<HashMap<String,String>> both = new ArrayList<>();
        System.out.println(System.getProperty("user.dir") + "/" + fileName);

        try{
            FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir") + "/" + fileName);
            
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            
            //this should only run once since there is only one sheet
            for(int i = 0; i < workbook.getNumberOfSheets(); i++){
                
                Sheet sheet = workbook.getSheetAt(i);
                
                //loop through each row
                for(int j = 0; j < sheet.getPhysicalNumberOfRows(); j++){
                    Row row = sheet.getRow(j);

                    //check that the row isn't null
                    if(row != null){

                        //Create each cell, if the cell is null, it makes the cell blank
                        //The only Cell that should have blanks is mensUrlCell
                        Cell teamCell = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Cell womensUrlCell = row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Cell mensUrlCell = row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);

                        //Conver the team name to a string
                        if(teamCell.getCellType() != CellType.BLANK){
                            String team = teamCell.toString();

                            //Convert the women's url to a string
                            if(womensUrlCell.getCellType() != CellType.BLANK){
                                String womensUrl = womensUrlCell.toString();
                                womens.put(team, womensUrl);
                            }

                            //Convert the mens url to a string
                            if(mensUrlCell.getCellType() != CellType.BLANK){
                                String mensUrl = mensUrlCell.toString();
                                mens.put(team, mensUrl);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Can't find the excel file: " + fileName);
            e.printStackTrace();
        }

        both.add(mens);
        both.add(womens);

        return both;
    }

    public static void printSpreadSheet(ArrayList<HashMap<String, String>> list){

        HashMap<String, String> mens = list.get(0);
        HashMap<String, String> womens = list.get(1);

        
        System.out.println("Printing all the men's teams and their urls...");
        for(String team: mens.keySet()){
            System.out.println(team + ": " + mens.get(team));
        }

        System.out.println("Printing all the women's teams and their urls...");
        for(String team: womens.keySet()){
            System.out.println(team + ": " + womens.get(team));
        }

    }


}