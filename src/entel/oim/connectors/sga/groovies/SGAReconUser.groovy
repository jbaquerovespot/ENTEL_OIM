package entel.oim.connectors.sga.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;
import entel.oim.connectors.sga.OperationsSGA;


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
     

if (!filterMap.isEmpty()) {
    trace.info("[Execute Query SGA] Performing Recon with Filter. Filter is::"+ filterMap+" And Filer Params are::"+filterMap);
    
    filterAttr = filterMap.get("filterattribute");
    filterOp = filterMap.get("filteroperator");
    if("equals".equalsIgnoreCase(filterOp) && ("__UID__".equalsIgnoreCase(filterAttr) || "RUT".equalsIgnoreCase(filterAttr))) {
		
      // Get filter value
  		filterVal = filterMap.get("filtervalue");
  		trace.info("Iniciando la consulta del usuario [$filterAttr - $filterVal]");
     
      def INDICATOR;
      if("__UID__".equalsIgnoreCase(filterAttr)) {
       INDICATOR = "L";
      } else {
       INDICATOR = "R";
      }
     
      // Getting the wsdl
      def WSDL=configuration.getBuscarCuentaUsuarioSGAWSDL();
      
      // Call webservice	
      entel.oim.connectors.sga.webservices.buscarcuentausuario.BuscarCuentaUsuarioSGAResponseType response = 
        OperationsSGA.getUser(  "$WEBPROTOCOL",
                              	"$WEBHOST",
                              	"$WEBPORT",
                              	"$WSDL",
                              	"$USER_LOGIN",
                              	"$PASS_LOGIN",
                                "$INDICATOR",
                                "$filterVal");
    
      // Checking response                                	
    	if (response == null){
    
    		//Error
        // Throw exception
        throw new ConnectorException("[Execute Query SGA] Error Recon: Unexpected error calling to the webservice");
    	
      } else if (response.getCodigo() != "0000") {
    
        // Error
        // Throw exception
        throw new ConnectorException("[Execute Query SGA] Error Recon: Webservice error code: "+ response.getCodigo() + "Webservice error message: "+response.getDescripcion());
     
      } else {
      
        // Get the user info
        def EMAIL = response.getEmail();
  			def BACKSTATUS = response.getEstadoBack();
  			def PHONE = response.getFono();
  			def LOGIN = response.getLogin();
  			def GENESYSLOGIN = response.getLoginGenesys();
  			def FULLNAME = response.getNombres();
  			def PLATFORM = response.getPlataforma();
  			def RUT = response.getRutUsuario();
  			def LIC_TYPE = response.getTipoLicencia();
        
        util.addAttribute(Uid.NAME,LOGIN);
  		  util.addAttribute(Name.NAME,LOGIN);
        util.addAttribute('EMAIL',EMAIL);
        util.addAttribute('PHONE',PHONE);
        util.addAttribute('GENESYSLOGIN',GENESYSLOGIN);
        util.addAttribute('FULLNAME',FULLNAME);
        util.addAttribute('PLATFORM',PLATFORM);
        util.addAttribute('RUT',RUT);
        util.addAttribute('LIC_TYPE',LIC_TYPE);
        util.addAttribute('BACKSTATUS',BACKSTATUS);
        if (BACKSTATUS == "Activo") {
          util.addAttribute('__ENABLE__',true);
          
        } else  {
           util.addAttribute('__ENABLE__',false);
        } 
  			
        
        // Get the groups of the user
        entel.oim.connectors.sga.webservices.buscarcuentausuario.DetalleGruposType[] grpTypes = response.getGrupos();
        
        // CHeck if the user has groups
        if ( grpTypes !=null ) {
        
          // Loop over the groups			
      		for (int i=0; i < grpTypes.length ; i++) {
      			
      			entel.oim.connectors.sga.webservices.buscarcuentausuario.DetalleGruposType grp = grpTypes[i];
      			System.out.println(grp.getIdGrupo() + " - " + grp.getNombreGrupo());
      			embeddedObjR = util.createEmbeddedObject();
            embeddedObjR.addEmbeddedAttribute("GROUPID",grp.getIdGrupo());
            embeddedObjR.addEmbeddedAttribute("GROUPNAME",grp.getNombreGrupo());
            util.addEmbeddedObject("GROUPS",embeddedObjR);
      			
      		}  
       
         }
         
         if (!util.build()) return;
    		
    		trace.info("[Execute Query SGA] Finalizo la consulta del usuario [$filterVal]");	
  	
       }
     
  	} else {
  	
  		throw new ConnectorException("[Execute Query SGA] Ocurrio un error en la Reconciliacion - Filtro Invalido - Solo se permite el operador: equalTo con el atributo: __UID__ o RUT");
  	
    }
	
} else {

	trace.info("[Execute Query SGA] Performing Recon of all users");
	

  // Getting the wsdl
  def WSDL=configuration.getObtenerListaUsuarioSGAWSDL();
  
  // Call webservice	
  entel.oim.connectors.sga.webservices.obtenerlistausuarios.ObtenerListaUsuarioSGAResponseType response = 
    OperationsSGA.getUsers(  "$WEBPROTOCOL",
                          	"$WEBHOST",
                          	"$WEBPORT",
                          	"$WSDL",
                          	"$USER_LOGIN",
                          	"$PASS_LOGIN");

  // Checking response                                	
	if (response == null){

		//Error
    // Throw exception
    throw new ConnectorException("[Execute Query SGA] Error Recon: Unexpected error calling to the webservice");
	
  } else if (response.getCodigo() != "0000") {

    // Error
    // Throw exception
    throw new ConnectorException("[Execute Query SGA] Error Recon: Webservice error code: "+ response.getCodigo() + "Webservice error message: "+response.getDescripcion());
 
  } else {
  
    // Get the users list
    entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleUsuariosType[] usrTypes = response.getListaUsuarios();
    
    // Check if exists users
    if ( usrTypes !=null ) {
      
      // Loop over the users
      for (int i=0; i < usrTypes.length; i++) {
  			
  			entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleUsuariosType user = usrTypes[i];
  			
	        // Get the user info
	        def EMAIL = user.getEmail();
    		def BACKSTATUS = user.getEstadoBack();
    		def PHONE = user.getFono();
    		def LOGIN = user.getLogin();
    		def GENESYSLOGIN = user.getLoginGenesys();
    		def FULLNAME = user.getNombres();
    		def PLATFORM = user.getPlataforma();
    		def RUT = user.getRutUsuario();
    		def LIC_TYPE = user.getTipoLicencia();
        
			util.addAttribute(Uid.NAME,LOGIN);
	    	util.addAttribute(Name.NAME,LOGIN);
	        util.addAttribute('EMAIL',EMAIL);
	        util.addAttribute('PHONE',PHONE);
	        util.addAttribute('GENESYSLOGIN',GENESYSLOGIN);
	        util.addAttribute('FULLNAME',FULLNAME);
	        util.addAttribute('PLATFORM',PLATFORM);
	        util.addAttribute('RUT',RUT);
	        util.addAttribute('LIC_TYPE',LIC_TYPE);
	        util.addAttribute('BACKSTATUS',BACKSTATUS);
	        if (BACKSTATUS == "Activo") {
	          util.addAttribute('__ENABLE__',true);
	     
	        } else  {
	           util.addAttribute('__ENABLE__',false);
	        } 
    		
	        // Get the groups of the user
	        entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleGruposType[]  grpTypes = user.getGrupos();
	        
	        // CHeck if the user has groups
	        if ( grpTypes !=null ) {
	          
				// Loop over the groups			
	      		for (int j=0; j < grpTypes.length ; j++) {
	      			
	      			entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleGruposType grp = grpTypes[j];
	      			System.out.println(grp.getIdGrupo() + " - " + grp.getNombreGrupo());
	      			embeddedObjR = util.createEmbeddedObject();
	                embeddedObjR.addEmbeddedAttribute('GROUPID',grp.getIdGrupo());
	                embeddedObjR.addEmbeddedAttribute('GROUPNAME',grp.getNombreGrupo());
	                util.addEmbeddedObject('GROUPS',embeddedObjR);
	      			
	      		}
	       
	        }
	    	
	    	if (!util.build()) return;
  						
  		}
     
    }
		
	trace.info("[Execute Query SGA] Finalizo la consulta de los usuarios");	

   }

};

