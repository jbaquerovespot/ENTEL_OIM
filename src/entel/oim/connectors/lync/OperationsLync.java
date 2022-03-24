package entel.oim.connectors.lync; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



/**
 * Contains all the connector operations for Lync application
 * @author Oracle
 *
 */
public class OperationsLync {

	private final static String className = OperationsLync.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	private final static String SUCCESS_MSG = "POWERSHELL COMPLETED SUCCESSFULLY";
	
	
	
	/**
	 * Add an user in Lync
	 * @param psScriptsDir Directory where the powershells scripts resides
	 * @param smAccountName identity of the user to create
	 * @param registrarPool Pool server in Lync for the account
	 * @param sipDomain SipDomain in Lync for the account
	 * @param sipAddressType SipAddressType in Lync for the account
	 * @param sipAddress SipAddress in Lync for the account
	 * @return Result of the powershell
	 */
	public static boolean createUser (	String psScriptsDir,
										String smAccountName,
										String registrarPool,
										String sipDomain,
										String sipAddressType) {
		
		// Log
		logger.entering(className, "createuser");
		boolean success = false;
			
		try {
			
			logger.finest("Calling the powershell");
			Process p = new ProcessBuilder()
	                //.inheritIO()
	                .command("powershell", "-ImportSystemModules", psScriptsDir + "LYNCCreateUser.ps1",
	                				 smAccountName,
									 registrarPool,
									 sipDomain,
									 sipAddressType).start();
			
			logger.finer("Calling procedure to get the output of the process");
			List<String> linesList = getProcessOutput(p);
			
			logger.finest("Wait for process to finish");
			p.waitFor();
			
			logger.finer("Calling procedure to check errors in the process");
			success = checkProcessSucces(linesList);
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "createuser - Unexpected error",e);
			
		}
		
