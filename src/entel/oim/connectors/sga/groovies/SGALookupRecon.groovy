package entel.oim.connectors.sga.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sga.OperationsSGA;

// Initiation
trace.info("[Lookup SGA] Lookup Recon timing:"+ timing);
trace.info("[Lookup SGA] Attributes to Get:"+ ATTRS_TO_GET);


// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
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


	
if (timing.equals("executeQuery:PLATFORMS")) {
	
	trace.info("[Lookup SGA] Getting platforms.");
  String codekey =  ATTRS_TO_GET[0];
	String decodekey = ATTRS_TO_GET[1];
 
  // Getting the wsdl
  def WSDL=configuration.getListarPlataformasSGAWSDL();
	
	// Call webservice	
  entel.oim.connectors.sga.webservices.listarplataformas.ListarPlataformasSGAResponseType response = 
      OperationsSGA.lookupPlatforms("$WEBPROTOCOL",
                                  	"$WEBHOST",
                                  	"$WEBPORT",
                                  	"$WSDL",
                                  	"$USER_LOGIN",
                                  	"$PASS_LOGIN");
  
  // Checking response                                	
	if (response == null){

		//Error
    // Throw exception
    throw new ConnectorException("[Lookup SGA] Error PLATFORMS: Unexpected error calling to the webservice");
	
  } else if (response.getCodigo() != "0000") {

    // Error
    // Throw exception
    throw new ConnectorException("[Lookup SGA] Error PLATFORMS: Webservice error code: "+ response.getCodigo() + "Webservice error message: "+response.getDescripcion());
 
  } else {
     
    // Getting the platforms list 
    entel.oim.connectors.sga.webservices.listarplataformas.DetallePlataformasType[] detArray = response.getPlataformas();
  
    // loop over platforms
    for (int i=0; i < detArray.length ; i++) {
					
      // Get the current platform   
      entel.oim.connectors.sga.webservices.listarplataformas.DetallePlataformasType det = detArray[i];
      trace.info("[Lookup SGA] Platform found: " + det.getNombrePlataforma());
		
      util.addAttribute(codekey,det.getNombrePlataforma());
      util.addAttribute(decodekey,det.getNombrePlataforma());
      if (!util.build()) return;
		} 
  
    trace.info("[Lookup SGA] PLATFORMS were loaded sucessfully.");
    
  }

}
	
 
if (timing.equals("executeQuery:GROUPS")) {
	
	trace.info("[Lookup SGA] Getting groups.");
  String codekey =  ATTRS_TO_GET[0];
	String decodekey = ATTRS_TO_GET[1];
	
  // Getting the wsdl
  def WSDL=configuration.getListarGruposSGAWSDL();
  
  
	// Call webservice	
  entel.oim.connectors.sga.webservices.listargrupos.ListarGruposSGAResponseType response = 
      OperationsSGA.lookupGroups (  "$WEBPROTOCOL",
                                  	"$WEBHOST",
                                  	"$WEBPORT",
                                  	"$WSDL",
                                  	"$USER_LOGIN",
                                  	"$PASS_LOGIN");
  
  // Checking response                                	
	if (response == null){

		//Error
    // Throw exception
    throw new ConnectorException("[Lookup SGA] Error GROUPS: Unexpected error calling to the webservice");
	
  } else if (response.getCodigo() != "0000") {

    // Error
    // Throw exception
    throw new ConnectorException("[Lookup SGA] Error GROUPS: Webservice error code: "+ response.getCodigo() + "Webservice error message: "+response.getDescripcion());
 
  } else {
     
    // Getting the platforms list 
    entel.oim.connectors.sga.webservices.listargrupos.DetalleGruposType[] detArray = response.getGrupos();
  
    // loop over groups
    for (int i=0; i < detArray.length ; i++) {
					
      // Get the current group   
      entel.oim.connectors.sga.webservices.listargrupos.DetalleGruposType det = detArray[i];
      trace.info("[Lookup SGA] Group found: " + det.getIdGrupo() + " - " + det.getNombreGrupo());
		
      util.addAttribute(decodekey,det.getIdGrupo());
      util.addAttribute(codekey,det.getNombreGrupo());
      if (!util.build()) return;
		} 
  
    trace.info("[Lookup SGA] GROUPS were loaded sucessfully.");
    
  }

} 


if (timing.equals("executeQuery:LIC_TYPES")) {

        trace.info("[Lookup SGA] Getting license types.");
  String codekey =  ATTRS_TO_GET[0];
        String decodekey = ATTRS_TO_GET[1];


  util.addAttribute(codekey,"Read");
  util.addAttribute(decodekey,"Read");
  if (!util.build()) return;


  util.addAttribute(codekey,"Fixed");
  util.addAttribute(decodekey,"Fixed");
  if (!util.build()) return;


  util.addAttribute(codekey,"Floating");
  util.addAttribute(decodekey,"Floating");
  if (!util.build()) return;

  trace.info("[Lookup SGA] LIC_TYPES were loaded sucessfully.");

} 
