package entel.oim.connectors.lync.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.lync.OperationsLync;
import entel.oim.connectors.lync.UserLync;


// Get the parameters
def PS_DIR = configuration.getPsDir();

if (!filterMap.isEmpty()) {
    trace.info("[Execute Query Lync] Performing Recon with Filter. Filter is::"+ filterMap+" And Filer Params are::"+filterMap);
    
    filterAttr = filterMap.get("filterattribute");
    filterOp = filterMap.get("filteroperator");
    if("equals".equalsIgnoreCase(filterOp) && "__UID__".equalsIgnoreCase(filterAttr) ) {
		
      // Get filter value
  		filterVal = filterMap.get("filtervalue");
  		trace.info("Iniciando la consulta del usuario [$filterAttr - $filterVal]");
     
	 // Call powershell	
      entel.oim.connectors.lync.UserLync user = 
					        OperationsLync.getUser( "$PS_DIR",
													"$filterVal");
					    
     // Checking response                                	
    if (user == null){
    
    	//Error
        // Throw exception
        throw new ConnectorException("[Execute Query Lync] Error Recon: Unexpected error calling to the powershell");
    	
    } else {
      
		// Get the info of the account
        def SAMACCOUNTNAME = user.getSamAccountName();
		def AUDIOVIDEODISABLED = user.isAudioVideoDisabled();
		def ENABLED = user.isEnabled();
		def REGISTRARPOOL = user.getRegistrarPool();
		def ENTERPRISEVOICEENABLED = user.isEnterpriseVoiceEnabled();
		def HOSTEDVOICEMAIL = user.isHostedVoiceMail();
		def LINEURI = user.getLineURI()
		def LINESERVERURI = user.getLineServerURI();
		def PRIVATELINE = user.getPrivateLine();
		def REMOTECALLCONTROLTELEPHONYENABLED = user.isRemoteCallControlTelephonyEnabled();
		def SIPADDRESS = user.getSipAddress();
		
		        
	      util.addAttribute(Uid.NAME,SAMACCOUNTNAME);
	      util.addAttribute(Name.NAME,SAMACCOUNTNAME);
	      util.addAttribute('AUDIOVIDEODISABLED',AUDIOVIDEODISABLED);
		  util.addAttribute('ENABLED',ENABLED);
		  util.addAttribute('__ENABLE__',ENABLED);
		  util.addAttribute('REGISTRARPOOL',REGISTRARPOOL);
		  util.addAttribute('ENTERPRISEVOICEENABLED',ENTERPRISEVOICEENABLED);
		  util.addAttribute('HOSTEDVOICEMAIL',HOSTEDVOICEMAIL);
		  util.addAttribute('LINEURI',LINEURI);
		  util.addAttribute('LINESERVERURI',LINESERVERURI);
		  util.addAttribute('PRIVATELINE',PRIVATELINE);
		  util.addAttribute('REMOTECALLCONTROLTELEPHONYENABLED',REMOTECALLCONTROLTELEPHONYENABLED);
		  util.addAttribute('SIPADDRESS',SIPADDRESS);
		  
		  
          if (!util.build()) return;
    		
    		trace.info("[Execute Query Lync] Finalizo la consulta del usuario [$filterVal]");	
  	
       }
     
  	} else {
  	
  		throw new ConnectorException("[Execute Query Lync] Ocurrio un error en la Reconciliacion - Filtro Invalido - Solo se permite el operador: equalTo con el atributo: __UID__");
  	
    }
	
} else {

	trace.info("[Execute Query Lync] Performing Recon of all users");
	
	
	// Call powershell	
      java.util.List<entel.oim.connectors.lync.UserLync> userList = 
					        OperationsLync.getUsers("$PS_DIR");
					    
   // Checking response                                	
   if (userList.size() == 0){
			
	//Error
    // Throw exception
    throw new ConnectorException("[Execute Query Lync] Error Recon: Unexpected error calling to the powershell");
	
  } else {
  
      // Loop over the users
      for (int i=0; i < userList.size(); i++) {
  			
		  	  // Get the user
  			  entel.oim.connectors.lync.UserLync user = userList.get(i);

			  // Get the info of the account
			  def SAMACCOUNTNAME = user.getSamAccountName();
			  def AUDIOVIDEODISABLED = user.isAudioVideoDisabled();
			  def ENABLED = user.isEnabled();
			  def REGISTRARPOOL = user.getRegistrarPool();
			  def ENTERPRISEVOICEENABLED = user.isEnterpriseVoiceEnabled();
			  def HOSTEDVOICEMAIL = user.isHostedVoiceMail();
			  def LINEURI = user.getLineURI()
			  def LINESERVERURI = user.getLineServerURI();
			  def PRIVATELINE = user.getPrivateLine();
			  def REMOTECALLCONTROLTELEPHONYENABLED = user.isRemoteCallControlTelephonyEnabled();
			  def SIPADDRESS = user.getSipAddress();
		
		        
			  util.addAttribute(Uid.NAME,SAMACCOUNTNAME);
			  util.addAttribute(Name.NAME,SAMACCOUNTNAME);
			  util.addAttribute('AUDIOVIDEODISABLED',AUDIOVIDEODISABLED);
			  util.addAttribute('ENABLED',ENABLED);
			  util.addAttribute('__ENABLE__',ENABLED);
			  util.addAttribute('REGISTRARPOOL',REGISTRARPOOL);
			  util.addAttribute('ENTERPRISEVOICEENABLED',ENTERPRISEVOICEENABLED);
			  util.addAttribute('HOSTEDVOICEMAIL',HOSTEDVOICEMAIL);
			  util.addAttribute('LINEURI',LINEURI);
			  util.addAttribute('LINESERVERURI',LINESERVERURI);
			  util.addAttribute('PRIVATELINE',PRIVATELINE);
			  util.addAttribute('REMOTECALLCONTROLTELEPHONYENABLED',REMOTECALLCONTROLTELEPHONYENABLED);
			  util.addAttribute('SIPADDRESS',SIPADDRESS);
				
			  if (!util.build()) return;
				  
        }
  						
  	 
    }
		
	trace.info("[Execute Query Lync] Finalizo la consulta de los usuarios");	

};

