package com.restassured.ipaddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;



public class RestAssuredExample {

public static FileInputStream fis;
    
	 public static HSSFWorkbook wb;
	 public static HSSFSheet sheet;
	 public static HSSFRow row;
	 public static HSSFCell cell;
	 public static Response resp;
	 public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//fis=new FileInputStream("BlackListIPs.xls");
		
		 FileInputStream fis=new FileInputStream("C:\\Users\\kundan.shrivastwa\\Desktop\\BlackListIPs.xls");
			
    	wb=new HSSFWorkbook(fis);
    	sheet=wb.getSheet("Sheet1");
    	    	
	    
     RestAssured.baseURI="https://api.abuseipdb.com";
	     	 
	
		 resp= RestAssured.given().
	             param("countMinimum",15).
                 param("maxAgeInDays",60).
                 param("confidenceMinimum",90).
                 header("Key","ec326d894da6c84e8a6cfc9384635d89a4495f01f357a0f6704f565ba81440619017b9202a130b34").
                 header("Accept","application/json").
                 when().get("/api/v2/blacklist").then().assertThat().statusCode(200).extract().response();
	
		 
                    // System.out.println(resp.asString());
                
                    JsonPath js=new JsonPath(resp.asString());
                     
                    int count=js.get("data.size()");
                    System.out.println(count);
                    HSSFRow header=sheet.createRow(0);
                    HSSFCellStyle style = wb.createCellStyle();
                    HSSFFont font = wb.createFont();
                    font.setFontName(HSSFFont.FONT_ARIAL);
                    font.setFontHeightInPoints((short)10);
                    font.setBold(true);
                    style.setFont(font);
                    header.createCell(0).setCellValue("IP Address");
                    header.createCell(1).setCellValue("Total Reports");
                    header.createCell(2).setCellValue("Confidence Score");
                    for(int j=0;j<3;j++)
                    {
                    	header.getCell(j).setCellStyle(style);
                    }
                    
                    
                    for(int i=1;i<count;i++)
                    {
                    	row=sheet.createRow(i);      				
            			
            			
            			for(int j=0;j<3;j++)
            				{
            					  try {         				
            							
            				if(j==0)
            					{   
            						cell=row.createCell(j);
            						String str0=js.get("data["+(i-1)+"].ipAddress").toString();
            					 	cell.setCellValue(str0);
            					
            					}
            					
            				else if(j==1)
            					{
            					cell=row.createCell(j);
            					String str0=js.get("data["+(i-1)+"].totalReports").toString();
            					cell.setCellValue(str0);
            					}
            					
            				else if(j==2)
            					{
            						cell=row.createCell(j);
            						String str0=js.get("data["+(i-1)+"].abuseConfidenceScore").toString();
            						cell.setCellValue(str0);
            					}
            					
            				}
            				
            			catch(NullPointerException e){
            				            				 
        						cell=row.createCell(j);
        						String str0="N/A";
        					 	cell.setCellValue(str0);     					
       				          				            				            				
            			}
            			
                    	
                    	
                    }                 
            			System.out.println(i+" set of data");
                                  
	              
                    }
                    
                    FileOutputStream fos=new FileOutputStream("C:\\Users\\kundan.shrivastwa\\Desktop\\BlackListIPs.xls");
        			wb.write(fos); 
		

	}

}
