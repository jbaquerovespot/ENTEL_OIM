package entel.oim.connectors.lync.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.lync.OperationsLync;


// Initiation
trace.ok("[Update Lync] Attributes::"+attributes);
System.out.println(attributes);

// Get the parameters
def PS_DIR = configuration.getPsDir();

// Get the info of the account
def SAMACCOUNTNAME = attributes.get("__UID__")!=null? attributes.get("__UID__").getValue().get(0).trim():null;
def AUDIOVIDEODISABLED = attributes.get("AUDIOVIDEODISABLED")!=null? attributes.get("AUDIOVIDEODISABLED").getValue().get(0):null;
def ENABLED = attributes.get("__ENABLED__")!=null? attributes.get("__ENABLED__").getValue().get(0):null;
def ENTERPRISEVOICEENABLED = attributes.get("ENTERPRISEVOICEENABLED")!=null? attributes.get("ENTERPRISEVOICEENABLED").getValue().get(0):null;
def HOSTEDVOICEMAIL = attributes.get("HOSTEDVOICEMAIL")!=null? attributes.get("HOSTEDVOICEMAIL").getValue().get(0):null;
def LINEURI = attributes.get("LINEURI")!=null? attributes.get("LINEURI").getValue().get(0).trim():null;
def LINESERVERURI = attributes.get("LINESERVERURI")!=null? attributes.get("LINESERVERURI").getValue().get(0).trim():null;
def PRIVATELINE = attributes.get("PRIVATELINE")!=null? attributes.get("PRIVATELINE").getValue().get(0).trim():null;
def REMOTECALLCONTROLTELEPHONYENABLED = attributes.get("REMOTECALLCONTROLTELEPHONYENABLED")!=null? attributes.get("REMOTECALLCONTROLTELEPHONYENABLED").getValue().get(0):null;
def SIPADDRESS = attributes.get("SIPADDRESS")!=null? attributes.get("SIPADDRESS").getValue().get(0).trim():null;

// Call powershell
boolean success = 	  OperationsLync.updateUser(  "$PS_DIR",
												  "$SAMACCOUNTNAME",
												  "$AUDIOVIDEODISABLED",
												  "$ENABLED",
												  "$ENTERPRISEVOICEENABLED",
												  "$HOSTEDVOICEMAIL",
												  "$LINEURI",
												  "$LINESERVERURI",
												  "$PRIVATELINE",
												  "$REMOTECALLCONTROLTELEPHONYENABLED",
												  "$SIPADDRESS");
										   
// Checking response
if (!success){
 
	 //Error
   // Throw exception
   throw new ConnectorException("[Update Lync] Error: Unexpected error calling to the powershell");
 
} else {
 
   trace.info("[Update Lync] Updated User: $UID");
 
   
}
