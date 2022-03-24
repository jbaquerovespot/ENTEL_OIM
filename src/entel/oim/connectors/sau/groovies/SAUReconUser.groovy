package entel.oim.connectors.sau.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sau.OperationsSAU;


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
     

if (!filterMap.isEmpty()) {
    trace.info("[Execute Query SAU] Performing Recon with Filter. Filter is::"+ filterMap+" And Filer Params are::"+filterMap);
    
    filterAttr = filterMap.get("filterattribute");
    filterOp = filterMap.get("filteroperator");
    if("equals".equalsIgnoreCase(filterOp) && "__UID__".equalsIgnoreCase(filterAttr) ) {
		
      // Get filter value
  		filterVal = filterMap.get("filtervalue");
  		trace.info("Iniciando la consulta del usuario [$filterAttr - $filterVal]");
     
    
      // Getting the wsdl
      def WSDL=configuration.getBuscarUsuarioSAUWSDL();
	  def AUTH=configuration.getAuthenticationReconUserSAU();
     
      // Call webservice	
      entel.oim.connectors.sau.webservices.buscarusuario.ResponseBuscarUsuarioType user = 
        OperationsSAU.getUser(  "$WEBPROTOCOL",
                              	"$WEBHOST",
                              	"$WEBPORT",
                              	"$WSDL",
                              	"$USER_LOGIN",
                              	"$PASS_LOGIN",
                                "$AUTH",
                                "$filterVal");
    


      // Checking response                                	
      if (user == null){
    
    		//Error
        // Throw exception
        throw new ConnectorException("[Execute Query SAU] Error Recon: Unexpected error calling to the webservice");
    	
      } else if (user.getE_codigo() != "0000" && user.getE_codigo() != "0002") {
    
        // Error
        // Throw exception
        throw new ConnectorException("[Execute Query SAU] Error Recon: Webservice error code: "+ user.getE_codigo() + "Webservice error message: "+user.getE_mensaje());
     
      } else {
      
        // Get the info of the account
        def USERTYPE = user.getE_idtipousuario()!=null? user.getE_idtipousuario():null;
        def COMPANYTYPE = user.getE_idcompania()!=null? user.getE_idcompania():null;
        def NAMES = user.getE_nombres()!=null? user.getE_nombres():null;
        def LASTNAME = user.getE_apellidos()!=null? user.getE_apellidos():null;
        def RUT = user.getE_run()!=null? user.getE_run():null;
        def EMAIL = user.getE_email()!=null? user.getE_email():null;
        def ACCOUNT = user.getE_cuenta()!=null? user.getE_cuenta():null;
        def ESTADO = user.getE_estado()!=null? user.getE_estado():null;
	def FCHCONEXION = user.getE_fchconexion()!=null? user.getE_fchconexion():null;
		
        
        if (ESTADO != "ELIMINADO" && ACCOUNT != null && ACCOUNT !="" && ACCOUNT !=" ") {
        
          util.addAttribute(Uid.NAME,ACCOUNT);
    		  util.addAttribute(Name.NAME,ACCOUNT);
          util.addAttribute('USERTYPE',USERTYPE);
          util.addAttribute('COMPANYTYPE',COMPANYTYPE);
          util.addAttribute('NAMES',NAMES);
          util.addAttribute('LASTNAME',LASTNAME);
          util.addAttribute('RUT',RUT.replace("-","").replace(".",""));
          util.addAttribute('EMAIL',EMAIL);
		  util.addAttribute('FCHCONEXION',FCHCONEXION);
  
          if (ESTADO == "ACTIVO" || ESTADO == "BLOQUEADO" || ESTADO == "INACTIVO") {
               util.addAttribute('__ENABLE__',true);
              
          } else  {
             util.addAttribute('__ENABLE__',false);
          } 
    			
          if (ESTADO == "BLOQUEADO") {
             util.addAttribute('BLOCKED',true);
          } else {
   	     util.addAttribute('BLOCKED',false);
	  }
		  
	  if (ESTADO == "INACTIVO") {
	     util.addAttribute('INACTIVE',true);
	  } else {
	     util.addAttribute('INACTIVE',false);
	  }  

          // Get the applications of the user
          entel.oim.connectors.sau.webservices.buscarusuario.Aplicaciones[] appList = user.getE_aplicaciones();
          
          // CHeck if the user has applications
          if ( appList !=null ) {
          
            // Loop over the app			
        		for (int i=0; i < appList.length ; i++) {
        			
        		  entel.oim.connectors.sau.webservices.buscarusuario.Aplicaciones app = appList[i];
              
	              if (app.getApl_idaplicacion() != null && app.getApl_idperfil() != null) {
	                embeddedObjR = util.createEmbeddedObject();
	                embeddedObjR.addEmbeddedAttribute("PROFILEID",app.getApl_idaplicacion() + ";" +app.getApl_idperfil());
	                util.addEmbeddedObject("PROFILES",embeddedObjR);
	                
	              }
        			
        		}  
         
           }
           
           if (!util.build()) return;
    		
        }
       
    		trace.info("[Execute Query SAU] Finalizo la consulta del usuario [$filterVal]");	
  	
       }
     
  	} else {
  	
  		throw new ConnectorException("[Execute Query SAU] Ocurrio un error en la Reconciliacion - Filtro Invalido - Solo se permite el operador: equalTo con el atributo: __UID__");
  	
    }
	
} else {

	trace.info("[Execute Query SAU] Performing Recon of all users");
	

  // Getting the wsdl
  def WSDL=configuration.getBuscarMasUsuarioSAUWSDL();
  def AUTH=configuration.getAuthenticationReconUserBulkSAU();
  
  // Call webservice	
  entel.oim.connectors.sau.webservices.listarusuarios.ResponseBuscarUsuarioMasType response = 
    OperationsSAU.getUsers( "$WEBPROTOCOL",
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
    throw new ConnectorException("[Execute Query SAU] Error Recon: Unexpected error calling to the webservice");
	
  } else if (response.getE_codigo() != "0000") {

    // Error
    // Throw exception
    throw new ConnectorException("[Execute Query SAU] Error Recon: Webservice error code: "+ response.getE_codigo() + "Webservice error message: "+response.getE_mensaje());
 
  } else {
  
    // Get the users list
    entel.oim.connectors.sau.webservices.listarusuarios.Usuarios[] usrTypes = response.getE_usuarios();
    
    // Check if exists users
    if ( usrTypes !=null ) {
      
      // Loop over the users
      for (int i=0; i < usrTypes.length; i++) {
  			
  			entel.oim.connectors.sau.webservices.listarusuarios.Usuarios user = usrTypes[i];
  			
        // Get the info of the account
        def USERTYPE = user.getE_idtipousuario()!=null? user.getE_idtipousuario():null;
        def COMPANYTYPE = user.getE_idcompania()!=null? user.getE_idcompania():null;
        def NAMES = user.getE_nombres()!=null? user.getE_nombres():null;
        def LASTNAME = user.getE_apellidos()!=null? user.getE_apellidos():null;
        def RUT = user.getE_run()!=null? user.getE_run():null;
        def EMAIL = user.getE_email()!=null? user.getE_email():null;
        def ACCOUNT = user.getE_cuenta()!=null? user.getE_cuenta():null;
        def IDESTADO = user.getE_idestado()!=null? user.getE_idestado():null;
	def FCHCONEXION = user.getE_fchconexion()!=null? user.getE_fchconexion():null;
		
        if (IDESTADO != "5" && ACCOUNT != null && ACCOUNT !="" && ACCOUNT !=" ") {
       
          util.addAttribute(Uid.NAME,ACCOUNT);
    		  util.addAttribute(Name.NAME,ACCOUNT);
          util.addAttribute('USERTYPE',USERTYPE);
          util.addAttribute('COMPANYTYPE',COMPANYTYPE);
          util.addAttribute('NAMES',NAMES);
          util.addAttribute('LASTNAME',LASTNAME);
          util.addAttribute('RUT',RUT.replace("-","").replace(".",""));
          util.addAttribute('EMAIL',EMAIL);
		  util.addAttribute('FCHCONEXION',FCHCONEXION);
  
          if (IDESTADO == "1" || IDESTADO == "2" || IDESTADO == "3") {
               util.addAttribute('__ENABLE__',true);
              
          } else  {
             util.addAttribute('__ENABLE__',false);
          } 
    			
	  if (IDESTADO == "2") {
             util.addAttribute('BLOCKED',true);
          } else {
	  util.addAttribute('BLOCKED',false);
	  }
	  
	  if (ESTADO == "3") {
		  util.addAttribute('INACTIVE',true);
	  } else {
		  util.addAttribute('INACTIVE',false);
	  }      		
          
          // Get the applications of the user
          entel.oim.connectors.sau.webservices.listarusuarios.Aplicaciones[] appList = user.getE_aplicaciones();
          
          // CHeck if the user has applications
          if ( appList !=null ) {
          
            // Loop over the app			
        		for (int j=0; j < appList.length ; j++) {
        			
        			entel.oim.connectors.sau.webservices.listarusuarios.Aplicaciones app = appList[j];
              
              if (app.getApl_idaplicacion() != null && app.getPrf_idperfil() != null) {
              
                embeddedObjR = util.createEmbeddedObject();
                embeddedObjR.addEmbeddedAttribute("PROFILEID",app.getApl_idaplicacion() + ";" +app.getPrf_idperfil());
                util.addEmbeddedObject("PROFILES",embeddedObjR);
          			
              }
              
        		}  
         
           }
      	
      		if (!util.build()) return;
         
        }
  						
  		}
     
    }
		
		trace.info("[Execute Query SAU] Finalizo la consulta de los usuarios");	

   }

};

