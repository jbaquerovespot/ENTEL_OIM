package entel.oim.plugins.scheduler;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.io.*;
import java.util.*;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.platform.Platform;
import oracle.iam.scheduler.vo.TaskSupport;
import oracle.iam.platformservice.api.PlatformUtilsService;


/**
 * Execute the job to get the values for lookups from file
 * @author Oracle
 *
 */
public class CustomLookupsFromFileReconciliationTask extends TaskSupport {
	
	private final static String className = CustomLookupsFromFileReconciliationTask.class.getName();
	private static Logger logger = Logger.getLogger(className);


    private final tcLookupOperationsIntf lookupOprInf= Platform.getService(tcLookupOperationsIntf.class);
    private final PlatformUtilsService pltfSrv = Platform.getService(PlatformUtilsService.class);

	@Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
	
		logger.finer("Loading parameters");
		String file = (String) hm.get("File");
		String lookupDefinition = (String) hm.get("Lookup Definition");

		logger.finer("Checking existence of file");
		if ((file == null) || (file != null && file.isEmpty())) {
			throw new Exception("No file specified");
		}
		if (file != null && !file.isEmpty() && (!Files.exists(Paths.get(file)) || Files.isDirectory(Paths.get(file)))) {
			throw new Exception("Lookups File must be a file and it should exist");
		}
		
		logger.log(Level.FINE, "Processing all the lookups...");
		processLookupsValues(lookupDefinition,file);
		
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
     * Update all the lookups from file
     * @param lookupName
     * 			  Lookup that update
     * @param file
     * 			  File to read the lookup values. 
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	private void processLookupsValues(String lookupName, String file)
            throws Exception {

    	logger.entering(className, "processLookupsValues", lookupName);
    	HashMap<String, String> hmap = new HashMap<String, String>();
    	
    	try {
    		
    		logger.fine("Getting lookup values from "+ file);
    		Path path = Paths.get(file);
    		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
    		while (true) {
		        String line = reader.readLine();
		        if (line == null) {
		           break;
		        }
    		    String[] parsArray = line.split(";");
    		    logger.finest("Reading  lookup Code: "+ parsArray[0] + " | lookup Value: "+ parsArray[1]);
    		    hmap.put(parsArray[0], parsArray[1]);
		    }
    		reader.close();
		
			logger.fine("Getting lookup values from  "+ lookupName);
			tcResultSet rs = lookupOprInf.getLookupValues(lookupName);
			
			for (int i=0;i<rs.getRowCount();i++) {
				rs.goToRow(i);
				logger.finest("Deleting  "+ rs.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
			    lookupOprInf.removeLookupValue(lookupName,rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"));
			    
			}
			
			logger.fine("Let's sort the map in ascending order of value");
			HashMap<String, String> sorted = hmap
						.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

			for (Map.Entry me : sorted.entrySet()) {
		          logger.finest("Adding lookup-> Key: "+me.getKey() + " & Value: " + me.getValue());
		          lookupOprInf.addLookupValue(lookupName,me.getKey().toString(),me.getValue().toString(),"","");
	        }
			
			logger.finer("Purging the Lookup Vaues from OIM");
			pltfSrv.purgeCache("All");
			
		} catch (tcAPIException | tcInvalidLookupException | tcInvalidValueException | tcColumnNotFoundException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error", e);
			throw e;
		} 
		

		logger.exiting(className, "processLookupsValues", lookupName);
    }
    

}
