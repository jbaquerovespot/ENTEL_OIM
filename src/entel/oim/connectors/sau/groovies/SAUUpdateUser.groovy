package entel.oim.connectors.sau.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sau.OperationsSAU;
import entel.oim.connectors.sau.UtilitiesSAU;


// Initiation
trace.ok("[Update SAU] Attributes::"+attributes);
System.out.println(attributes);


// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
def AUTH=configuration.getAuthenticationUpdateUserSAU();
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
def OIMURL = configuration.getOimUrl();
def OIMSCHEME = configuration.getOimScheme();
def OIMUSER = configuration.getOimUser();
def OIMPASS = configuration.getOimPass();


// Get the info of the account
def UID = attributes.get("__UID__")!=null? attributes.get("__UID__").getValue().get(0).trim():null;
def LASTCHANGEBY = attributes.get("LASTCHANGEBY")!=null? attributes.get("LASTCHANGEBY").getValue().get(0).trim():null;
def PASSWORD = attributes.get("__PASSWORD__")!=null? attributes.get("__PASSWORD__").getValue().get(0):null;
def USERTYPE = attributes.get("USERTYPE")!=null? attributes.get("USERTYPE").getValue().get(0).trim():null;
def COMPANYTYPE = attributes.get("COMPANYTYPE")!=null? attributes.get("COMPANYTYPE").getValue().get(0).trim():null;
def NAMES = attributes.get("NAMES")!=null? attributes.get("NAMES").getValue().get(0).trim():null;
def LASTNAME = attributes.get("LASTNAME")!=null? attributes.get("LASTNAME").getValue().get(0).trim():null;
def RUT = attributes.get("RUT")!=null? attributes.get("RUT").getValue().get(0).trim():null;
def EMAIL = attributes.get("EMAIL")!=null? attributes.get("EMAIL").getValue().get(0).trim():null;
def BLOCKED = attributes.get("BLOCKED")!=null? attributes.get("BLOCKED").getValue().get(0):null;
def INACTIVE = attributes.get("INACTIVE")!=null? attributes.get("INACTIVE").getValue().get(0):null;
def ENABLE = attributes.get("__ENABLE__")!=null? attributes.get("__ENABLE__").getValue().get(0):null;
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


if (LASTCHANGEBY != null || USERTYPE != null || COMPANYTYPE != null || NAMES != null || LASTNAME != null || RUT != null || EMAIL != null || BLOCKED != null || INACTIVE != null) {

  trace.ok("[Update SAU] Calling to database with UID: " + UID);
  HashMap<String,String> userInfo = UtilitiesSAU.findUserAccount(UID, OIMURL, OIMSCHEME, OIMUSER, OIMPASS);
  
  // Get the info of the account
  LASTCHANGEBY = attributes.get("LASTCHANGEBY")!=null? attributes.get("LASTCHANGEBY").getValue().get(0).trim():userInfo.get("LASTCHANGEBY");
  USERTYPE = attributes.get("USERTYPE")!=null? attributes.get("USERTYPE").getValue().get(0).trim():userInfo.get("USERTYPE");
  COMPANYTYPE = attributes.get("COMPANYTYPE")!=null? attributes.get("COMPANYTYPE").getValue().get(0).trim():userInfo.get("COMPANYTYPE");
  NAMES = attributes.get("NAMES")!=null? attributes.get("NAMES").getValue().get(0).trim():userInfo.get("NAMES");
  LASTNAME = attributes.get("LASTNAME")!=null? attributes.get("LASTNAME").getValue().get(0).trim():userInfo.get("LASTNAME");
  RUT = attributes.get("RUT")!=null? attributes.get("RUT").getValue().get(0).trim():userInfo.get("RUT");
  EMAIL = attributes.get("EMAIL")!=null? attributes.get("EMAIL").getValue().get(0).trim():userInfo.get("EMAIL");
  
  
  // Get WSDL of the webservice
  def WSDL=configuration.getActualizarUsuarioSAUWSDL();
  
  // Call webservice	
  entel.oim.connectors.sau.webservices.actualizaratributosusr.ResponseActualizarUsuarioType response = 
      OperationsSAU.updateUser(  "$WEBPROTOCOL",
                              	 "$WEBHOST",
                              	 "$WEBPORT",
                              	 "$WSDL",
                              	 "$USER_LOGIN",
                              	 "$PASS_LOGIN",
                                 "$AUTH",
                                 "$LASTCHANGEBY", 
                                 "$UID",
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
    throw new ConnectorException("[Update SAU] Error: Unexpected error calling to the webservice");
  
  } else if (response.getE_codigo() == "0000") {
  
    trace.info("[Update SAU] Update User: $UID");
  
    
  } else {
     
    // Error
    // Throw exception
    throw new ConnectorException("[Update SAU] Error: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());
  
  }
  
  
}

  
if (PASSWORD != null) {

    trace.ok("[Update SAU] Calling reset password with UID: " + UID);

    // Get WSDL of the webservice
    def WSDL=configuration.getResetPasswordSAUWSDL();
  
    // Call webservice
    entel.oim.connectors.sau.webservices.reiniciarpassword.ResponseResetPasswordType response = 
    OperationsSAU.resetPasswordUser( "$WEBPROTOCOL",
                                  	 "$WEBHOST",
                                  	 "$WEBPORT",
                                  	 "$WSDL",
                                  	 "$USER_LOGIN",
                                  	 "$PASS_LOGIN",
                                     "$AUTH",
                                     "$UID",
                              			 "$PASSWORD_CLEAR");
  
  
    // Checking response                                	
    if (response == null){
    
    	//Error
      // Throw exception
      throw new ConnectorException("[Update SAU] Error: Unexpected error calling to the webservice");
    
    } else if (response.getE_codigo() == "0000") {
    
      trace.info("[Update SAU] Update User Password: $UID");
      
    } else {
       
      // Error
      // Throw exception
      throw new ConnectorException("[Update SAU] Error: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());
    
    }  
    
}



if (ENABLE != null) {


  if (ENABLE) {
  
    trace.ok("[Update SAU] Calling to enable user with UID: " + UID);

    // Get WSDL of the webservice
    def WSDL=configuration.getHabilitarUsuarioSAUWSDL();
  
    // Call webservice
    entel.oim.connectors.sau.webservices.habilitarusuario.ResponseHabilitarUsuarioType response = 
    OperationsSAU.enableUser( "$WEBPROTOCOL",
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
      throw new ConnectorException("[Update SAU] Error: Unexpected error calling to the webservice");
    
    } else if (response.getE_codigo() == "0000") {
    
      trace.info("[Update SAU] Enabled User: $UID");
      
    } else {
       
      // Error
      // Throw exception
      throw new ConnectorException("[Update SAU] Error: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+responsePass.getE_mensaje());
    
    }  
    
    
  } else {
  
    trace.ok("[Update SAU] Calling to disable user with UID: " + UID);
    
    // Get WSDL of the webservice
    def WSDL=configuration.getDeshabilitarUsuarioSAUWSDL();
  
    // Call webservice
    entel.oim.connectors.sau.webservices.deshabilitarusuario.ResponseDeshabilitarUsuarioType response = 
    OperationsSAU.disableUser( "$WEBPROTOCOL",
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
      throw new ConnectorException("[Update SAU] Error: Unexpected error calling to the webservice");
    
    } else if (response.getE_codigo() == "0000") {
    
      trace.info("[Update SAU] Disabled User: $UID");
      
    } else {
       
      // Error
      // Throw exception
      throw new ConnectorException("[Update SAU] Error: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+responsePass.getE_mensaje());
    
    }
  
  }
  
}


					
