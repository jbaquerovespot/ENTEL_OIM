package entel.oim.migration;

import java.io.BufferedReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import entel.oim.connectors.lync.UserLync;
import entel.oim.connectors.utilities.SuccessFactorsConnection;

public class Prueba {

	private final static String SUCCESS_MSG = "POWERSHELL COMPLETED SUCCESSFULLY";
	
	public Prueba() {
		// TODO Auto-generated constructor stub
	}
	
	public static String toFormatRut(String rut, boolean sepRut, boolean sepDigVer) {
		
		String value = rut; 
		
		value = value.replace(".", "").replace("-", "");

		value = value.replaceFirst ("^0*", "");
		
		
		if (sepRut) {
			StringBuilder str = new StringBuilder(value);
			int idx = (str.length()-1) - 3;

			while (idx > 0)
			{
			    str.insert(idx, ".");
			    idx = idx - 3;
			}
			
			value = str.toString();
		}
		
		if (sepDigVer) {
			value = value.substring(0, value.length()-1) + "-" + value.charAt(value.length()-1);
		}
		
		return value;
	}

	public static void main(String[] args) throws Exception {
		
		
		/*
		String hola="";
		System.out.println(hola.isEmpty());
		updateTemplateExcel("D:\\Customers\\Entel\\Documents\\prueba.xlsx");
		
		
		getUser( "hostURL",
				"userLogin",
				"123456",
				"C:\\Users\\danferre\\Downloads\\",
				"DANFERRE_CL");
		*/
		
		 Map<Integer, String> unsortMap = new HashMap<Integer, String>();
	        unsortMap.put(1, "z");
	        unsortMap.put(2, "b");
	        unsortMap.put(3, "A");
	        unsortMap.put(7, "c");
	        unsortMap.put(9, "d");
	        unsortMap.put(0, "E");
	        unsortMap.put(4, "y");
	        unsortMap.put(10, "N");
	        unsortMap.put(5, "j");
	        unsortMap.put(8, "m");
	        unsortMap.put(11, "f");
	        
	        System.out.println("Unsorted: " + unsortMap);
	        
	        HashMap<Integer, String> sorted = unsortMap
					.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

		
	        System.out.println("sorted: " + sorted);
	        
	        for (Map.Entry me : sorted.entrySet()) {
	        	System.out.println("Adding lookup-> Key: "+me.getKey() + " & Value: " + me.getValue());
		    }
	}
	
		
	
	public static boolean getUser (	String host,
			String user,
			String password,
			String psScriptsDir,
			String identity) {

		try {
		
		
			// Log
			boolean success = false;
			String line;
			
			System.out.println("Calling the powershell");
			Process p = new ProcessBuilder()
									//.inheritIO()
									.command("powershell", "-ImportSystemModules", psScriptsDir + "Prueba.ps1",
											 host,
											 user,
											 password,
											 identity).start();
			
			System.out.println("Wait for process to finish");
			p.waitFor();
			
			System.out.println("Getting output of the process");
			List<String> linesList = getProcessOutput(p);
			
			System.out.println("Checking error i nthe process");
			success = checkProcessSucces(linesList);
			
			if (success) {
				getUserLyncObject(linesList);
			}
			
			
			System.out.println("Success: " + success);
			return success;
		
		} catch (Exception e) {
		
			// Unexpected exception
			e.printStackTrace();
			
			// Return
			System.out.println("Success: " + false);
			return false;

		}
	}
	
	
	/**
	 * Check for a process success
	 * @param p
	 * @return true if success. false if not.
	 */
	private static boolean checkProcessSucces(List<String> linesList) {
		
		boolean success = linesList.contains(SUCCESS_MSG);
		
		System.out.println("Looking for exceptions");
		if (!success) {
			System.out.println("checkProcessSucces - Unexpected error: " + linesList);
		}
		
		System.out.println(success);
		return success;
	}
	
	
	/**
	 * Get the output of a process
	 * @param p
	 * @return List of lines.
	 */
	private static List<String> getProcessOutput(Process p) {
		
		List<String> linesList= new ArrayList<String>();
		String line;
		
		try {
			
			System.out.println("Getting Standard Ouptput of the process");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
			  p.getInputStream()));
			
				while ((line = stdout.readLine()) != null) {
					System.out.println(line);
					linesList.add(line);
				}
			stdout.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("getProcessOutput - Unexpected error:" + e);
		}
		
		return linesList;
	}
	

	
	/**
	 * Get an User Lync Object
	 * @param linesList List of lines of the user reconciliated
	 * @return UserLync Object
	 */
	private static UserLync getUserLyncObject(List<String> linesList) {
		
		UserLync userLync = new UserLync();
		HashMap<String,String> userAttrMap = new HashMap<String,String>();
		System.out.println("Looping over lines of the output");
		for (int i = 0; i < linesList.size()-1; i++) {
			
			String line = linesList.get(i);
			System.out.println("Getting line #"+i+": " + line);
			if (line != null && !line.isEmpty()) {
			
				if (line.contains(":")) {
					
					String[] entry = line.split(":",2);
					String key = entry[0].trim();
					String value = entry[1].trim();
					userAttrMap.put(key, value);
						
				} else {
					
					String prevLine = linesList.get(i-1);
					if (prevLine != null && !prevLine.isEmpty()) {
						
						if (prevLine.contains(":")) {
							
							String[] entry = prevLine.split(":",2);
							String key = entry[0].trim();
							String value = entry[1].trim();
							userAttrMap.remove(key);
							userAttrMap.put(key, value + line.trim());
							
						}
					}
					
				}
				
			}
		}
		
		System.out.println("User Attribute Map: " + userAttrMap.toString());
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
		

		return userLync;
		
	}
	
	
	
}
	
	


