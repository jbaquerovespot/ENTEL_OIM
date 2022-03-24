package entel.oim.connectors.sga.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sga.OperationsSGA;


// Initiation
trace.ok("[Update SGA] Attributes::"+attributes);
System.out.println(attributes);

// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
def WSDL=configuration.getModificarCuentasUsuarioSGAWSDL();
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
def PLATFORM = attributes.get("PLATFORM")!=null? attributes.get("PLATFORM").getValue().get(0).trim():null;
def RUT = attributes.get("RUT")!=null? attributes.get("RUT").getValue().get(0).trim():null;
def LIC_TYPE = attributes.get("LIC_TYPE")!=null? attributes.get("LIC_TYPE").getValue().get(0).trim():null;
def FULLNAME = attributes.get("FULLNAME")!=null? attributes.get("FULLNAME").getValue().get(0).trim():null;
def EMAIL = attributes.get("EMAIL")!=null? attributes.get("EMAIL").getValue().get(0).trim():null;
def GENESYSLOGIN = attributes.get("GENESYSLOGIN")!=null? attributes.get("GENESYSLOGIN").getValue().get(0).trim():null;
def PHONE = attributes.get("PHONE")!=null? attributes.get("PHONE").getValue().get(0).trim():null;
def ENABLE = attributes.get("__ENABLE__")!=null? attributes.get("__ENABLE__").getValue().get(0):null;

//Check status account
def BACKSTATUS;
if (ENABLE) {
   BACKSTATUS = "Activo";
} else if (ENABLE == null) {
   BACKSTATUS = "null";
} else {
   BACKSTATUS = "Inactivo";
}


// Call webservice	
entel.oim.connectors.sga.webservices.modificarcuentasusuario.ModificarCuentasUsuarioSGAResponseType response = 
    OperationsSGA.updateUser(  "$WEBPROTOCOL",
                            	 "$WEBHOST",
                            	 "$WEBPORT",
                            	 "$WSDL",
                            	 "$USER_LOGIN",
                            	 "$PASS_LOGIN",
                               "$UID", 
                               "$PLATFORM",
                        			 "$RUT",
    				                   "$LIC_TYPE",
                        			 "$FULLNAME",
                        			 "$EMAIL",
                        			 "$BACKSTATUS",
                        			 "$GENESYSLOGIN",
                        			 "$PHONE");
  
// Checking response                                	
if (response == null){

	//Error
  // Throw exception
  throw new ConnectorException("[Update SGA] Error: Unexpected error calling to the webservice");

} else if (response.getCodigo() == "0000") {

  trace.info("[Update SGA] Update User: $UID");

  
} else {
   
  // Error
  // Throw exception
  throw new ConnectorException("[Update SGA] Error: Webservice error code: "+ response.getCodigo() + "Webservice error message: "+response.getDescripcion());

}					
