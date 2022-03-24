package entel.oim.migration;

import java.util.HashMap;
import java.util.Hashtable;
import javax.security.auth.login.LoginException;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import oracle.iam.platform.OIMClient;
import java.sql.*;
import java.io.PrintWriter;


public class PasswordMgmtPS2 {
	
	private OIMClient oimClient = null;
	private tcITResourceInstanceOperationsIntf itResOps = null;
	private final String IT_RESOURCE_NAME= "OIM Migration 12c";
	private final String PASSWORD_FILE = "/home/oraapp/oimmigration12c/claves.txt";

	public static void main(String[] args) {
		PasswordMgmtPS2 it = new PasswordMgmtPS2();
		it.connectOIM();
		it.searchUsersPasswords();
		it.disconnectOIM();
	}

	public void disconnectOIM() {
		if (oimClient != null) {
			oimClient.logout();
		}
	}

	public void connectOIM() {
		if (oimClient == null) {
			Hashtable<Object, Object> env = new Hashtable<>();
			env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");

			env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, "t3://vm1453.entel.cl:14000");

			System.setProperty("java.security.auth.login.config",
					"/DATA/u01/app/oracle/product/fmw/Oracle_IDM1/server/config/authwl.conf");
			System.setProperty("OIM.AppServerType", "wls");
			System.setProperty("APPSERVER_TYPE", "wls");
			oimClient = new oracle.iam.platform.OIMClient(env);
			try {
				oimClient.login("xelsysadm", "Entel2015".toCharArray()); // Update
				System.out.print("Successfully Connected with OIM ");
			} catch (LoginException e) {
				System.out.print("Login Exception" + e);
			}
		}

		if (itResOps == null) {
			itResOps = oimClient.getService(tcITResourceInstanceOperationsIntf.class);
		}
	}
	 
	 
	 
	/**
	 * Search the password of all users in OIM PS2
	 *  @author Oracle
	 */
	public void searchUsersPasswords() {
	 
		try{
		 	
			System.out.println("-------- Oracle JDBC Connection Testing ------");
		 	try {
		 		Class.forName("oracle.jdbc.driver.OracleDriver");
		 	} catch (ClassNotFoundException e) {
		 		System.out.println("Where is your Oracle JDBC Driver?");
	        	e.printStackTrace();
	        	return;
		 	}
		 	System.out.println("Oracle JDBC Driver Registered!");
		 	
		 	
		 	
		 	System.out.println("Trying connection!");
		 	Connection connection = null;
		 	try {
		 		connection = DriverManager.getConnection(
	                      "jdbc:oracle:thin:@10.85.44.11:1521/IAMdes.entel.cl", "END_OIM", "EntelD2015");
		 	} catch (SQLException e) {
		 		System.out.println("Connection Failed! Check output console");
	            e.printStackTrace();
	            return;
		 	}
	  
		 	if (connection != null) {
		 		System.out.println("You made it, take control your database now!");
  
		 		Statement stmtSelect = null;
		 		Statement stmtUpdate = null;
		 		String query = "select USR_LOGIN, USR_PASSWORD from USR where usr_login in ('XUS_PMIGRACION', 'AABARRA')";
		 		try {
		 			stmtSelect = connection.createStatement();
		 			ResultSet rs = stmtSelect.executeQuery(query);
		 			PrintWriter writer = new PrintWriter(PASSWORD_FILE, "UTF-8");
		 			while (rs.next()) {
		 				String usr_login = rs.getString("USR_LOGIN");
		 				String usr_password = rs.getString("USR_PASSWORD");
		 				System.out.println(usr_login + "\t" + usr_password);
      
		 				String updateItResource = "update svp set svp_field_value = '"+usr_password+"' where (svp.svr_key,svp.spd_key) in ( " +
                                  "select svr.svr_key, spd.spd_key " +
                                   " from svr  " +
                                   " join svd " +
                                   "   on svd.svd_key= svr.svd_key  " +
                                   " join spd " +
                                   "   on spd.svd_key= svd.svd_key  " +
                                   " where svr.svr_name= 'OIM Migration 12c' " +
                                   "  and spd_field_name='Key' )";
                                   
                        // execute insert SQL stetement
		 				stmtUpdate = connection.createStatement();
		 				stmtUpdate.executeUpdate(updateItResource);
      
		 				// Print the clear password
		 				String clear_usr_password = getITResourcePasswordDetails();
		 				writer.println(usr_login + ";"+clear_usr_password );
		 			}
		 			writer.close();
      
		 		} catch (SQLException e ) {
		 			e.printStackTrace();
		 		} finally {
		 			if (stmtSelect != null) { 
		 				stmtSelect.close(); 
		 				stmtUpdate.close(); 
                        connection.close();
                      	}
		 		}
  
		 	} else {
		 		System.out.println("Failed to make connection!");
		 	}
	    
		} catch (Exception e ) {
			e.printStackTrace();
	    }
	}

		
	 
	 /**
	  * Get the clear password of the dummy IT resource
	  * @return Password of the IT Resource
	  */
	 public String getITResourcePasswordDetails() {

			
	    String result = null;
	    
	    try {
				HashMap<String, String> searchcriteria = new HashMap<>();
				searchcriteria.put("IT Resources.Name", IT_RESOURCE_NAME);

				tcResultSet trs = itResOps.findITResourceInstances(searchcriteria);
				int rowCount = trs.getRowCount();
				long itResourceKey = -1;

				if (rowCount > 0) {
					trs.goToRow(0);
					itResourceKey = trs.getLongValue("IT Resource.Key");
				}

				//Map<String, String> itAttrib = new HashMap<>();
				if (itResourceKey > 0) {
					tcResultSet paramsRs = itResOps.getITResourceInstanceParameters(itResourceKey);
					for (int i = 0; i < paramsRs.getRowCount(); i++) {
						paramsRs.goToRow(i);
	          String name = paramsRs.getStringValue("IT Resources Type Parameter.Name");
	          if (name.equals("Key")) {
	            String value = paramsRs.getStringValue("IT Resources Type Parameter Value.Value");
	            System.out.println(name + ": " + value);
	            result = value;
	          }
	        		}
				}
			} catch (tcAPIException | tcColumnNotFoundException | tcITResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   
	   return result;

		}
	}

