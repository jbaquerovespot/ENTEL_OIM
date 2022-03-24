package entel.oim.plugins.namegenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.ssointg.impl.handlers.account.commonname.plugins.api.CommonNamePolicy;
import oracle.iam.ssointg.exception.CommonNameGenerationException;
import oracle.iam.platform.Platform;


public class CustomCommonNamePolicy implements CommonNamePolicy{
	
	private final static String className = CustomCommonNamePolicy.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	// Attr Keys
	private final static String ATTR_FIRSTNAME_ID = UserManagerConstants.AttributeName.FIRSTNAME.getId();
	private final static String ATTR_MIDDLENAME_ID = UserManagerConstants.AttributeName.MIDDLENAME.getId();
	private final static String ATTR_LASTNAME_ID = UserManagerConstants.AttributeName.LASTNAME.getId();
	private final static String ATTR_SURNAME_ID = "SurName";
	

    public CustomCommonNamePolicy() {
        super();
    }

    @Override
    public String getCommonNameFromPolicy(Map<String, Object> reqData) throws CommonNameGenerationException {
        
    	logger.entering(className, "getCommonNameFromPolicy");
		String cn = null;
		try {
		logger.fine("Getting the values of fields");
		String firstName = reqData.get(ATTR_FIRSTNAME_ID) == null ? null : reqData.get(ATTR_FIRSTNAME_ID).toString();
		String secondName = reqData.get(ATTR_MIDDLENAME_ID) == null ? null : reqData.get(ATTR_MIDDLENAME_ID).toString();
		String lastName = reqData.get(ATTR_LASTNAME_ID) == null ? null : reqData.get(ATTR_LASTNAME_ID).toString();
		String surname = reqData.get(ATTR_SURNAME_ID) == null ? null : reqData.get(ATTR_SURNAME_ID).toString();
		
		logger.fine("Calling the method to generate de CN");
		cn = createCommonName(firstName,secondName,lastName,surname);
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected error - getCommonNameFromPolicy", e);
			throw new CommonNameGenerationException("Unexpected error - getCommonNameFromPolicy",e,e.getCause().getMessage());
		}
    	logger.exiting(className, "getCommonNameFromPolicy");
		return cn;
		
    }
    
    
    /**
	 * Creates the Common Name
	 * @param firstName First name of the user
	 * @param secondName Second name of the user
	 * @param lastName Last name of the user
	 * @param surname Surname of the user
	 * @param indRemoveReservedWords Flag to determinate if remove reserved words
	 * @return User Common Name of the user
	 * @throws Exception 
	 */
	public static String createCommonName(String firstName,String secondName,String lastName,String surname) throws Exception {
		
		logger.entering(className, "createCommonName");
		String userCN=null;
		
		try {
			logger.fine("Remove special characters from attributes");
			firstName = removeSpecialCharacters(firstName);
			secondName = removeSpecialCharacters(secondName);
			lastName = removeSpecialCharacters(lastName);
			surname = removeSpecialCharacters(surname);
			logger.finest("Values of the user: "
					+ "	firstName: " +firstName 
					+ " | secondName:  " + secondName
					+ " | lastName:  " + lastName
					+ " | surname:  " + surname);
			
			
			logger.fine("Calling the generation of the ID");
			int attempts = 1;
			while(true) {
			
				logger.fine("Checking if userCN \""+userCN+"\" exists");
				if(userCN != null && !existsUserCN(userCN)){
				
					logger.fine("UserCN \""+userCN+"\" not exist in OIM");
					break;
					
				} else{
					
					logger.fine("Constructing userCN with attempt: " + attempts);
					userCN = constructUserCN(attempts,firstName,secondName,lastName,surname);
					logger.fine("Constructed userCN: " + userCN);
					
					attempts++;
					
					logger.fine("Checking max attempts");
					if ("-1".equals(userCN)) {
						logger.severe("Cannot construct userCN. Reached max attempts");
						userCN = null;
						break;
					}
					
				}
			
			}
		
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error - createCommonName", e);
			e.printStackTrace();
			throw e;
		}
		
		logger.exiting(className, "createCommonName");
		return userCN;
	}
	
	
	
