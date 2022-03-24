package entel.oim.connectors.sga.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sga.OperationsSGA;


// Initiation
trace.ok("[Delete Child SGA] Attributes::"+attributes);

System.out.println(attributes)

// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
def WSDL=configuration.getActualizarGruposUsuarioSGAWSDL();
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


childDataEOSet = null;
// Logic for handling Complex multi valued attributes
if(attributes.get("GROUPS")!=null)
{
    childDataEOSet=attributes.get("GROUPS").getValue();
    if(childDataEOSet !=null)
    {
        for( iterator = childDataEOSet.iterator(); iterator.hasNext(); )
        {
            eo = iterator.next();
            attrsSet = eo.getAttributes(); // Get all the attributes of child object
            superattr=AttributeUtil.find("GROUPID",attrsSet);
            if(superattr!=null)
            {
                // Get GroupID
                def GROUPID = superattr.getValue().get(0);
                
                // Call webservice	
                entel.oim.connectors.sga.webservices.actualizargruposusuario.ActualizarGruposUsuarioSGAResponseType response = 
                    OperationsSGA.updateGroup(  "$WEBPROTOCOL",
                                            	 "$WEBHOST",
                                            	 "$WEBPORT",
                                            	 "$WSDL",
                                            	 "$USER_LOGIN",
                                            	 "$PASS_LOGIN",
                                               "$UID",
                                               "Eliminar",
                                               "$GROUPID"+";");
                  
                // Checking response                                	
                if (response == null){
                
                	//Error
                  // Throw exception
                  throw new ConnectorException("[Delete Child SGA] Error: Unexpected error calling to the webservice");
                
                } else if (response.getCodigo() == "0000") {
                
                  trace.info("[Delete Child SGA] Delete Child Success: $UID");
                
                  
                } else {
                   
                  // Error
                  // Throw exception
                  throw new ConnectorException("[Delete Child SGA] Error: Webservice error code: "+ response.getCodigo() + "Webservice error message: "+response.getDescripcion());
                
                }	
               
                
            } 

        } 
    } 
} 
    
				
