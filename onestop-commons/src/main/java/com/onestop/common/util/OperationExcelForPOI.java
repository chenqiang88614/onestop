package com.onestop.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;

public class OperationExcelForPOI {
    public static void main(String[] args) {  
    	
//    	int [][] aaa = new int[3][];
//    	System.out.println(aaa.length);
//    	for(int i=0; i < 3; i++){
//    		aaa[i] = new int[2];
//    		for(int j=0; j < 2; j++) {
//    			aaa[i][j] = i*j;
//    			System.out.println(aaa[i][j]);
//    		}
//    	}
    	String aa = "06-09-1988";
//    	BigDecimal bd = new BigDecimal(aa);
    	Date d = new Date(aa);
    	System.out.println(d);
        // 文件所在路径  
//        String execelFile = "E:/satellite/China.xlsx" ;  
        //String execelFile = "C:/Book2003.xls" ;  
        // 导入Excel  
//        new OperationExcelForPOI().impExcel(execelFile) ;  
        // 导出Excel  
//        String expFilePath = "C:/testBook.xls" ;  
//        new OperationExcelForPOI().expExcel(expFilePath); 
    }  
      
    /** 
     * 导入Excel 
     * @param execelFile 
     */  
    @SuppressWarnings("resource")
	public String[][] impExcel(String execelFile, Integer sheetIndex){
    	String [][] data = null;
    	try {  
            // 构造 Workbook 对象，execelFile 是传入文件路径(获得Excel工作区)  
            Workbook book = null;  
            try {  
                // Excel 2007获取方法  
                book = new XSSFWorkbook(new FileInputStream(execelFile));  
            } catch (Exception ex) {  
                // Excel 2003获取方法  
                book = new HSSFWorkbook(new FileInputStream(execelFile));  
            }  
              
            // 读取表格的第一个sheet页
            if (sheetIndex == null) {
                sheetIndex = 0;
            }
            Sheet sheet = book.getSheetAt(sheetIndex);
            // 定义 row、cell  
            Row row;  
            String cell;  
            // 总共有多少行,从0开始  
            int totalRows = sheet.getLastRowNum() ;  
            data = new String[totalRows+1][];
            // 循环输出表格中的内容,首先循环取出行,再根据行循环取出列  
            for (int i = 0; i <= totalRows; i++) {

                row = sheet.getRow(i);  
                // 处理空行  
                if(row == null){  
                    continue ;  
                }  
                // 总共有多少列,从0开始  
                int totalCells = row.getLastCellNum() ;  
                data[i] = new String[totalCells];
                if (i < 2) {
                    continue;
                }
                for (int j = row.getFirstCellNum(); j < totalCells; j++) {  
                    // 处理空列  
//                	 data[i][j] = new String[totalCells];
                    if(row.getCell(j) == null){  
                        continue ;  
                    }  
                    
                    // 通过 row.getCell(j).toString() 获取单元格内容
                    try {
//                    	if(j==14) {
//                        	DecimalFormat df = new DecimalFormat("0.00");
//                        	data[i][j] = df.format(row.getCell(j).getNumericCellValue());
//                        } else if(j == 15) {
//                        	DecimalFormat df = new DecimalFormat("0.0000000");
//                        	data[i][j] = df.format(row.getCell(j).getNumericCellValue());
//                        } else{
                        	data[i][j] = row.getCell(j).toString(); 
//                        }
					} catch (Exception e) {
						// TODO: handle exception
						data[i][j] = row.getCell(j).toString(); 
					}
                }
            }
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return data;
    }  
      
    public void expExcel(String expFilePath){  
        OutputStream os = null ;  
        Workbook book = null;  
        try {  
            // 输出流  
            os = new FileOutputStream(expFilePath);  
            // 创建工作区(97-2003)  
            book = new HSSFWorkbook();  
            // 创建第一个sheet页  
            Sheet sheet= book.createSheet("test");  
            // 生成第一行  
            Row row = sheet.createRow(0);  
            // 给第一行的第一列赋值  
            row.createCell(0).setCellValue("column1");  
            // 给第一行的第二列赋值  
            row.createCell(1).setCellValue("column2");  
            // 写文件  
            book.write(os);  
              
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭输出流  
            try {  
                os.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }      
    }  
}
