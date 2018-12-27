package com.feihua.utils.office;
/** 
 * 作者: feihua
 * 时间: 2013年12月23日 11:19:51
 * 功能:Excel操作工具类，POI实现，兼容Excel2003或以下版本，及Excel2007或以上版本
 **/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelSSUtils {
	/**
	 * 文档类型
	 * 
	 * @author feihua
	 * 
	 */
	public enum DocType {
		// excel 2003及以下版本
		EXCEL_2003_LOW("2003",".xls"),
		// excel 2007及以上版本
		EXCEL_2007_UP("2007",".xlsx");
		private String version;

		private String extension;
		DocType(String version,String extension) {
			this.version = version;
			this.extension = extension;
		}

		public String getVersion() {
			return version;
		}

		public String getExtension() {
			return extension;
		}

		
	}

	/**
	 * 创建空工作簿
	 * @param docType
	 * @return
	 */
	public static Workbook createWorkbook(DocType docType) {
		Workbook workbook = null;
		if(DocType.EXCEL_2003_LOW.equals(docType)){
			return new HSSFWorkbook();
		}else if(DocType.EXCEL_2007_UP.equals(docType)){
			return new XSSFWorkbook();
		}
		return workbook;
	}

	/**
	 * 创建工作簿
	 * @param file 文件
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws InvalidFormatException 
	 */
	public static Workbook createWorkbook(File file) throws  IOException, InvalidFormatException{
		Workbook workbook = null;
		InputStream in = new FileInputStream(file);
		workbook = createWorkbook(in);
		return workbook;
	}
	/**
	 * 创建工作簿
	 * @param in 文件输入流
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws InvalidFormatException 
	 */
	public static Workbook createWorkbook(InputStream in) throws  IOException, InvalidFormatException{
		Workbook workbook = null;
		workbook = WorkbookFactory.create(in);
		return workbook;
	}
	/**
	 * 创建工作簿
	 * @param filePath 文件路径
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws InvalidFormatException 
	 */
	public static Workbook createWorkbook(String filePath) throws  IOException, InvalidFormatException{
		Workbook workbook = null;
		File file = new File(filePath);
		workbook = createWorkbook(file);
		return workbook;
	}
	/**
	 * 创建带有名称的sheet
	 * @param workbook 工作簿
	 * @param sheetName 名称
	 * @return
	 */
	public static Sheet createSheet(Workbook workbook,String sheetName){
		Sheet sheet = null;
		sheet = workbook.createSheet(sheetName);
		return sheet;
	}
	/**
	 * 创建一个默认名称的sheet
	 * @param workbook
	 * @return
	 */
	public static Sheet createSheet(Workbook workbook){
		return workbook.createSheet();
	}
	/**
	 * 根据索引获取sheet
	 * @param workbook
	 * @param index
	 * @return
	 */
	public static Sheet getSheet(Workbook workbook,int index){
		return workbook.getSheetAt(index);
	}
	/**
	 * 窗口冻结
	 * @param sheet
	 * @param frozenColumnNum 冻结列的数目
	 * @param forzenRowNum 冻结行的数目
	 * @param frozenCloumnStartNum 冻结列起始位置数
	 * @param frozenRowStartNum 冻结行起始位置数
	 */
	public static void FreezePane(Sheet sheet,int frozenColumnNum,int forzenRowNum,int frozenCloumnStartNum,int frozenRowStartNum){
		sheet.createFreezePane(frozenColumnNum, forzenRowNum, frozenCloumnStartNum, frozenRowStartNum);
	}
	/**
	 * 设置列宽
	 * @param sheet
	 * @param columnNum 列的索引
	 * @param wordNum 列宽的字符数
	 */
	public static void setColumnWidth(Sheet sheet,int columnNum,int wordNum){
		sheet.setColumnWidth(columnNum, wordNum*256);
	}
	/**
	 * 设置单元格对齐方式
	 * @param workbook
	 * @param cell 单元格
	 * @param horizontalAlignment 对齐方式 （CellStyle.center等使用）
	 */
	public static void setAlignment(Workbook workbook,Cell cell,HorizontalAlignment horizontalAlignment){
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(horizontalAlignment);
		cell.setCellStyle(cellStyle);
	}
	/**
	 * 在指定单元格位置插入数据
	 * @param sheet sheet对象
	 * @param rowNum 行从0开始
	 * @param cellNum 列从0开始
	 * @param value 插入的数据
	 * @return
	 */
	public static Cell createOrUpdateCell(Sheet sheet, int rowNum, int cellNum, String value){
		Cell cell = null;
		Row row = getRow(sheet,rowNum);
		if(row == null){
			row = createRow(sheet,rowNum);
		}
		cell = getCell(row,cellNum);
		if(cell == null){
			cell = createCell(row,cellNum);
		}
		cell.setCellType(CellType.STRING);
		cell.setCellValue(value);
		return cell;
	}
	public static Object getCellValue(Sheet sheet,int rowNum,int cellNum){
		Cell cell = getCell(getRow(sheet,rowNum),cellNum);
		Object value = null;
		//如果是字符串类型
		try{
			value = cell.getStringCellValue();
			if(value!=null)return value;
		}catch(Exception e){
			
		}
		//如果是富字符文本类型RichTextString
		try{
			value = cell.getRichStringCellValue();
			if(value!=null)return value;
		}catch(Exception e){
			
		}
		//如果是double类型
		try{
			value = cell.getNumericCellValue();
			if(value!=null)return value;
		}catch(Exception e){
			
		}
		//如果是日期类型date
		try{
			value = cell.getDateCellValue();
			if(value!=null)return value;
		}catch(Exception e){
			
		}
		return value;
	}
	/**
	 * 取得指定行的sheet行
	 * @param sheet
	 * @param rowNum 行数从0开始
	 * @return
	 */
	public static Row getRow(Sheet sheet, int rowNum){
		Row row = null;
		row = sheet.getRow(rowNum);
		return row;
	}
	/**
	 * 在指定行创建row
	 * @param sheet sheet
	 * @param rowNum 行数从0开始
	 * @return
	 */
	public static Row createRow(Sheet sheet, int rowNum){
		return sheet.createRow(rowNum);
	}
	/**
	 * 获取行中指定列的单元格
	 * @param row row对象
	 * @param cellNum 列从0开始
	 * @return
	 */
	public static Cell getCell(Row row,int cellNum){
		return row.getCell(cellNum);
	}
	/**
	 * 在行中创建指定列的单元格
	 * @param row row对象
	 * @param cellNum 列从0开始
	 * @return
	 */
	public static Cell createCell(Row row,int cellNum){
		return row.createCell(cellNum);
	}
    /**
     * 将workbook转化为输入流,方便自己输出文档
     * @param workbook
     * @return
     * @throws IOException
     */
    public static ByteArrayInputStream getInputStream(Workbook workbook) throws IOException{
    	ByteArrayOutputStream os = new ByteArrayOutputStream();  
    	workbook.write(os);  
    	ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray()); 
    	return is;
    }
    /**
     * 将给定的文件名包装成excel对应版本的扩展名
     * @param fileName 文件名，可以不带扩展名，自动封装扩展名
     * @return
     */
    public static String getExtension(Workbook workbook,String fileName){
    	
    	String realFileName = fileName;
    	//去掉扩展名，以在后面根据类型添加
    	if(null == fileName || "".equals(fileName.trim())){
    		realFileName = "";
    	}else if(realFileName.endsWith(DocType.EXCEL_2003_LOW.getExtension())){
    		realFileName = realFileName.substring(0,realFileName.lastIndexOf(DocType.EXCEL_2003_LOW.getExtension()));
    	}else if(realFileName.endsWith(DocType.EXCEL_2007_UP.getExtension())){
    		realFileName = realFileName.substring(0,realFileName.lastIndexOf(DocType.EXCEL_2007_UP.getExtension()));
    	}
    	DocType type = getExtension(workbook);
    	//添加扩展名
    	if(type != null){
    		realFileName += type.getExtension();
    	}
    	//如果再不行，返回原文件名
    	else{
    		realFileName = fileName;
    	}
    	
    	return realFileName;
    }
    /**
     * 获取文档有扩展名
     * @param workbook
     * @return
     */
    public static DocType getExtension(Workbook workbook){
    	DocType type = null;
    	//2003及以下版本
    	if(workbook instanceof HSSFWorkbook){
    		type = DocType.EXCEL_2003_LOW;
    	}
    	//2007及以上版本
    	else if(workbook instanceof XSSFWorkbook){
    		type = DocType.EXCEL_2007_UP;
    	}
    	return type;
    }
    /**
     * 复制sheet
     * @param sheetFrom 被复制的sheet
     * @param sheetTo 复制的目的sheet
     * @return
     */
	public static Sheet copySheet(Sheet sheetFrom, Sheet sheetTo) {

		CellRangeAddress region = null;
		Row rowFrom = null;
		Row rowTo = null;
		Cell cellFrom = null;
		Cell cellTo = null;
		for (int i = 0; i < sheetFrom.getNumMergedRegions(); i++) {
			region = sheetFrom.getMergedRegion(i);
			if ((region.getFirstColumn() >= sheetFrom.getFirstRowNum())
			&& (region.getLastRow() <= sheetFrom.getLastRowNum())) {
				sheetTo.addMergedRegion(region);
			}
		}
		for (int intRow = sheetFrom.getFirstRowNum(); intRow <= sheetFrom
				.getLastRowNum(); intRow++) {

			rowFrom = sheetFrom.getRow(intRow);
			rowTo = sheetTo.createRow(intRow);
			if (null == rowFrom)
				continue;
			rowTo.setHeight(rowFrom.getHeight());
			for (int intCol = 0; intCol < rowFrom.getLastCellNum(); intCol++) {
				if (null != sheetFrom.getColumnStyle(intCol))
					sheetTo.setDefaultColumnStyle(intCol, sheetFrom.getColumnStyle(intCol));
					sheetTo.setColumnWidth(intCol, sheetFrom.getColumnWidth(intCol));
					cellFrom = rowFrom.getCell(intCol);

					cellTo = rowTo.createCell(intCol);
				if (null == cellFrom)
					continue;
				cellTo.setCellStyle(cellFrom.getCellStyle());
				cellTo.setCellType(cellFrom.getCellType());
				if (null != cellFrom.getStringCellValue()
						&& !"".equals(cellFrom.getStringCellValue().trim()))
					cellTo.setCellValue(cellFrom.getStringCellValue());
			}
		}
		return sheetTo;
	}
    /**
	 * 输出Excel
	 * 
	 * @param workbook 工作簿
	 * @param excelPath 输出路径
	 * @return
	 * @throws IOException
	 */  
    public static void outputExcel(Workbook workbook, String excelPath) throws IOException {  
        FileOutputStream fOut = null;
        try {  
            fOut = new FileOutputStream(excelPath);  
            workbook.write(fOut);  
            fOut.flush();  
        } catch (FileNotFoundException fe) {  
        	throw fe;
        } catch (IOException e) {  
            throw e; 
        } finally {  
            try {  
                if (fOut != null)  
                    fOut.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
  
    }
}