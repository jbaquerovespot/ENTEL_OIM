package entel.oim.connectors.ssff.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.ssff.OperationsSSFF;


// Get the parameters
def ORGINAL_CONNECTOR_NAME = configuration.getOriginalConnectorName();
def CONFIGURATION_LOOKUP = configuration.getConfigurationLookup();

if (!filterMap.isEmpty()) {
    trace.info("[Execute Query SSFF] Performing Recon with Filter. Filter is::"+ filterMap+" And Filer Params are::"+filterMap);
    
    filterAttr = filterMap.get("filterattribute");
    filterOp = filterMap.get("filteroperator");
    if("equals".equalsIgnoreCase(filterOp) && "userId".equalsIgnoreCase(filterAttr) ) {
		
      // Get filter value
  		filterVal = filterMap.get("filtervalue");
  		trace.info("Iniciando la consulta del usuario [$filterAttr - $filterVal]");
     
	 // Call powershell	
      java.util.HashMap<String,String> attrsMap = 
					        OperationsSSFF.getUser( "$ORGINAL_CONNECTOR_NAME",
													"$CONFIGURATION_LOOKUP",
													"$filterVal");
					    
     // Checking response                                	
    if (user == null){
    
    	//Error
        // Throw exception
        throw new ConnectorException("[Execute Query SSFF] Error Recon: Unexpected error calling to the powershell");
    	
    } else {
      
		for (Map.Entry me : attrsMap.entrySet()) {
          System.out.println("Key: "+me.getKey() + " & Value: " + me.getValue());
		  util.addAttribute(me.getKey(),me.getValue());
        }
		        
         //	      util.addAttribute(Uid.NAME,SAMACCOUNTNAME);
	     //      util.addAttribute(Name.NAME,SAMACCOUNTNAME);
		  
		  
          if (!util.build()) return;
    		
    		trace.info("[Execute Query SSFF] Finalizo la consulta del usuario [$filterVal]");	
  	
       }
     
  	} else {
  	
  		throw new ConnectorException("[Execute Query SSFF] Ocurrio un error en la Reconciliacion - Filtro Invalido - Solo se permite el operador: equalTo con el atributo: __UID__");
  	
    }
	
} else {

	trace.info("[Execute Query SSFF] Performing Recon of all users");
	
	
	// Call powershell	
      java.util.HashMap<String,HashMap<String,String>> userMap = 
					        OperationsSSFF.getUsers("$ORGINAL_CONNECTOR_NAME",
								                    "$CONFIGURATION_LOOKUP");
					    
   // Checking response                                	
   if (userMap.size() == 0){
			
	//Error
    // Throw exception
    throw new ConnectorException("[Execute Query SSFF] Error Recon: Unexpected error calling to the powershell");
	
  } else {
  
      // Loop over the users
      for (Map.Entry me : attrsMap.entrySet()) {
  			
		  	  // Get the user
  			  def USERID = me.getKey();
			  java.util.HashMap attrsMap = me.getValue();	
			  
			  for (Map.Entry meAttr : attrsMap.entrySet()) {
				  System.out.println("Key: "+meAttr.getKey() + " & Value: " + meAttr.getValue());
				  util.addAttribute(meAttr.getKey(),meAttr.getValue());
				}
				
				//util.addAttribute(Uid.NAME,SAMACCOUNTNAME);
				//util.addAttribute(Name.NAME,SAMACCOUNTNAME);

			  if (!util.build()) return;
				  
        }
  						
  	 
   }
		
	trace.info("[Execute Query SSFF] Finalizo la consulta de los usuarios");	

};

