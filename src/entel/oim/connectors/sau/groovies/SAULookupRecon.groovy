package entel.oim.connectors.sau.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sau.OperationsSAU;


// Initiation
trace.info("[Lookup SAU] Lookup Recon timing:"+ timing);
trace.info("[Lookup SAU] Attributes to Get:"+ ATTRS_TO_GET);


// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
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

	
if (timing.equals("executeQuery:COMPANIES")) {
	
	trace.info("[Lookup SAU] Getting companies.");
  String codekey =  ATTRS_TO_GET[0];
	String decodekey = ATTRS_TO_GET[1];
 
  // Getting the wsdl
  def WSDL=configuration.getObtenerCompaniasSAUWSDL();
  def AUTH=configuration.getAuthenticationLookupCompanySAU();
	
	// Call webservice	
  entel.oim.connectors.sau.webservices.obtenercompaniasall.ResponseObtenerCompaniasType response = 
      OperationsSAU.lookupCompanies("$WEBPROTOCOL",
                                  	"$WEBHOST",
                                  	"$WEBPORT",
                                  	"$WSDL",
                                  	"$USER_LOGIN",
                                  	"$PASS_LOGIN",
                                    "$AUTH");
  
  // Checking response                                	
	if (response == null){

		//Error
    // Throw exception
    throw new ConnectorException("[Lookup SAU] Error COMPANIES: Unexpected error calling to the webservice");
	
  } else if (response.getE_codigo() != "0000") {

    // Error
    // Throw exception
    throw new ConnectorException("[Lookup SAU] Error COMPANIES: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());
 
  } else {
     
    // Getting the company list 
    entel.oim.connectors.sau.webservices.obtenercompaniasall.Compania[] detArray = response.getE_companias();
  
    // loop over companies
    for (int i=0; i < detArray.length ; i++) {
					
      // Get the current company   
      entel.oim.connectors.sau.webservices.obtenercompaniasall.Compania det = detArray[i];
      trace.info("[Lookup SAU] Company found: " + det.getE_idcompania() + " - " + det.getE_descripcion());
		
      util.addAttribute(codekey,det.getE_idcompania());
      util.addAttribute(decodekey,det.getE_descripcion());
      if (!util.build()) return;
		} 
  
    trace.info("[Lookup SAU] COMPANIES were loaded sucessfully.");
    
  }

}



if (timing.equals("executeQuery:USERTYPES")) {
	
	trace.info("[Lookup SAU] Getting usertypes.");
  String codekey =  ATTRS_TO_GET[0];
	String decodekey = ATTRS_TO_GET[1];

		
  util.addAttribute(codekey,"51");
  util.addAttribute(decodekey,"PERSONA");
  if (!util.build()) return;
  
  
  util.addAttribute(codekey,"52");
  util.addAttribute(decodekey,"SERVICIO");
  if (!util.build()) return;
  
  trace.info("[Lookup SAU] USERTYPES were loaded sucessfully.");
    
}




if (timing.equals("executeQuery:PROFILES")) {
	
	trace.info("[Lookup SAU] Getting profiles.");
  String codekey =  ATTRS_TO_GET[0];
	String decodekey = ATTRS_TO_GET[1];
 
  // Getting the wsdl
  def WSDL=configuration.getBuscarPerfilesSAUWSDL();
  def AUTH=configuration.getAuthenticationLookupProfileSAU();
	
	// Call webservice	
  entel.oim.connectors.sau.webservices.buscarperfiles.ResponseBuscarPerfilesType response = 
      OperationsSAU.lookupProfiles("$WEBPROTOCOL",
                                  	"$WEBHOST",
                                  	"$WEBPORT",
                                  	"$WSDL",
                                  	"$USER_LOGIN",
                                  	"$PASS_LOGIN",
                                    "$AUTH");
  
  // Checking response                                	
	if (response == null){

		//Error
    // Throw exception
    throw new ConnectorException("[Lookup SAU] Error PROFILES: Unexpected error calling to the webservice");
	
  } else if (response.getE_codigo() != "0000") {

    // Error
    // Throw exception
    throw new ConnectorException("[Lookup SAU] Error PROFILES: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());
 
  } else {
     
    // Getting the profiles list 
    entel.oim.connectors.sau.webservices.buscarperfiles.Perfiles[] detArray = response.getE_perfiles();
  
    // loop over profiles
    for (int i=0; i < detArray.length ; i++) {
					
      // Get the current profile   
      entel.oim.connectors.sau.webservices.buscarperfiles.Perfiles det = detArray[i];
      trace.info("[Lookup SAU] Company found: " + decodekey,det.getE_aplicacion() + " -> " + det.getE_nombre());
		
      util.addAttribute(codekey,det.getE_idaplicacion()+";"+det.getE_idperfil());
      util.addAttribute(decodekey,det.getE_aplicacion() + " -> " + det.getE_nombre());
      if (!util.build()) return;
		} 
  
    trace.info("[Lookup SAU] PROFILES were loaded sucessfully.");
    
  }

}
	
 
