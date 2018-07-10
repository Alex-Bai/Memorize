package com.bcgkyy.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DBManagement {
	
	private InputStream excelFile;
	private FileOutputStream outputStream;
	private Workbook workbook;
	private Sheet datatypeSheet;	
	private String TABLE_NAME = "EnglishDictionary.xlsx";	
	private String SHEET_NAME = "EnglishDic";
	public final static String SPLIT = ",";

	public DBManagement() {}
	
	public DBManagement(String tableName, String sheetName) {
		this.TABLE_NAME = tableName;
		this.SHEET_NAME = sheetName;
	}
	
	public boolean fileNotExistsOrEmpty(String fileName) {
		File newFile = new File(fileName);
		try {
			if(newFile.createNewFile() || newFile.length()==0) {
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public List<String> getRecords() throws IOException {
		
		List<String> res = new LinkedList<String>();
		
		if(fileNotExistsOrEmpty(TABLE_NAME) || !openFile()) {
			return res;
		}									
		
		Iterator<Row> rowIterator = datatypeSheet.iterator();		
		while(rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			Iterator<Cell> cellIterator = currentRow.cellIterator();
			StringBuilder sb = new StringBuilder();
			boolean isFirstCell = true;
			while(cellIterator.hasNext()) {
				if(!isFirstCell) {
					sb.append(SPLIT);
				}
				sb.append(cellIterator.next());
				isFirstCell = false;
			}	
			res.add(sb.toString());
		}
		
		//closeFile(TABLE_NAME);
		workbook.close();
		
		return res;
	}
	
	public int getIndex(Iterator<Row> interator, String key) throws IOException {
		int rowNum = 0;
		boolean findVal = false;
		while(interator.hasNext()) {			
			Row currentRow = interator.next();
			Iterator<Cell> cellInterator = currentRow.cellIterator();			
			while(cellInterator.hasNext()) {
				if(cellInterator.next().getStringCellValue().equals(key)) {
					findVal = true;
					break;
				}						
			}
			if(findVal) break;
			rowNum++;
		}                
		return findVal ? rowNum : -1;
	}
	
	public void getSheetBasedonName() {
		if(workbook != null) {
			int numberSheets = workbook.getNumberOfSheets();
			for(int index=0; index<numberSheets; index++) {
				String sheetName = workbook.getSheetName(index);
				if(sheetName.equals(SHEET_NAME)) {
					datatypeSheet = workbook.getSheetAt(index);
					break;
				}
			}			
		}
	}
	
	public boolean openFile() throws IOException {
		//excelFile = getClass().getResourceAsStream(fileName);
		excelFile = new FileInputStream(TABLE_NAME);
		workbook = new XSSFWorkbook(excelFile);
		getSheetBasedonName();
		if(datatypeSheet == null) {
			datatypeSheet = workbook.createSheet(SHEET_NAME);
			return false;
		}
		return true;
	}
	
	public void closeFile() throws IOException {
		//outputStream = new FileOutputStream(fileName);		
		outputStream = new FileOutputStream(TABLE_NAME);
        workbook.write(outputStream);
        workbook.close();
	}
	
	public boolean insertRecord(String[] recordArr) throws IOException {		
		boolean isNewFile = false;
		if(fileNotExistsOrEmpty(TABLE_NAME) || !openFile()) {
			workbook = new XSSFWorkbook();
	        datatypeSheet = workbook.createSheet(SHEET_NAME);	
	        isNewFile = true;
		}
				        		
		int keyIndex = getIndex(datatypeSheet.rowIterator(), recordArr[0]);
		if(keyIndex == -1) {
			int lastRowNum = isNewFile ? -1 : datatypeSheet.getLastRowNum();			
			Row currentRow = datatypeSheet.createRow(++lastRowNum);
			
			for(int col=0; col<recordArr.length; col++) {
				Cell cell = currentRow.createCell(col);
				cell.setCellValue(recordArr[col]);
			}
			closeFile();
		}else{
			closeFile();
			return false;
		}											
		return true;
	}		
	
	public boolean deleteRecord(String key, String fileName) throws IOException {
		
		if(fileNotExistsOrEmpty(fileName) || !openFile()) {
			return false;
		}
		
		int rowIndex = getIndex(datatypeSheet.rowIterator(), key);
		if(rowIndex == -1) {
			closeFile();
			return false;
		}
		
		datatypeSheet.removeRow(datatypeSheet.getRow(rowIndex));
		
		closeFile();
		return true;
	}
	
	public static void main(String[] args) {		
		try {
			String tableName = "NumberMemorize.xlsx";
			String sheetName = "2018-07";
			String recordArr[] = new String[]{"01","150"};
			DBManagement db = new DBManagement(tableName, sheetName);
			//db.insertRecord(recordArr);
			List<String> records = db.getRecords();
			for(String str : records) {
				System.out.println(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