		logger.exiting(className, "createuser");
        return success;
	
	}
	
	
	
	
	/**
	 * Remove an user in Lync
	 * @param psScriptsDir Directory where the powershells scripts resides
	 * @param smAccountName identity of the user to delete
	 * @return Result of the powershell
	 */
	public static boolean deleteUser (	String psScriptsDir,
										String smAccountName) {
		
		
			
		// Log
		logger.entering(className, "deleteuser");
		boolean success = false;
		
		try {
				
			logger.finest("Calling the powershell");
			Process p = new ProcessBuilder()
	                //.inheritIO()
	                .command("powershell", "-ImportSystemModules", psScriptsDir + "LYNCDeleteUser.ps1",
	                				 smAccountName).start();
			
			logger.finer("Calling procedure to get the output of the process");
			List<String> linesList = getProcessOutput(p);
			
			logger.finest("Wait for process to finish");
			p.waitFor();
			
			logger.finer("Calling procedure to check errors in the process");
			success = checkProcessSucces(linesList);
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "deleteuser - Unexpected error",e);
			
		}
		
		// Return
		logger.exiting(className, "deleteuser");
		return success;
	
	}
	
	
	
	/**
	 * Update an user in Lync
	 * @param psScriptsDir Directory where the powershells scripts resides
	 * @param smAccountName identity of the user to create
	 * @param audioVideoDisabled Flag for AudioVideo
	 * @param enabled Flag for Enable or disable the account
	 * @param enterpriseVoiceEnabled Flag for EnterpriseVoice
	 * @param hostedVoiceMail Flag for HostedVoice
	 * @param lineURI LineURI of the account
	 * @param lineServerURI LineServerURI of the account
	 * @param privateLine PrivateLine of the account
	 * @param remoteCallControlTelephonyEnabled Flag for
	 * @param sipAddress SipAddress in Lync for the account
	 * @return Result of the powershell
	 */
	public static boolean updateUser (	String psScriptsDir,
										String smAccountName,
										String audioVideoDisabled,
										String enabled,
										String enterpriseVoiceEnabled,
										String hostedVoiceMail,
										String lineURI,
										String lineServerURI,
										String privateLine,
										String remoteCallControlTelephonyEnabled,
										String sipAddress) {
		
		// Log
		logger.entering(className, "updateuser");
		boolean success = false;
		
		try {
			
			logger.finest("Calling the powershell");
			Process p = new ProcessBuilder()
	                //.inheritIO()
	                .command("powershell", "-ImportSystemModules", psScriptsDir + "LYNCUpdateUser.ps1",
	                				 smAccountName,
									 audioVideoDisabled,
									 enabled,
									 enterpriseVoiceEnabled,
									 hostedVoiceMail,
									 lineURI,
									 lineServerURI,
									 privateLine,
									 remoteCallControlTelephonyEnabled,
									 sipAddress).start();
			
			logger.finer("Calling procedure to get the output of the process");
			List<String> linesList = getProcessOutput(p);
			
			logger.finest("Wait for process to finish");
			p.waitFor();
			
			logger.finer("Calling procedure to check errors in the process");
			success = checkProcessSucces(linesList);
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateuser - Unexpected error",e);
			
		}
		
		logger.exiting(className, "updateuser");
        return success;
	
	}
	
	
	
	/**
	 * Update an user in Lync
	 * @param psScriptsDir Directory where the powershells scripts resides
	 * @param smAccountName identity of the user to create
	 * @param audioVideoDisabled Flag for AudioVideo
	 * @param enabled Flag for Enable or disable the account
	 * @param domainController DomainController of the account
	 * @param enterpriseVoiceEnabled Flag for EnterpriseVoice
	 * @param hostedVoiceMail Flag for HostedVoice
	 * @param lineURI LineURI of the account
	 * @param lineServerURI LineServerURI of the account
	 * @param privateLine PrivateLine of the account
	 * @param remoteCallControlTelephonyEnabled Flag for
	 * @param sipAddress SipAddress in Lync for the account
	 * @return UserLync Object
	 */
	public static UserLync getUser (	String psScriptsDir,
										String smAccountName) {
		
		logger.entering(className, "getUser");
		UserLync userLync = null; 
		
		try {
			
			logger.finest("Calling the powershell");
			boolean success = false;
			Process p = new ProcessBuilder()
					//.inheritIO()
	                .command("powershell.exe", "-ImportSystemModules", psScriptsDir + "LYNCReconUser.ps1",
	                				 smAccountName).start();
			
			logger.finer("Calling procedure to get the output of the process");
			List<String> linesList = getProcessOutput(p);

			logger.finest("Wait for process to finish");
			p.waitFor();
			
			logger.finer("Calling procedure to check errors in the process");
			success = checkProcessSucces(linesList);
			
			if (success) {
				logger.finer("No error found. Continue with the reconciliation process");
				userLync = getUserLyncObject(linesList);
			}
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUser - Unexpected error",e);
			
		}
		
		logger.exiting(className, "getUser");
        return userLync;
	
	}
	
	
	
	/**
	 * Get the list of users in Lync
	 * @param psScriptsDir Directory where the powershells scripts resides
	 * @return List of Lync users
	 */
	public static List<UserLync> getUsers ( String psScriptsDir) {
		
		logger.entering(className, "getUsers");
		List<UserLync> userLyncList = new ArrayList<UserLync>(); 
		
		try {
			
			logger.finest("Calling the powershell");
			boolean success = false;
			Process p = new ProcessBuilder()
	                //.inheritIO()
	                .command("powershell", "-ImportSystemModules", psScriptsDir + "LYNCReconUser.ps1").start();
			
			logger.finer("Calling procedure to get the output of the process");
			List<String> linesList = getProcessOutput(p);
			
			logger.finest("Wait for process to finish");
			p.waitFor();
			
			logger.finer("Calling procedure to check errors in the process");
			success = checkProcessSucces(linesList);
			
			if (success) {
				logger.finer("No error found. Continue with the reconciliation process");
				for (int i = 2; i< linesList.size()-1; i++) {
					String line = linesList.get(i);
					if (line != null && line.startsWith("Identity")) {
						logger.finer("Identity found: " + line);
						List<String> userPropertyList = new ArrayList<String>();
						userPropertyList.add(line);
						logger.finer("Reading properties...");
						for (int j = i+1; j < linesList.size()-1; j++ ) {
							String property = linesList.get(j);
							if (property != null && property.startsWith("Identity")) {
								logger.finer("No more property found");
								i=j-1;
								break;
							}
							logger.finer("Property: " + property);
							userPropertyList.add(property);
						}
						
						logger.finer("Constructing user lync object");
						UserLync userLync = getUserLyncObject(userPropertyList);
						if ( userLync != null) {
							logger.finer("Adding the user: " + userLync.getSamAccountName() + " to the list.");
							userLyncList.add(userLync);
						}
					
					}
					
				}
			
			}
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsers - Unexpected error",e);
			
		}
		
		logger.exiting(className, "getUsers");
        return userLyncList;
	
	}
	
	
	
	/**
	 * Check for a process success
	 * @param p
	 * @return true if success. false if not.
	 */
	private static boolean checkProcessSucces(List<String> linesList) {
		
		logger.entering(className, "checkProcessSucces", linesList);
		boolean success = linesList.contains(SUCCESS_MSG);
		
		logger.finer("Looking for exceptions");
		if (!success) {
			logger.log(Level.SEVERE, "checkProcessSucces - Unexpected error: " + linesList);
		}
		
		logger.exiting(className, "checkProcessSucces", success);
		return success;
	}
	
	
	/**
	 * Get the output of a process
	 * @param p
	 * @return List of lines.
	 */
	private static List<String> getProcessOutput(Process p) {
		
		logger.entering(className, "getProcessOutput");
		List<String> linesList= new ArrayList<String>();
		
		try {
			
			logger.finest("Getting Standard Ouptput of the process");
			String line;
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
			  p.getInputStream()));
			
				while ((line = stdout.readLine()) != null) {
					logger.finest(line);
					linesList.add(line);
				}
			stdout.close();
	
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getProcessOutput - Unexpected error", e);
		}
		
		logger.exiting(className, "getProcessOutput", linesList);
		return linesList;
	}
	
	
	
	/**
	 * Get an User Lync Object
	 * @param linesList List of lines of the user reconciliated
	 * @return UserLync Object
	 */
	private static UserLync getUserLyncObject(List<String> linesList) {
		
		logger.entering(className, "getUserLyncObject");
		UserLync userLync = new UserLync();
		HashMap<String,String> userAttrMap = new HashMap<String,String>();
		
		logger.finest("Looping over lines of the output");
		for (int i = 0; i < linesList.size()-1; i++) {
			String line = linesList.get(i);
			logger.finest("Getting line #"+i+": " + line);
			if (line != null && !line.isEmpty()) {
			
				if (line.contains(":")) {
					logger.finest("Splitting the line with key/value");
					String[] entry = line.split(":",2);
					String key = entry[0].trim();
					String value = entry[1].trim();
					logger.finest("Key: " + key + " | Value: " + value);
					userAttrMap.put(key, value);
						
				} else {
					logger.finest("Getting the previous key to concatenete the value");
					String prevLine = linesList.get(i-1);
					if (prevLine != null && !prevLine.isEmpty()) {
						if (prevLine.contains(":")) {
							String[] entry = prevLine.split(":",2);
							String key = entry[0].trim();
							String value = entry[1].trim() + line.trim();
							logger.finest("Key: " + key + " | Value: " + value);
							userAttrMap.remove(key);
							userAttrMap.put(key, value);
							
						}
					}
				}
				
			}
		}
		
		logger.finest("User Attribute Map: " + userAttrMap.toString());
		userLync.setAudioVideoDisabled(Boolean.parseBoolean(userAttrMap.get("AudioVideoDisabled")));
		userLync.setRegistrarPool(userAttrMap.get("RegistrarPool"));
		userLync.setEnabled(Boolean.parseBoolean(userAttrMap.get("Enabled")));
		userLync.setEnterpriseVoiceEnabled(Boolean.parseBoolean(userAttrMap.get("EnterpriseVoiceEnabled")));
		userLync.setHostedVoiceMail(Boolean.parseBoolean(userAttrMap.get("HostedVoiceMail")));
		userLync.setIdentity(userAttrMap.get("Identity"));
		userLync.setLineServerURI(userAttrMap.get("LineServerURI"));
		userLync.setLineURI(userAttrMap.get("LineURI"));
		userLync.setPrivateLine(userAttrMap.get("PrivateLine"));
		userLync.setRemoteCallControlTelephonyEnabled(Boolean.parseBoolean(userAttrMap.get("RemoteCallControlTelephonyEnabled")));
		userLync.setSamAccountName(userAttrMap.get("SamAccountName"));
		userLync.setSipAddress(userAttrMap.get("SipAddress"));
		
		
		logger.exiting(className, "getUserLyncObject");
		return userLync;
		
	}
}
