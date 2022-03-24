package entel.oim.connectors.lync.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.lync.OperationsLync;


// Initiation
trace.ok("[Delete Lync] Attributes::"+attributes);


// Get the parameters
def PS_DIR = configuration.getPsDir();

// Get the info of the account
def UID = attributes.get("__UID__")!=null? attributes.get("__UID__").getValue().get(0).trim():null;


// Call powershell
boolean success = 	  OperationsLync.deleteUser( "$PS_DIR",
												 "$UID");
										  
// Checking response                                	
if (!success){

	//Error
  // Throw exception
  throw new ConnectorException("[Delete Lync] Error: Unexpected error calling to the powershell");

} else {

  trace.info("[Delete Lync] Deleted User: $UID");

  
}				
