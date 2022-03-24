package entel.oim.connectors.sau.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sau.OperationsSAU;
import entel.oim.connectors.sau.webservices.EliminarUsuarioServiceStub.*;


// Initiation
trace.ok("[Delete SAU] Attributes::"+attributes);


// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
def WSDL=configuration.getEliminarUsuarioSAUWSDL();
def AUTH=configuration.getAuthenticationDeleteUserSAU();
def USER_LOGIN = configuration.getUser();
def PASS_LOGIN = "";
GuardedString guard= configuration.getPassword()
if (guard != null ) {
guard.access(new GuardedString.Accessor()
	  {
		  public void access(char[] clearChars)
		  {
			  PASS_LOGIN = new String(clearChars);
		  }
	  }
	  );
}

// Get the info of the account
def UID = attributes.get("__UID__")!=null? attributes.get("__UID__").getValue().get(0).trim():null;
def LASTCHANGEBY = attributes.get("LASTCHANGEBY")!=null? attributes.get("LASTCHANGEBY").getValue().get(0).trim():null;


// Call webservice	
entel.oim.connectors.sau.webservices.eliminarusuario.ResponseEliminarUsuarioType response = 
    OperationsSAU.deleteUser(  "$WEBPROTOCOL",
                            	 "$WEBHOST",
                            	 "$WEBPORT",
                            	 "$WSDL",
                               "$USER_LOGIN",
                            	 "$PASS_LOGIN",
                               "$AUTH",
                               "$LASTCHANGEBY",
                               "$UID");
  
// Checking response                                	
if (response == null){

	//Error
  // Throw exception
  throw new ConnectorException("[Delete SAU] Error: Unexpected error calling to the webservice");

} else if (response.getE_codigo() == "0000") {

  trace.info("[Delete SAU] Deleted User: $UID");

  
} else {
   
  // Error
  // Throw exception
  throw new ConnectorException("[Delete SAU] Error: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());

}					
