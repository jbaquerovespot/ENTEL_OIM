package entel.oim.connectors.sau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Contains utilities used by SAU Connector
 * @author Oracle
 *
 */
public class UtilitiesSAU {
	
	private final static String className = UtilitiesSAU.class.getName();
	private static Logger logger = Logger.getLogger(className);

	/**
	 * Find in database an user information of SAU
	 * @param userId d of the user to find
	 * @param dbUrlOIM database jdbc URL
	 * @param userOIM User to connect to the database
	 * @param passOIM Password to connect to the database
	 * @return A map with all the information of the user
	 * @author Oracle
	 */
	public static HashMap<String, String> findUserAccount(String userId, String dbUrlOIM, String oimScheme, String userOIM, String passOIM) {
		
		
		logger.entering(className, "findUserAccount", userId);
		HashMap<String, String> map = new HashMap<>();
		
		try{
			
			logger.fine("Looking for driver for Oracle Database");
		 	try {
		 		Class.forName("oracle.jdbc.driver.OracleDriver");
		 	} catch (ClassNotFoundException e) {
		 		e.printStackTrace();
	        	logger.severe("Oracle JDBC Driver not found");
	        	return null;
		 	}
		 	logger.fine("Oracle JDBC Driver Registered!");
		 	
		 	
		 	logger.fine("Trying connection!");
		 	Connection connection = null;
		 	try {
		 		connection = DriverManager.getConnection(
		 				dbUrlOIM, userOIM, passOIM);
		 	} catch (SQLException e) {
		 		logger.severe("Connection Failed! Check output console: " + e.getMessage());
	            e.printStackTrace();
	            return null;
		 	}
		 	
		 	if (connection != null) {
		 		logger.fine("Connection success. Preparing query");
  
		 		Statement stmtSelect = null;
		 		String query = "select UD_SAU_TARG.* from "+oimScheme+".UD_SAU_TARG where UD_SAU_TARG.UD_SAU_TARG_USERID = '" + userId + "'";
		 		logger.finest("Query to execute: " + query);
		 		try {
		 			
		 			logger.fine("Executing query...");
		 			stmtSelect = connection.createStatement();
		 			ResultSet rs = stmtSelect.executeQuery(query);
		 			
		 			logger.fine("Checking if found...");
		 			if (rs.next()) {
		 				logger.finest("Map values: " + rs.toString());
			 			String sauITServer = rs.getString("UD_SAU_TARG_SERVER");
		 				String mapUserId = (rs.getString("UD_SAU_TARG_USERID") != null) ? rs.getString("UD_SAU_TARG_USERID").replaceFirst(sauITServer+"~", "") : null;
			 			String mapRut = (rs.getString("UD_SAU_TARG_RUT") != null) ? rs.getString("UD_SAU_TARG_RUT").replaceFirst(sauITServer+"~", "") : null;
			 			String mapNames = (rs.getString("UD_SAU_TARG_NAMES") != null) ? rs.getString("UD_SAU_TARG_NAMES").replaceFirst(sauITServer+"~", "") : null;
			 			String mapLastName = (rs.getString("UD_SAU_TARG_LASTNAME") != null) ? rs.getString("UD_SAU_TARG_LASTNAME").replaceFirst(sauITServer+"~", "") : null;
			 			String mapEmail = (rs.getString("UD_SAU_TARG_EMAIL") != null) ? rs.getString("UD_SAU_TARG_EMAIL").replaceFirst(sauITServer+"~", "") : null;
			 			String mapCompanyType = (rs.getString("UD_SAU_TARG_COMPANYTYPE") != null) ? rs.getString("UD_SAU_TARG_COMPANYTYPE").replaceFirst(sauITServer+"~", "") : null;
			 			String mapLastChangeBy = (rs.getString("UD_SAU_TARG_LASTCHANGEBY") != null) ? rs.getString("UD_SAU_TARG_LASTCHANGEBY").replaceFirst(sauITServer+"~", "") : null;
			 			String mapUserType = (rs.getString("UD_SAU_TARG_USERTYPE") != null) ? rs.getString("UD_SAU_TARG_USERTYPE").replaceFirst(sauITServer+"~", "") : null;
			 			map.put("USERID", mapUserId);
			 			map.put("RUT", mapRut);
		 				map.put("NAMES", mapNames);
		 				map.put("LASTNAME", mapLastName);
		 				map.put("EMAIL", mapEmail);
		 				map.put("COMPANYTYPE", mapCompanyType);
		 				map.put("LASTCHANGEBY", mapLastChangeBy);
		 				map.put("USERTYPE", mapUserType);
		 			
		 			} else {
		 				logger.finest("User NOT found");
		 			}
		 			
		 			
		 		} catch (SQLException e ) {
		 			e.printStackTrace();
		 		} finally {
		 			if (stmtSelect != null) { 
		 				stmtSelect.close(); 
		 			  	}
		 			if (connection != null) { 
		 				connection.close();
                    }
		 		}
		 		
 
		 	} else {
		 		logger.severe("Failed to make connection!");
		 	}
	    
		} catch (Exception e ) {
			e.printStackTrace();
			logger.log(Level.SEVERE,"findUserAccount - Unexpected exception", e);
	    }
		
		
		logger.exiting(className, "findUserAccount");
		return map;
		
	}


}
