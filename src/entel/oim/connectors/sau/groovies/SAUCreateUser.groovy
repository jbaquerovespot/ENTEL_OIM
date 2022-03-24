package entel.oim.connectors.sau.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sau.OperationsSAU;
import entel.oim.connectors.sau.webservices.CrearUsuarioServiceStub.*;


// Initiation
trace.ok("[Create SAU] Attributes::"+attributes);
System.out.println(attributes);

// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
def WSDL=configuration.getCrearUsuarioSAUWSDL();
def AUTH=configuration.getAuthenticationCreateUserSAU();
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
def LASTCHANGEBY = attributes.get("LASTCHANGEBY")!=null? attributes.get("LASTCHANGEBY").getValue().get(0).trim():null;
def ACCOUNT = attributes.get("__NAME__")!=null? attributes.get("__NAME__").getValue().get(0).trim():null;
def PASSWORD = attributes.get("__PASSWORD__")!=null? attributes.get("__PASSWORD__").getValue().get(0):null;
def USERTYPE = attributes.get("USERTYPE")!=null? attributes.get("USERTYPE").getValue().get(0).trim():null;
def COMPANYTYPE = attributes.get("COMPANYTYPE")!=null? attributes.get("COMPANYTYPE").getValue().get(0).trim():null;
def NAMES = attributes.get("NAMES")!=null? attributes.get("NAMES").getValue().get(0).trim():null;
def LASTNAME = attributes.get("LASTNAME")!=null? attributes.get("LASTNAME").getValue().get(0).trim():null;
def RUT = attributes.get("RUT")!=null? attributes.get("RUT").getValue().get(0).trim():null;
def EMAIL = attributes.get("EMAIL")!=null? attributes.get("EMAIL").getValue().get(0).trim():null;

def PASSWORD_CLEAR = "";
if (PASSWORD != null ) {
PASSWORD.access(new GuardedString.Accessor()
	  {
		  public void access(char[] clearChars)
		  {
			  PASSWORD_CLEAR = new String(clearChars);
		  }
	  }
	  );
}                                                                                                                          


// Call webservice	
entel.oim.connectors.sau.webservices.crearusuario.ResponseCrearUsuarioType response = 
    OperationsSAU.createUser(  "$WEBPROTOCOL",
                            	 "$WEBHOST",
                            	 "$WEBPORT",
                            	 "$WSDL",
                            	 "$USER_LOGIN",
                            	 "$PASS_LOGIN",
                               "$AUTH",
                               "$LASTCHANGEBY", 
                               "$ACCOUNT",
                        			 "$PASSWORD_CLEAR",
    				                   "$USERTYPE",
                        			 "$COMPANYTYPE",
                        			 "$NAMES",
                        			 "$LASTNAME",
                        			 "$RUT",
                        			 "$EMAIL",
                        			 "1"); // Activo
  
// Checking response                                	
if (response == null){

	//Error
  // Throw exception
  throw new ConnectorException("[Create SAU] Error: Unexpected error calling to the webservice");

} else if (response.getE_codigo() == "0000") {

  trace.info("[Create SAU] Created User: $ACCOUNT");

  //Return Uid from the script
  return new Uid(ACCOUNT);
  
} else {
   
  // Error
  // Throw exception
  throw new ConnectorException("[Create SAU] Error: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());

}					
