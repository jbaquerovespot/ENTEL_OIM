package entel.oim.connectors.lync.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.lync.OperationsLync;


// Initiation
trace.ok("[Create Lync] Attributes::"+attributes);
System.out.println(attributes);

// Get the parameters

def PS_DIR = configuration.getPsDir();
def SIPDOMAIN = configuration.getSipDomain();
def SIPADDRESSTYPE = configuration.getSipAddressType();

// Get the info of the account
def SAMACCOUNTNAME = attributes.get("__NAME__")!=null? attributes.get("__NAME__").getValue().get(0).trim():null;
def REGISTRARPOOL = attributes.get("REGISTRARPOOL")!=null? attributes.get("REGISTRARPOOL").getValue().get(0).trim():null;


// Call powershell
boolean success = 	  OperationsLync.createUser(  "$PS_DIR",
												  "$SAMACCOUNTNAME",
												  "$REGISTRARPOOL",
												  "$SIPDOMAIN",
												  "$SIPADDRESSTYPE");
										   
// Checking response
if (!success){
 
	 //Error
   // Throw exception
   throw new ConnectorException("[Create Lync] Error: Unexpected error calling to the powershell");
 
} else {
 
   trace.info("[Create Lync] Created User: $SAMACCOUNTNAME");

  //Return Uid from the script
  return new Uid(SAMACCOUNTNAME);
 
   
}