	/**
	 * Construct the user common name of the user using predefined rules
	 * @param attempts Attempts to constructs the user common name of the user
	 * @param firstName First Name of the user
	 * @param secondName Second Name of the user
	 * @param lastName Last Name of the user
	 * @param surname SurName of the user
	 * @return The user common name of the user
	 */
	public static String constructUserCN(int attempts, String firstName,String secondName,String lastName,String surName) {		
		
		logger.entering(className, "constructUserCN");
		String userCN = null;

		if(attempts==1){
			
			logger.finest("First character of firstname + lastname");
			userCN = firstName + (secondName == null ? "" : " " + secondName )
					           + " " + lastName
					           + (surName == null ? "" : " " + surName);
			
		} else if (attempts <= 10) {
		
			logger.finest("First character of firstname + lastname");
			userCN = firstName + (secondName == null ? "" : " " +secondName )
					           + " " + lastName
					           + (surName == null ? "" : " " +surName)
					           + attempts;
		
		} else if(attempts > 10) {
			
			String errorMsg ="Cannot create userCN with all the rules defined. All options were used.";
			logger.severe(errorMsg);
			return "-1";
		}
		
		logger.exiting(className, "constructUserCN",userCN);
		return userCN;
	}


    @Override
    public boolean isCommonNameValid(String commonName, Map<String,Object> userData) {
       return true;
    }

    @Override
    public String getDescription(Locale locale) {
        return "Custom policy - returns full name as common name";
    }
    
    
    /**
	 * Remove specials characters from a string
	 * @param words String to format
	 * @return Words without specials characters
	 */
	public static String removeSpecialCharacters(String words){
		
		logger.entering(className, "removeSpecialCharacters", words);
		
		if (words != null) {
			words = words.replaceAll("[á,ä,à]","a");
			words = words.replaceAll("[Á,Ä,À]","A");
			words = words.replaceAll("[é,ë,è]","e");
			words = words.replaceAll("[É,Ë,È]","E");
			words = words.replaceAll("[í,ï,ì]","i");
			words = words.replaceAll("[Í,Ï,Ì]","I");
			words = words.replaceAll("[ó,ö,ò]","o");
			words = words.replaceAll("[Ó,Ö,Ò]","O");
			words = words.replaceAll("[ú,ü,ù]","u");
			words = words.replaceAll("[Ú,Ü,Ù]","U");
			words = words.replaceAll("[ñ]","n");
			words = words.replaceAll("[Ñ]","N");
			words = words.replaceAll("[^a-zA-Z]+","");
		} 
		
		logger.exiting(className, "removeSpecialCharacters", words);
		return words;
	}
	
	
	/**
	 * Check if the user common name exists in OIM
	 * @param userCN User Common Name to check
	 * @return True if the user common name exist in OIM. False if the user common name doesn't exist in OIM
	 */
	public static boolean existsUserCN(String userCN) throws Exception {
		
		logger.entering(className, "existsUserCN",userCN);
		boolean exist = false;
		PreparedStatement stmt = null;
		Connection oimdb= null;
    	try {
    	
    		logger.fine("Getting OIM Connection");
	    	oimdb = Platform.getOperationalDS().getConnection();
		
	    	logger.fine("Constructing SQL");
	    	String sql = "select 1 from usr where UPPER(usr_common_name) = UPPER('" +userCN+"')";
			logger.finest("SQL to execute: " + sql);
					
	    	logger.finer("Executing query");
	    	stmt = oimdb.prepareStatement(sql);
	    	ResultSet rs = stmt.executeQuery();
	
		    if (!rs.isBeforeFirst()) {
		    	logger.finer("No users found!");
		    } else {
		    	exist = true;
			}
				    
		    logger.finest("Closing result set...");
		    rs.close();
	    	
	    } catch (SQLException e) {
			logger.log(Level.SEVERE, "Unexpected error - existsUserCN", e);
			e.printStackTrace();
			throw e;
		} finally {
    		if (stmt != null) {
    			logger.finest("Closing Prepared Statement...");
    			stmt.close();
		    }
    		if (oimdb != null) {
    			logger.finer("Closing database connection!");
    			oimdb.close();
		    }
    	}
    	
    	logger.exiting(className, "existsUserCN",exist);
		return exist;
		
	}

	
}