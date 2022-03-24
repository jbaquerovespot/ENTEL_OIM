package entel.oim.connectors.sau.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sau.OperationsSAU;
import entel.oim.connectors.sau.webservices.EliminarUAPServiceStub.*;


// Initiation
trace.ok("[Delete Child SAU] Attributes::"+attributes);
System.out.println(attributes);

// Get the parameters
def WEBPROTOCOL = configuration.getProtocol();
def WEBHOST = configuration.getHost();
def WEBPORT = configuration.getPort();
def WSDL=configuration.getEliminarUAPSAUWSDL();
def AUTH=configuration.getAuthenticationDeleteChildSAU();
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

childDataEOSet = null;
// Logic for handling Complex multi valued attributes
if(attributes.get("PROFILES")!=null)
{
    childDataEOSet=attributes.get("PROFILES").getValue();
    if(childDataEOSet !=null)
    {
        for( iterator = childDataEOSet.iterator(); iterator.hasNext(); )
        {
            eo = iterator.next();
            attrsSet = eo.getAttributes(); // Get all the attributes of child object
            superattr=AttributeUtil.find("PROFILEID",attrsSet);
            if(superattr!=null)
            {
                // Get GroupID
                def PROFILEID = superattr.getValue().get(0);
                
                // Call webservice	
                entel.oim.connectors.sau.webservices.eliminarusrappperfil.ResponseEliminarUAPType response = 
                    OperationsSAU.deleteUserProfile( "$WEBPROTOCOL",
                                                  	 "$WEBHOST",
                                                  	 "$WEBPORT",
                                                  	 "$WSDL",
                                                  	 "$USER_LOGIN",
                                                  	 "$PASS_LOGIN",
                                                     "$AUTH",
                                                     "$UID",
                                                     "$PROFILEID");
                  
                // Checking response                                	
                if (response == null){
                
                	//Error
                  // Throw exception
                  throw new ConnectorException("[Delete Child SAU] Error: Unexpected error calling to the webservice");
                
                } else if (response.getE_codigo() == "0000" || response.getE_codigo() == "0002") {
                
                  trace.info("[Delete Child SAU] Delete Child Success: $UID");
                
                  
                } else {
                   
                  // Error
                  // Throw exception
                  throw new ConnectorException("[Delete Child SAU] Error: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());
                
                }	
               
                
            } 

        } 
    } 
} 