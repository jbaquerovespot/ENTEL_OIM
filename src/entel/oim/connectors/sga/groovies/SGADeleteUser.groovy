package entel.oim.connectors.sga.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sga.OperationsSGA;


// Initiation
trace.ok("[Delete SGA] Attributes::"+attributes);


// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
def WSDL=configuration.getEliminarCuentasUsuarioSGAWSDL();
def USER_LOGIN = configuration.getUser();
def PASS_LOGIN = "";
GuardedString guard= configuration.getPassword()
guard.access(new GuardedString.Accessor()
	  {
		  public void access(char[] clearChars)
		  {
			  PASS_LOGIN = new String(clearChars);
		  }
	  }
	  );

// Get the info of the account
def UID = attributes.get("__UID__")!=null? attributes.get("__UID__").getValue().get(0).trim():null;


// Call webservice	
entel.oim.connectors.sga.webservices.eliminarcuentasusuario.EliminarCuentasUsuarioSGAResponseType response = 
    OperationsSGA.deleteUser(  "$WEBPROTOCOL",
                            	 "$WEBHOST",
                            	 "$WEBPORT",
                            	 "$WSDL",
                            	 "$USER_LOGIN",
                            	 "$PASS_LOGIN",
                               "$UID");
  
// Checking response                                	
if (response == null){

	//Error
  // Throw exception
  throw new ConnectorException("[Delete SGA] Error: Unexpected error calling to the webservice");

} else if (response.getCodigo() == "0000") {

  trace.info("[Delete SGA] Deleted User: $UID");

  
} else {
   
  // Error
  // Throw exception
  throw new ConnectorException("[Delete SGA] Error: Webservice error code: "+ response.getCodigo() + "Webservice error message: "+response.getDescripcion());

}					
