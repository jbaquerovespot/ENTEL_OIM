package entel.oim.plugins.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Thor.API.tcResultSet;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.scheduler.vo.TaskSupport;

public class UpdateExcelTemplateTask extends TaskSupport {

	private static final String className = UpdateExcelTemplateTask.class.getName();
	private static final Logger logger = Logger.getLogger(className);

	private tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);
	private final OrganizationManager orgMgr = Platform.getService(OrganizationManager.class);

	@Override
	public void execute(HashMap hm) throws Exception {
		logger.entering(className, "execute", hm);

		String ufp = (String) hm.get("Template File");
		String key = (String) hm.get("Key");

		if ((ufp == null ) || (ufp != null && ufp.isEmpty())) {
			throw new Exception("No files specified");
		}

		if (ufp != null && !ufp.isEmpty() && (!Files.exists(Paths.get(ufp)) || Files.isDirectory(Paths.get(ufp)))) {
			throw new Exception("Template File can't be a directory and the file must exist");
		}

		File file = new File(ufp);
		if (!file.isDirectory()) {
			logger.finest("Loading users file " + file.getName());
			updateTemplateExcel(file.getAbsolutePath(),key);
		}
			
		logger.exiting(className, "execute");
		
	}

	
	@Override
	public HashMap getAttributes() {
		return null;
	}

	@Override
	public void setAttributes() {

	}
	
	/**
	 * Update the lookups values in a sheet
	 * @param workbook Workbook object to update
	 * @param sheetName Sheet to update values
	 * @param lookupName Lookup from witch to load values
	 * @throws Exception
	 */
	public void updateLookupsValueInSheet(XSSFWorkbook workbook, String sheetName, String lookupName) throws Exception {

		logger.entering(className, "updateLookupsValueInSheet", new Object[] {sheetName,lookupName});
		
		logger.finer("Looping over Sheets looking for " +sheetName);
        int sheetNumber = -1;
        for(int i = 0; i < workbook.getNumberOfSheets() ; i++) {
        	String tmpSheetName = workbook.getSheetAt(i).getSheetName();
        	logger.finest(i+":" + tmpSheetName);
            if (tmpSheetName != null && tmpSheetName.equals(sheetName)) {
            	sheetNumber = i;
            }
        }

        logger.finer("Getting the Sheet " +sheetName+ " in sheet number " +sheetNumber);
        Sheet sheet = workbook.getSheetAt(sheetNumber);

        logger.finer("Iterating over Rows to delete previous values");
        for (int i = 1; i < sheet.getLastRowNum() ; i ++) {
        	Row row = sheet.getRow(i);
        	if (row != null && !isEmptyRow(row)) {
	        	logger.finest("Removing row: " +i);
	            try {
	            	sheet.removeRow(row);
	            } catch (Exception e) {
	            	logger.finest("Exception - Empty row: " +i);
	            }
        	}
        }
        
        
        logger.finer("Getting lookup values from  "+ lookupName);
		tcResultSet rs = lookupOps.getLookupValues(lookupName);
		
		HashMap<String, String> hmap = new HashMap<String,String>();
		logger.finer("Looping over results");
		for (int i=0;i<rs.getRowCount();i++) {
			rs.goToRow(i);
			logger.finest("Adding value: "+ rs.getStringValue("Lookup Definition.Lookup Code Information.Decode") + " to the Unsorted Map");
			hmap.put(rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"), rs.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
		}
		
		logger.fine("Let's sort the map in ascending order of value");
		HashMap<String, String> sorted = hmap
					.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

		
		logger.fine("Let's add the values to the Excel sheet");
		int rowNum = 1;
		for (Map.Entry<String,String> me : sorted.entrySet()) {
			logger.finest("Adding row # "+rowNum + " with values <" + me.getKey() + ";" + me.getValue() +">");
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue((String)me.getKey());
			row.createCell(1).setCellValue((String)me.getValue());
			rowNum++;
		}
        
		logger.exiting(className, "updateLookupsValueInSheet");

    }
	
	
	/**
	 * Update the key in a sheet
	 * @param workbook Workbook object to update
	 * @param sheetName Sheet to update values
	 * @param key Key to update
	 * @throws Exception
	 */
	public void updateKeyInSheet(XSSFWorkbook workbook, String sheetName, String key) throws Exception {

		logger.entering(className, "updateKeyInSheet", new Object[] {sheetName,key});
		
		logger.finer("Looping over Sheets looking for " +sheetName);
        int sheetNumber = -1;
        for(int i = 0; i < workbook.getNumberOfSheets() ; i++) {
        	String tmpSheetName = workbook.getSheetAt(i).getSheetName();
        	logger.finest(i+":" + tmpSheetName);
            if (tmpSheetName != null && tmpSheetName.equals(sheetName)) {
            	sheetNumber = i;
            }
        }

        logger.finer("Getting the Sheet " +sheetName+ " in sheet number " +sheetNumber);
        Sheet sheet = workbook.getSheetAt(sheetNumber);

        logger.finer("Setting the new key en excel file");
        Row row = sheet.getRow(0);
        row.createCell(0).setCellValue(key);
        
        logger.exiting(className, "updateKeyInSheet");

    }
	
	
	
	/**
	 * Update the template Excel for External Users
	 * @param absFilePath 
	 * @throws Exception 
	 */
	public void updateTemplateExcel(String absFilePath,String key) throws Exception {

		logger.entering(className, "updateTemplateExcel", absFilePath);
		
        logger.finer("Creating a Workbook from an Excel file (.xls or .xlsx)");
        InputStream inpStr = new FileInputStream(absFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(inpStr);
        
        logger.finer("Close Input File: " + absFilePath);
        inpStr.close();
        
        logger.finer("Updating sheets");
        updateOrganizationValuesInSheet(workbook,"Organizaciones");
        updateLookupsValueInSheet(workbook,"Tipos de Contratos","Lookup.Users.ContractType");
        updateLookupsValueInSheet(workbook,"País","Lookup.Users.WorkCountry");
        updateLookupsValueInSheet(workbook,"Gestores","Lookup.Users.Gestor");
        updateLookupsValueInSheet(workbook,"Canales","Lookup.Users.Channel");
        updateLookupsValueInSheet(workbook,"Cargos Externos","Lookup.Users.ExternJob");
        updateLookupsValueInSheet(workbook,"Habilidades","Lookup.Users.Ability");
        updateLookupsValueInSheet(workbook,"Responsabilidades","Lookup.Users.Responsability");
        updateLookupsValueInSheet(workbook,"Estamentos","Lookup.Users.Stratum");
        updateLookupsValueInSheet(workbook,"Sociedades","Lookup.Users.Society");
        updateLookupsValueInSheet(workbook,"Unidades","Lookup.Users.Unit");
        updateKeyInSheet(workbook,"Key",key);
        
        logger.finer("Creating SXSSFWorkbook file");
        SXSSFWorkbook wbSXSSF = new SXSSFWorkbook(workbook); 
        wbSXSSF.setCompressTempFiles(true);
        
        logger.finer("Creating output file");
        FileOutputStream outFile =new FileOutputStream(new File(absFilePath));
        
        logger.finer("Writing output file");
        wbSXSSF.write(outFile);
        
        logger.finer("Closing output files");
        outFile.close();
        wbSXSSF.close();
        workbook.close();

        logger.exiting(className, "updateTemplateExcel", absFilePath);
    }
	
	
	/**
	 * Get the keys of the organization of type Extern
	 * @return External Organization list key 
	 * @throws Exception
	 */
	private Map<String,String> getExternOrgMap() throws Exception {
		
		logger.entering(className,"getExternOrgMap");
		Map<String,String> externOrgKeysMap = new HashMap<String,String>();
		
		try {
			
			logger.finer("Setting the attributes to find of the organization");
			Set<String> searchAttrs = new HashSet<String>();
			searchAttrs.add(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());
		  
			logger.finer("Getting Organization Key of Extern");
			Organization orgExtern = orgMgr.getDetails("Externos", searchAttrs, true);
			
			logger.finer("Getting Organizations Keys Childs for Extern");
			HashMap<String, Object> configParams = new HashMap<String, Object>();
			List<Organization> childOrgExtern = orgMgr.getChildOrganizations(orgExtern.getEntityId(), searchAttrs, configParams);
		    
			logger.finer("Constructing ths list");
			for (Organization org : childOrgExtern) {
				logger.finer("Organization found: Key -> " + org.getEntityId()+ " | Name -> "+org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
				externOrgKeysMap.put(org.getEntityId(), (String)org.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
			}
		
		} catch (OrganizationManagerException | AccessDeniedException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getExternOrgMap - Unexpected error", e);
			throw e;
		}
		
		logger.exiting(className,"getExternOrgMap",externOrgKeysMap);
		return externOrgKeysMap;
	}
	
	
	
	/**
	 * Update the Organization lookups values in a sheet
	 * @param workbook Workbook object to update
	 * @param sheetName Sheet to update values
	 * @throws Exception
	 */
	public void updateOrganizationValuesInSheet(XSSFWorkbook workbook, String sheetName) throws Exception {

		logger.entering(className, "updateOrganizationValuesInSheet", new Object[] {sheetName});
		
		logger.finer("Looping over Sheets looking for " +sheetName);
        int sheetNumber = -1;
        for(int i = 0; i < workbook.getNumberOfSheets() ; i++) {
        	String tmpSheetName = workbook.getSheetAt(i).getSheetName();
        	logger.finest(i+":" + tmpSheetName);
            if (tmpSheetName != null && tmpSheetName.equals(sheetName)) {
            	sheetNumber = i;
            }
        }

        logger.finer("Getting the Sheet " +sheetName+ " in sheet number " +sheetNumber);
        Sheet sheet = workbook.getSheetAt(sheetNumber);

        logger.finer("Iterating over Rows to delete previous values");
        for (int i = 1; i <sheet.getLastRowNum() ; i ++) {
        	Row row = sheet.getRow(i);
        	logger.finest("Removing row: " +i);
            try {
            	sheet.removeRow(row);
            } catch (Exception e) {
            	logger.finest("Exception - Empty row: " +i);
            }
        }
        
        logger.finer("Getting values of Organizations");
		Map<String,String> orgMap = getExternOrgMap();
		
		logger.fine("Let's sort the map in ascending order of value");
		HashMap<String, String> sorted = orgMap
					.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

		
		logger.fine("Let's add the values to the Excel sheet");
		int rowNum = 1;
		for (Map.Entry<String, String> me : sorted.entrySet()) {
			logger.finest("Adding row # "+rowNum + " with values <" + me.getKey() + ";" + me.getValue() +">");
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(me.getKey());
			row.createCell(1).setCellValue((String) me.getValue());
			rowNum++;
		}
        
		logger.exiting(className, "updateOrganizationValuesInSheet");

    }
	
	
	
	
	/**
	 * Check if a row is empty
	 * @param row Row to check
	 * @return True if the row is empty. False if not.
	 */
	private static boolean isEmptyRow(Row row) {
		
		logger.entering(className, "isEmptyRow");
		boolean isEmpty = true;
		
		logger.finest("Looping over all cells of the row");
        for(Cell cell: row) {
        	if(cell.getCellType() != Cell.CELL_TYPE_FORMULA 
        			&& cell.getCellType() != Cell.CELL_TYPE_BLANK
        			&& cell.getCellType() != Cell.CELL_TYPE_ERROR) {
        		logger.finest("Cell with value found: " + cell);
        		
        		isEmpty = false;
        		break;
            } 
        }
		
        logger.exiting(className, "isEmptyRow", isEmpty);
		return isEmpty;
	
	}
	
}
