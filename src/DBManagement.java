
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
	public final static String TABLE_NAME = "EnglishDictionary.xlsx";	
	public final static String SPLIT = ",";

	
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
	
	public List<String> getRecords(String fileName) throws IOException {
		
		List<String> res = new LinkedList<String>();
		
		if(fileNotExistsOrEmpty(fileName)) {
			return res;
		}
		
		openFile(TABLE_NAME);			
		
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
	
	public void openFile(String fileName) throws IOException {
		//excelFile = getClass().getResourceAsStream(fileName);
		excelFile = new FileInputStream(fileName);
		workbook = new XSSFWorkbook(excelFile);
        datatypeSheet = workbook.getSheetAt(0);	
	}
	
	public void closeFile(String fileName) throws IOException {
		//outputStream = new FileOutputStream(fileName);		
		outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
	}
	
	public boolean insertRecord(String[] recordArr, String fileName) throws IOException {		
		boolean isNewFile = false;
		if(fileNotExistsOrEmpty(fileName)) {
			workbook = new XSSFWorkbook();
	        datatypeSheet = workbook.createSheet("English Dic");	
	        isNewFile = true;
		}else{
			openFile(fileName);			
		}
				        		
		int keyIndex = getIndex(datatypeSheet.rowIterator(), recordArr[0]);
		if(keyIndex == -1) {
			int lastRowNum = isNewFile ? -1 : datatypeSheet.getLastRowNum();			
			Row currentRow = datatypeSheet.createRow(++lastRowNum);
			
			for(int col=0; col<recordArr.length; col++) {
				Cell cell = currentRow.createCell(col);
				cell.setCellValue(recordArr[col]);
			}
			closeFile(fileName);
		}else{
			closeFile(fileName);
			return false;
		}											
		return true;
	}		
	
	public boolean deleteRecord(String key, String fileName) throws IOException {
		
		if(fileNotExistsOrEmpty(fileName)) {
			return false;
		}
		
		openFile(fileName);
		
		int rowIndex = getIndex(datatypeSheet.rowIterator(), key);
		if(rowIndex == -1) {
			closeFile(fileName);
			return false;
		}
		
		datatypeSheet.removeRow(datatypeSheet.getRow(rowIndex));
		
		closeFile(fileName);
		return true;
	}
	
	public static void main(String[] args) {
		DBManagement db = new DBManagement();
		try {
			db.getRecords(TABLE_NAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

