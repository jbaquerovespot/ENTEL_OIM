package entel.oim.plugins.scheduler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import oracle.iam.platform.Platform;
import oracle.iam.platformservice.api.PlatformUtilsService;
import oracle.iam.scheduler.vo.TaskSupport;
import org.apache.poi.ss.usermodel.*;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import entel.oim.adapters.Utilities;

public class InternalControlListTask extends TaskSupport {

	private static final String className = InternalControlListTask.class.getName();
	private static final Logger logger = Logger.getLogger(className);

	private tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);
	private PlatformUtilsService pltfSrv = Platform.getService(PlatformUtilsService.class);

	
	@Override
	public void execute(HashMap hm) throws Exception {
		logger.entering(className, "execute", hm);

		String file = (String) hm.get("Internal Control List File");
		String lookupDefinition = (String) hm.get("Lookup Definition");

		logger.finer("Checking existence of file");
		if ((file == null) || (file != null && file.isEmpty())) {
			throw new Exception("No file specified");
		}
		if (file != null && !file.isEmpty() && (!Files.exists(Paths.get(file)) || Files.isDirectory(Paths.get(file)))) {
			throw new Exception("Internal Control List File must be a file and it should exist");
		}

		logger.log(Level.FINE, "Processing the internal control list file...");
		loadInternalControlUsers(lookupDefinition,file);

	}

	

	
	@Override
	public HashMap getAttributes() {
		return null;
	}

	@Override
	public void setAttributes() {

	}

	/**
	 * Read an excel file
	 * @param lookupName
	 * 				Lookup to update
	 * @param absFilePath
	 * 				File to read
	 * @throws Exception
	 */
	public void loadInternalControlUsers(String lookupName, String absFilePath) throws Exception {

		logger.entering(className, "loadInternalControlUsers", absFilePath);
		HashMap<String,String> hmap = new HashMap<String ,String>();
		
		logger.finer("Creating a Workbook from an Excel file (.xls or .xlsx)");
		Workbook workbook = WorkbookFactory.create(new File(absFilePath));

		logger.finest("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		logger.finer("Looping over Sheets looking for Usuarios");
		int sheetNumber = -1;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			String sheetName = workbook.getSheetAt(i).getSheetName();
			logger.finest("=> " + sheetName);
			if (sheetName != null && sheetName.equals("Usuarios")) {
				sheetNumber = i;
			}
		}

		logger.finer("Get the Sheet: Usuarios");
		Sheet sheet = workbook.getSheetAt(sheetNumber);

		logger.finer("Iterating over Rows and Columns beginning in the first users lines (line #2)");
		for (int i = 2; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			logger.finer("Check if not empty row");
			if (!isEmptyRow(row)) {
				logger.finer("Processing row #" + (i+1));
				saveUserInfo(hmap,row);
			}

		}

		
		logger.finer("Closing workbook");
		workbook.close();
		
		logger.finer("Deleting previous lookup values");
		deleteInternalControlUsers(lookupName);
		
		logger.finer("Adding new lookup values");
		addInternalControlUsers(hmap, lookupName);
	
		logger.finer("Purging the Lookup Vaues from OIM");
		pltfSrv.purgeCache("LookupValues");

		logger.exiting(className, "loadInternalControlUsers", absFilePath);
	}

	
	/**
	 * Check if a row is empty
	 * 
	 * @param row
	 *            Row to check
	 * @return True if the row is empty. False if not.
	 */
	private static boolean isEmptyRow(Row row) {

		logger.entering(className, "isEmptyRow");
		boolean isEmpty = true;

		logger.finest("Looping over all cells of the row");
		for (Cell cell : row) {
			if (cell.getCellType() != Cell.CELL_TYPE_FORMULA && cell.getCellType() != Cell.CELL_TYPE_BLANK
					&& cell.getCellType() != Cell.CELL_TYPE_ERROR) {
				logger.finest("Cell with value found: " + cell);

				isEmpty = false;
				break;
			}
		}

		logger.exiting(className, "isEmptyRow", isEmpty);
		return isEmpty;

	}

	/**
	 * Get an user info
	 * 
	 * @param row
	 *            Row of the sheet with the user info to process
	 * @return Entry of the user
	 * @throws Exception
	 */
	private void saveUserInfo(HashMap<String,String> hmap, Row row) throws Exception {

		logger.entering(className, "saveUserInfo", row);
		Utilities util = new Utilities();
		String rut = null;
		String names = null;
		String lastName = null;
		String nickName = null;
		String organization = null;
		String ability = null;
		String society = null;
		String job = null;
		String reason = null;
		Date startDate = null;
		Date endDate = null;
		

		logger.finer("Getting all the atttributes of the user in the row");
		Cell rutCell = row.getCell(0);
		if (rutCell != null) {
			rut = getStringValueOfCell(rutCell).toUpperCase().replace("-", "").replace(".", "").replace(" ","");
		}
		
		Cell namesCell = row.getCell(1);
		if (namesCell != null) {
			names = util.toCapitalizeString(getStringValueOfCell(namesCell));
		}

		Cell lastNameCell = row.getCell(2);
		if (lastNameCell != null) {
			lastName = util.toCapitalizeString(getStringValueOfCell(lastNameCell));
		}

		Cell nickNameCell = row.getCell(3);
		if (nickNameCell != null) {
			nickName = util.toCapitalizeString(getStringValueOfCell(nickNameCell));
		}
		
		
		Cell organizationCell = row.getCell(4);
		if (organizationCell != null) {
			organization = util.toCapitalizeString(getStringValueOfCell(organizationCell));
		}

		Cell abilityCell = row.getCell(5); 
		if (abilityCell != null) {
			ability = util.toCapitalizeString(getStringValueOfCell(abilityCell));

		}
		
		Cell societyCell = row.getCell(6); 
		if (societyCell != null) {
			society = util.toCapitalizeString(getStringValueOfCell(societyCell));

		}
		
		Cell jobCell = row.getCell(7); 
		if (jobCell != null) {
			job = util.toCapitalizeString(getStringValueOfCell(jobCell));

		}
		
		Cell reasonCell = row.getCell(8); 
		if (reasonCell != null) {
			reason = util.toCapitalizeString(getStringValueOfCell(reasonCell));

		}

		Cell startDateCell = row.getCell(9);
		if (startDateCell != null) {
			startDate = startDateCell.getDateCellValue();

		}

		Cell endDateCell = row.getCell(10);
		if (endDateCell != null) {
			endDate = endDateCell.getDateCellValue();

		}

		logger.finer("Checking if the user is active in the internal control list");
		if (endDate == null || endDate.before(new java.util.Date())) {
			hmap.put(rut, names + "|" + lastName + "|" + nickName +"|" + organization + "|" + ability + "|" +society + "|" + job + "|" + reason + "|" + startDate + "|" + endDate);
		}
		
		logger.exiting(className, "saveUserInfo");

	}
	

	/**
	 * Return the string value of a cell
	 * 
	 * @param cell
	 *            Cell to convert
	 * @return String value of a cell
	 */
	private String getStringValueOfCell(Cell cell) {

		logger.entering(className, "getStringValueOfCell", cell);
		String value = null;

		logger.finest("Checking if not null the cell");
		if (cell != null) {

			logger.finest("Checking cell type");
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				logger.finest("It is a String");
				value = cell.getStringCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				logger.finest("It is a Number. Converting...");
				value = String.valueOf((int) cell.getNumericCellValue());
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				logger.finest("It is Empty");
				value = "";
			}
			if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				logger.finest("It is a Formula. Checking type of Value");
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
					logger.finest("It is a String");
					value = cell.getStringCellValue();
				}
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
					logger.finest("It is a Number. Converting...");
					value = String.valueOf((int) cell.getNumericCellValue());
				}
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_BLANK) {
					logger.finest("It is Empty");
					value = "";
				}
			}

		}

		logger.exiting(className, "getStringValueOfCell", value);
		return value;

	}

    
    /**
     * Delete previous values from lookup
     * @param lookupName
     * 			Lookup to delete
     * @throws Exception
     */
    private void deleteInternalControlUsers(String lookupName) throws Exception {
    	
    	
    	logger.entering(className, "deleteInternalControlUsers");
		try {
			
			logger.fine("Getting lookup values from  "+ lookupName);
			tcResultSet rs = lookupOps.getLookupValues(lookupName);
			
			for (int i=0;i<rs.getRowCount();i++) {
				rs.goToRow(i);
				logger.finest("Deleting  "+ rs.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
				lookupOps.removeLookupValue(lookupName,rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"));
			    
			}
			
			logger.exiting(className, "deleteInternalControlUsers");
			
		} catch (tcAPIException | tcInvalidLookupException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"deleteInternalControlUsers - Unexpected error",e);
			logger.exiting(className, "deleteInternalControlUsers");
			throw e;
		} 
		
	}
    
    
    /**
     * Add values to lookup
     * @param lookupName
     * 			Lookup to update
     * @throws Exception
     */
    private void addInternalControlUsers(HashMap<String,String> hmap, String lookupName) throws Exception {
    	
    	logger.entering(className, "addInternalControlUsers",hmap);
		try {
			
			logger.fine("Let's sort the map in ascending order of value");
			HashMap<String, String> sorted = hmap
						.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

			for (Map.Entry<String,String> me : sorted.entrySet()) {
		          logger.finest("Adding lookup-> Key: "+me.getKey() + " & Value: " + me.getValue());
		          lookupOps.addLookupValue(lookupName,me.getKey().toString(),me.getValue().toString(),"","");
		    }

			logger.exiting(className, "addInternalControlUsers");
			
		} catch (tcAPIException | tcInvalidLookupException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"addInternalControlUsers - Unexpected error",e);
			logger.exiting(className, "addInternalControlUsers");
			throw e;
		} 
		
	}
    
}
