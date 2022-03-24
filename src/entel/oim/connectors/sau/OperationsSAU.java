package entel.oim.connectors.sau;

import java.util.logging.Level;
import java.util.logging.Logger;
import entel.oim.connectors.sau.webservices.actualizaratributosusr.ActualizarUsuarioBindingStub;
import entel.oim.connectors.sau.webservices.actualizaratributosusr.ActualizarUsuarioPortType;
import entel.oim.connectors.sau.webservices.actualizaratributosusr.ActualizarUsuarioService;
import entel.oim.connectors.sau.webservices.actualizaratributosusr.ActualizarUsuarioServiceLocator;
import entel.oim.connectors.sau.webservices.actualizaratributosusr.RequestActualizarUsuarioType;
import entel.oim.connectors.sau.webservices.actualizaratributosusr.ResponseActualizarUsuarioType;
import entel.oim.connectors.sau.webservices.agregarusrappperfil.AgregarUAPBindingStub;
import entel.oim.connectors.sau.webservices.agregarusrappperfil.AgregarUAPPortType;
import entel.oim.connectors.sau.webservices.agregarusrappperfil.AgregarUAPService;
import entel.oim.connectors.sau.webservices.agregarusrappperfil.AgregarUAPServiceLocator;
import entel.oim.connectors.sau.webservices.agregarusrappperfil.RequestAgregarUAPType;
import entel.oim.connectors.sau.webservices.agregarusrappperfil.ResponseAgregarUAPType;
import entel.oim.connectors.sau.webservices.buscarperfiles.BuscarPerfilesBindingStub;
import entel.oim.connectors.sau.webservices.buscarperfiles.BuscarPerfilesPortType;
import entel.oim.connectors.sau.webservices.buscarperfiles.BuscarPerfilesService;
import entel.oim.connectors.sau.webservices.buscarperfiles.BuscarPerfilesServiceLocator;
import entel.oim.connectors.sau.webservices.buscarperfiles.RequestBuscarPerfilesType;
import entel.oim.connectors.sau.webservices.buscarperfiles.ResponseBuscarPerfilesType;
import entel.oim.connectors.sau.webservices.buscarusuario.BuscarUsuarioBindingStub;
import entel.oim.connectors.sau.webservices.buscarusuario.BuscarUsuarioPorType;
import entel.oim.connectors.sau.webservices.buscarusuario.BuscarUsuarioService;
import entel.oim.connectors.sau.webservices.buscarusuario.BuscarUsuarioServiceLocator;
import entel.oim.connectors.sau.webservices.buscarusuario.RequestBuscarUsuarioType;
import entel.oim.connectors.sau.webservices.buscarusuario.ResponseBuscarUsuarioType;
import entel.oim.connectors.sau.webservices.crearusuario.CrearUsuarioBindingStub;
import entel.oim.connectors.sau.webservices.crearusuario.CrearUsuarioPortType;
import entel.oim.connectors.sau.webservices.crearusuario.CrearUsuarioService;
import entel.oim.connectors.sau.webservices.crearusuario.CrearUsuarioServiceLocator;
import entel.oim.connectors.sau.webservices.crearusuario.RequestCrearUsuarioType;
import entel.oim.connectors.sau.webservices.crearusuario.ResponseCrearUsuarioType;
import entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPBindingStub;
import entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPPortType;
import entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPService;
import entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPServiceLocator;
import entel.oim.connectors.sau.webservices.eliminarusrappperfil.RequestEliminarUAPType;
import entel.oim.connectors.sau.webservices.eliminarusrappperfil.ResponseEliminarUAPType;
import entel.oim.connectors.sau.webservices.eliminarusuario.EliminarUsuarioBindingStub;
import entel.oim.connectors.sau.webservices.eliminarusuario.EliminarUsuarioPortType;
import entel.oim.connectors.sau.webservices.eliminarusuario.EliminarUsuarioService;
import entel.oim.connectors.sau.webservices.eliminarusuario.EliminarUsuarioServiceLocator;
import entel.oim.connectors.sau.webservices.eliminarusuario.RequestEliminarUsuarioType;
import entel.oim.connectors.sau.webservices.eliminarusuario.ResponseEliminarUsuarioType;
import entel.oim.connectors.sau.webservices.deshabilitarusuario.DeshabilitarUsuarioBindingStub;
import entel.oim.connectors.sau.webservices.deshabilitarusuario.DeshabilitarUsuarioPortType;
import entel.oim.connectors.sau.webservices.deshabilitarusuario.DeshabilitarUsuarioService;
import entel.oim.connectors.sau.webservices.deshabilitarusuario.DeshabilitarUsuarioServiceLocator;
import entel.oim.connectors.sau.webservices.deshabilitarusuario.RequestDeshabilitarUsuarioType;
import entel.oim.connectors.sau.webservices.deshabilitarusuario.ResponseDeshabilitarUsuarioType;
import entel.oim.connectors.sau.webservices.habilitarusuario.HabilitarUsuarioBindingStub;
import entel.oim.connectors.sau.webservices.habilitarusuario.HabilitarUsuarioPortType;
import entel.oim.connectors.sau.webservices.habilitarusuario.HabilitarUsuarioService;
import entel.oim.connectors.sau.webservices.habilitarusuario.HabilitarUsuarioServiceLocator;
import entel.oim.connectors.sau.webservices.habilitarusuario.RequestHabilitarUsuarioType;
import entel.oim.connectors.sau.webservices.habilitarusuario.ResponseHabilitarUsuarioType;
import entel.oim.connectors.sau.webservices.listarusuarios.BuscarUsuariosMasBindingStub;
import entel.oim.connectors.sau.webservices.listarusuarios.BuscarUsuariosMasPorType;
import entel.oim.connectors.sau.webservices.listarusuarios.BuscarUsuariosMasService;
import entel.oim.connectors.sau.webservices.listarusuarios.BuscarUsuariosMasServiceLocator;
import entel.oim.connectors.sau.webservices.listarusuarios.RequestBuscarUsuariosMasType;
import entel.oim.connectors.sau.webservices.listarusuarios.ResponseBuscarUsuarioMasType;
import entel.oim.connectors.sau.webservices.obtenerappestados.ObtenerAppEstadosBindingStub;
import entel.oim.connectors.sau.webservices.obtenerappestados.ObtenerAppEstadosPortType;
import entel.oim.connectors.sau.webservices.obtenerappestados.ObtenerAppEstadosService;
import entel.oim.connectors.sau.webservices.obtenerappestados.ObtenerAppEstadosServiceLocator;
import entel.oim.connectors.sau.webservices.obtenerappestados.RequestBuscarAplicacionesType;
import entel.oim.connectors.sau.webservices.obtenerappestados.ResponseBuscarAplicacionesType;
import entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasBindingStub;
import entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasPortType;
import entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasService;
import entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasServiceLocator;
import entel.oim.connectors.sau.webservices.obtenercompaniasall.RequestObtenerCompaniasType;
import entel.oim.connectors.sau.webservices.obtenercompaniasall.ResponseObtenerCompaniasType;
import entel.oim.connectors.sau.webservices.obtenerusrestados.ObtenerEstadosUsrBindingStub;
import entel.oim.connectors.sau.webservices.obtenerusrestados.ObtenerEstadosUsrPortType;
import entel.oim.connectors.sau.webservices.obtenerusrestados.ObtenerEstadosUsrService;
import entel.oim.connectors.sau.webservices.obtenerusrestados.ObtenerEstadosUsrServiceLocator;
import entel.oim.connectors.sau.webservices.obtenerusrestados.RequestObtenerEstadosUsrType;
import entel.oim.connectors.sau.webservices.obtenerusrestados.ResponseObtenerEstadosUsrType;
import entel.oim.connectors.sau.webservices.reiniciarpassword.RequestResetPasswordType;
import entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordBindingStub;
import entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordPortType;
import entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordService;
import entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordServiceLocator;
import entel.oim.connectors.sau.webservices.reiniciarpassword.ResponseResetPasswordType;


/**
 * Contains all the connector operations for SAU application
 * @author Oracle
 *
 */
public class OperationsSAU {
	
	private final static String className = OperationsSAU.class.getName();
	private static Logger logger = Logger.getLogger(className);

	/**
	 * Create an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param user User that creates the new user account
	 * @param account Account of the user to create
	 * @param password Password of the user to create
	 * @param userType Type of user to create
	 * @param companyType Company type of the user to create
	 * @param names Names of the user to create
	 * @param lastname Lastnames of the user to create
	 * @param rut RUt of the user to create
	 * @param email Email of the user to create
	 * @param stateType State type of the user to create
	 * @return Response of the webservice 
	 */
	public static ResponseCrearUsuarioType createUser(	String protocol_ws,
														String host_ws,
														String port_ws,
														String wsdl,
														String username_ws, 
														String password_ws,
														String authentication,
														String user,
														String account,
														String password,
														String userType,
														String companyType,
														String names,
														String lastname,
														String rut,
														String email,
														String stateType) {
		
		
		
		
		try {
			// Log
			logger.entering(className, "createUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for CrearUsuarioServiceStub service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of CrearUsuarioService service");
			CrearUsuarioService service = new CrearUsuarioServiceLocator();

			// Put the URL
			logger.fine("Setting URL for CrearUsuarioService service");
			CrearUsuarioPortType port = service.getCrearUsuarioPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for CrearUsuarioService service");
			RequestCrearUsuarioType requestCrearUsuarioType = new RequestCrearUsuarioType();
			requestCrearUsuarioType.setI_nombre(names);
			requestCrearUsuarioType.setI_apellido(lastname);
			requestCrearUsuarioType.setI_email(email);
			requestCrearUsuarioType.setI_cuenta(account);
			requestCrearUsuarioType.setI_password(password);
			requestCrearUsuarioType.setI_usuario(user);
			requestCrearUsuarioType.setI_idtipoestado(stateType);
			requestCrearUsuarioType.setI_tipocompania(companyType);
			requestCrearUsuarioType.setI_tipousuario(userType);
			requestCrearUsuarioType.setI_rut(rut);
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for CrearUsuarioService service");
				CrearUsuarioBindingStub stub = (CrearUsuarioBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
		        
			}
	        		
	        // Call the webservice
	        logger.finest("Calling webservice CrearUsuarioService with parameters: "+ requestCrearUsuarioType.toString());
	        ResponseCrearUsuarioType responseCrearUsuarioType = port.crearUsuario(requestCrearUsuarioType);
			
	        // Return the webservice result
	        logger.exiting(className, "createUser");
	     	return responseCrearUsuarioType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
	     	logger.log(Level.SEVERE, "createUser - Unexpected error", e);
			logger.exiting(className, "createUser");
			
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Update an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param user User that updates the user account
	 * @param account Account of the user to update
	 * @param password Password of the user to update
	 * @param userType Type of user to update
	 * @param companyType Company type of the user to update
	 * @param names Names of the user to update
	 * @param lastname Lastnames of the user to update
	 * @param rut RUt of the user to update
	 * @param email Email of the user to update
	 * @param stateType State type of the user to update
	 * @return Response of the webservice 
	 */
	 public static ResponseActualizarUsuarioType updateUser(	String protocol_ws,
															String host_ws,
															String port_ws,
															String wsdl,
															String username_ws, 
															String password_ws,
															String authentication,
															String user,
															String account,
															String userType,
															String companyType,
															String names,
															String lastname,
															String rut,
															String email,
															String stateType) {
		
		
		
		
		try {
			// Log
			logger.entering(className, "updateUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for ActualizarUsuarioService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of ActualizarUsuarioService service");
			ActualizarUsuarioService service = new ActualizarUsuarioServiceLocator();
			
			// Put the URL
			logger.fine("Setting URL for ActualizarUsuarioService service");
			ActualizarUsuarioPortType port = service.getActualizarUsuarioPort(new java.net.URL(url_ws));
						
			// Create the request
			logger.fine("Creating request for ActualizarUsuarioService service");
			RequestActualizarUsuarioType requestActualizarUsuarioType = new RequestActualizarUsuarioType();
			requestActualizarUsuarioType.setI_nombre(names);
			requestActualizarUsuarioType.setI_apellido(lastname);
			requestActualizarUsuarioType.setI_email(email);
			requestActualizarUsuarioType.setI_cuenta(account);
			requestActualizarUsuarioType.setI_usuario(user);
			requestActualizarUsuarioType.setI_idtipoestado(stateType);
			requestActualizarUsuarioType.setI_tipocompania(companyType);
			requestActualizarUsuarioType.setI_tipousuario(userType);
			requestActualizarUsuarioType.setI_rut(rut);
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for ActualizarUsuarioService service");
				ActualizarUsuarioBindingStub stub = (ActualizarUsuarioBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
		        
		    }
			
			// Call the webservice
	        logger.finest("Calling webservice ActualizarUsuarioServiceStub with parameters: "+ requestActualizarUsuarioType.toString());
	        ResponseActualizarUsuarioType responseActualizarUsuarioType = port.actualizarUsuario(requestActualizarUsuarioType);
	        
	        // Return the webservice result
	        logger.exiting(className, "updateUser");
	     	return responseActualizarUsuarioType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateUser - Unexpected error", e);
			logger.exiting(className, "updateUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	
	
	/**
	 * Disable an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param user User that disable the user account
	 * @param account Account of the user to disable
	 * @return Response of the webservice 
	 */
	public static ResponseDeshabilitarUsuarioType disableUser(	String protocol_ws,
																String host_ws,
																String port_ws,
																String wsdl,
																String username_ws, 
																String password_ws,
																String authentication,
																String user,
																String account) {
		
		
		
		
		try {
			// Log
			logger.entering(className, "disableUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for DeshabilitarUsuarioService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of DeshabilitarUsuarioService service");
			DeshabilitarUsuarioService service = new DeshabilitarUsuarioServiceLocator();

			// Put the URL
			logger.fine("Setting URL for DeshabilitarUsuarioService service");
			DeshabilitarUsuarioPortType port = service.getDeshabilitarUsuarioPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for DeshabilitarUsuarioService service");
			RequestDeshabilitarUsuarioType requestDeshabilitarUsuarioType = new RequestDeshabilitarUsuarioType();
			requestDeshabilitarUsuarioType.setI_cuenta(account);
			requestDeshabilitarUsuarioType.setI_usuario(user);
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for DeshabilitarUsuarioService service");
				DeshabilitarUsuarioBindingStub stub = (DeshabilitarUsuarioBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
		    }
	        		
	        // Call the webservice
	        logger.finest("Calling webservice DeshabilitarUsuarioService with parameters: "+ requestDeshabilitarUsuarioType.toString());
	        ResponseDeshabilitarUsuarioType responseDeshabilitarUsuarioType = port.deshabilitarUsuario(requestDeshabilitarUsuarioType);
	        
	        // Return the webservice result
	        logger.exiting(className, "disableUser");
	     	return responseDeshabilitarUsuarioType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "disableUser - Unexpected error", e);
			logger.exiting(className, "disableUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Enable an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param user User that enable the user account
	 * @param account Account of the user to enable
	 * @return Response of the webservice 
	 */
	public static ResponseHabilitarUsuarioType enableUser(	String protocol_ws,
																String host_ws,
																String port_ws,
																String wsdl,
																String username_ws, 
																String password_ws,
																String authentication,
																String user,
																String account) {
		
		
		
		
		try {
			// Log
			logger.entering(className, "enableUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for HabilitarUsuarioService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of HabilitarUsuarioService service");
			HabilitarUsuarioService service = new HabilitarUsuarioServiceLocator();

			// Put the URL
			logger.fine("Setting URL for HabilitarUsuarioService service");
			HabilitarUsuarioPortType port = service.getHabilitarUsuarioPort(new java.net.URL(url_ws));
						
			// Create the request
			logger.fine("Creating request for HabilitarUsuarioService service");
			RequestHabilitarUsuarioType requestHabilitarUsuarioType = new RequestHabilitarUsuarioType();
			requestHabilitarUsuarioType.setI_cuenta(account);
			requestHabilitarUsuarioType.setI_usuario(user);
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for HabilitarUsuarioService service");
				HabilitarUsuarioBindingStub stub = (HabilitarUsuarioBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
	        		
	        // Call the webservice
	        logger.finest("Calling webservice HabilitarUsuarioService with parameters: "+ requestHabilitarUsuarioType.toString());
	        ResponseHabilitarUsuarioType responseHabilitarUsuarioType  = port.habilitarUsuario(requestHabilitarUsuarioType);
	        
	        // Return the webservice result
	        logger.exiting(className, "enableUser");
	     	return responseHabilitarUsuarioType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "enableUser - Unexpected error",e);
			logger.exiting(className, "enableUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	
	/**
	 * Reset the password of an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param account Account of the user to reset password
	 * @param password New password to set
	 * @return Response of the webservice 
	 */
	public static ResponseResetPasswordType resetPasswordUser(	String protocol_ws,
																	String host_ws,
																	String port_ws,
																	String wsdl,
																	String username_ws, 
																	String password_ws,
																	String authentication,
																	String account,
																	String password) {
		
		
		
		
		try {
			// Log
			logger.entering(className, "resetPasswordUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for ResetPasswordService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of ResetPasswordService service");
			ResetPasswordService service = new ResetPasswordServiceLocator();

			// Put the URL
			logger.fine("Setting URL for ResetPasswordService service");
			ResetPasswordPortType port = service.getResetPasswordPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for ResetPasswordService service");
			RequestResetPasswordType requestResetPasswordType = new RequestResetPasswordType();
			requestResetPasswordType.setI_cuenta(account);
			requestResetPasswordType.setI_password(password);
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for ResetPasswordService service");
				ResetPasswordBindingStub stub = (ResetPasswordBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
	        		
	        // Call the webservice
	        logger.finest("Calling webservice ResetPasswordService with parameters: "+ requestResetPasswordType.toString());
	        ResponseResetPasswordType responseResetPasswordType = port.resetPassword(requestResetPasswordType);	        
	        
	        // Return the webservice result
	        logger.exiting(className, "resetPasswordUser");
	     	return responseResetPasswordType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "resetPasswordUser - Unexpected error", e);
			logger.exiting(className, "resetPasswordUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	/**
	 * Delete an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param user User that delete the user account
	 * @param account Account of the user to delete
	 * @return Result of the webservice
	 */
	public static ResponseEliminarUsuarioType deleteUser(	String protocol_ws,
															String host_ws,
															String port_ws,
															String wsdl,
															String username_ws, 
															String password_ws,
															String authentication,
															String user,
															String account) {
		
		try {
			// Log
			logger.entering(className, "deleteUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for EliminarUsuarioService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of EliminarUsuarioService service");
			EliminarUsuarioService service = new EliminarUsuarioServiceLocator();

			// Put the URL
			logger.fine("Setting URL for EliminarUsuarioService service");
			EliminarUsuarioPortType port = service.getEliminarUsuarioPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for EliminarUsuarioService service");
			RequestEliminarUsuarioType requestEliminarUsuarioType = new RequestEliminarUsuarioType();
			requestEliminarUsuarioType.setI_cuenta(account);
			requestEliminarUsuarioType.setI_usuario(user);
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for EliminarUsuarioService service");
				EliminarUsuarioBindingStub stub = (EliminarUsuarioBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
			
			
	        // Call the webservice
	        logger.finest("Calling webservice EliminarUsuarioService with parameters: "+ requestEliminarUsuarioType.toString());
	        ResponseEliminarUsuarioType responseEliminarUsuarioType = port.eliminarUsuario(requestEliminarUsuarioType);
	        
	        // Return the webservice result
	        logger.exiting(className, "deleteUser");
	     	return responseEliminarUsuarioType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "deleteUser - Unexpected error", e);
			logger.exiting(className, "deleteUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	/**
	 * List all companies in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @return Result of the webservice
	 */
	public static ResponseObtenerCompaniasType lookupCompanies(	String protocol_ws,
																String host_ws,
																String port_ws,
																String wsdl,
																String username_ws, 
																String password_ws,
																String authentication) {
		
		try {
			// Log
			logger.entering(className, "lookupCompanies");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for ObtenerCompaniasService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of ObtenerCompaniasService service");
			ObtenerCompaniasService service = new ObtenerCompaniasServiceLocator();
			
			// Put the URL
			logger.fine("Setting URL for ObtenerCompaniasService service");
			ObtenerCompaniasPortType port = service.getObtenerCompaniasPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for ObtenerCompaniasService service");
			RequestObtenerCompaniasType requestObtenerCompaniasType = new RequestObtenerCompaniasType();
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for ObtenerCompaniasService service");
				ObtenerCompaniasBindingStub stub = (ObtenerCompaniasBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
			
	        // Call the webservice
	        logger.finest("Calling webservice ObtenerCompaniasService with parameters: "+ requestObtenerCompaniasType.toString());
	        ResponseObtenerCompaniasType responseObtenerCompaniasType = port.obtenerCompanias(requestObtenerCompaniasType);
	        
	        // Return the webservice result
	        logger.exiting(className, "lookupCompanies");
	     	return responseObtenerCompaniasType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "lookupCompanies - Unexpected error", e);
			logger.exiting(className, "lookupCompanies");
	     	
			// Return
			return null;
			
		}
	
	}
		

	
	/**
	 * List all user status in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @return Result of the webservice
	 */
	public static ResponseObtenerEstadosUsrType lookupUserStatus(	String protocol_ws,
																	String host_ws,
																	String port_ws,
																	String wsdl,
																	String username_ws, 
																	String password_ws,
																	String authentication) {
		
		try {
			// Log
			logger.entering(className, "lookupUserStatus");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for ObtenerEstadosUsrService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of ObtenerEstadosUsrService service");
			ObtenerEstadosUsrService service = new ObtenerEstadosUsrServiceLocator();

			// Put the URL
			logger.fine("Setting URL for ObtenerCompaniasService service");
			ObtenerEstadosUsrPortType port = service.getObtenerEstadosUsrPort(new java.net.URL(url_ws));
						
			// Create the request
			logger.fine("Creating request for ObtenerEstadosUsrService service");
			RequestObtenerEstadosUsrType requestObtenerEstadosUsrType = new RequestObtenerEstadosUsrType();
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for ObtenerEstadosUsrService service");
				ObtenerEstadosUsrBindingStub stub = (ObtenerEstadosUsrBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
			
	        // Call the webservice
	        logger.finest("Calling webservice ObtenerEstadosUsrService with parameters: "+ requestObtenerEstadosUsrType.toString());
	        ResponseObtenerEstadosUsrType responseObtenerEstadosUsrType = port.obtenerEstadosUsr(requestObtenerEstadosUsrType);
	        
	        // Return the webservice result
	        logger.exiting(className, "lookupUserStatus");
	     	return responseObtenerEstadosUsrType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "lookupUserStatus - Unexpected error", e);
			logger.exiting(className, "lookupUserStatus");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	/**
	 * List all app status in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @return Result of the webservice
	 */
	public static ResponseBuscarAplicacionesType lookupAppStatus(	String protocol_ws,
																	String host_ws,
																	String port_ws,
																	String wsdl,
																	String username_ws, 
																	String password_ws,
																	String authentication) {
		
		try {
			// Log
			logger.entering(className, "lookupAppStatus");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for ObtenerAppEstadosService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of ObtenerAppEstadosService service");
			ObtenerAppEstadosService service = new ObtenerAppEstadosServiceLocator();

			// Put the URL
			logger.fine("Setting URL for ObtenerAppEstadosService service");
			ObtenerAppEstadosPortType port = service.getObtenerAppEstadosPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for ObtenerAppEstadosService service");
			RequestBuscarAplicacionesType requestBuscarAplicacionesType = new RequestBuscarAplicacionesType();
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for ObtenerAppEstadosService service");
				ObtenerAppEstadosBindingStub stub = (ObtenerAppEstadosBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
			
	        // Call the webservice
	        logger.finest("Calling webservice ObtenerAppEstadosService with parameters: "+ requestBuscarAplicacionesType.toString());
	        ResponseBuscarAplicacionesType responseBuscarAplicacionesType = port.obtenerAppEstados(requestBuscarAplicacionesType);
	        
	        // Return the webservice result
	        logger.exiting(className, "lookupAppStatus");
	     	return responseBuscarAplicacionesType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "lookupAppStatus - Unexpected error", e);
			logger.exiting(className, "lookupAppStatus");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	/**
	 * List all profiles in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @return Result of the webservice
	 */
	public static ResponseBuscarPerfilesType lookupProfiles(	String protocol_ws,
																String host_ws,
																String port_ws,
																String wsdl,
																String username_ws, 
																String password_ws,
																String authentication) {
	
		try {
			// Log
			logger.entering(className, "lookupProfiles");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for BuscarPerfilesService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of BuscarPerfilesService service");
			BuscarPerfilesService service = new BuscarPerfilesServiceLocator();

			// Put the URL
			logger.fine("Setting URL for ObtenerAppEstadosService service");
			BuscarPerfilesPortType port = service.getBuscarPerfilesPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for BuscarPerfilesService service");
			RequestBuscarPerfilesType requestBuscarPerfilesType = new RequestBuscarPerfilesType();
			requestBuscarPerfilesType.setI_accion("ALL");
			requestBuscarPerfilesType.setI_palabraclave("");
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for BuscarPerfilesService service");
				BuscarPerfilesBindingStub stub = (BuscarPerfilesBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
			
	        // Call the webservice
	        logger.finest("Calling webservice BuscarPerfilesService with parameters: "+ requestBuscarPerfilesType.toString());
	        ResponseBuscarPerfilesType responseBuscarAplicacionesType = port.buscarPerfiles(requestBuscarPerfilesType);
	        
	        // Return the webservice result
	        logger.exiting(className, "lookupProfiles");
	     	return responseBuscarAplicacionesType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "lookupProfiles - Unexpected error", e);
			logger.exiting(className, "lookupProfiles");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Add a profile to an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param user User that creates the new user account
	 * @param account Account of the user to add the profile
	 * @param idAppProfile Parofile to add
	 * @param status Status of the assignation
	 * @return Response of the webservice 
	 */
	public static ResponseAgregarUAPType insertUserProfile(	String protocol_ws,
															String host_ws,
															String port_ws,
															String wsdl,
															String username_ws, 
															String password_ws,
															String authentication,
															String user,
															String account,
															String idAppProfile,
															String status) {
		
		try {
			// Log
			logger.entering(className, "insertUserProfile");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for AgregarUAPService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of AgregarUAPService service");
			AgregarUAPService service = new AgregarUAPServiceLocator();

			// Put the URL
			logger.fine("Setting URL for AgregarUAPService service");
			AgregarUAPPortType port = service.getAgregarUAPPort(new java.net.URL(url_ws));
			
						
			// Create the request
			logger.fine("Creating request for AgregarUAPService service");
			RequestAgregarUAPType requestAgregarUAPType = new RequestAgregarUAPType();
			String idApp = idAppProfile.split(";")[0];
			String idProfile = idAppProfile.split(";")[1];		
			requestAgregarUAPType.setI_cuenta(account);
			requestAgregarUAPType.setI_idaplicacion(idApp);
			requestAgregarUAPType.setI_idestado(status);
			requestAgregarUAPType.setI_idperfil(idProfile);
			requestAgregarUAPType.setI_usuario(user);
					
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for AgregarUAPService service");
				AgregarUAPBindingStub stub = (AgregarUAPBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
				
	        // Call the webservice
	        logger.finest("Calling webservice AgregarUAPService with parameters: "+ requestAgregarUAPType.toString());
	        ResponseAgregarUAPType responseAgregarUAPType = port.agregarUAP(requestAgregarUAPType);
	        
	        // Return the webservice result
	        logger.exiting(className, "insertUserProfile");
	     	return responseAgregarUAPType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "insertUserProfile - Unexpected error", e);
			logger.exiting(className, "insertUserProfile");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Remove a profile from an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param account Account of the user to remove the profile
	 * @param idAppProfile Parofile to remove
	 * @return Response of the webservice 
	 */
	public static ResponseEliminarUAPType deleteUserProfile(	String protocol_ws,
																String host_ws,
																String port_ws,
																String wsdl,
																String username_ws, 
																String password_ws,
																String authentication,
																String account,
																String idAppProfile) {
		
		try {
			// Log
			logger.entering(className, "deleteUserProfile");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for EliminarUAPService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of EliminarUAPService service");
			EliminarUAPService service = new EliminarUAPServiceLocator();

			// Put the URL
			logger.fine("Setting URL for AgregarUAPService service");
			EliminarUAPPortType port = service.getEliminarUAPPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for EliminarUAPService service");
			RequestEliminarUAPType requestEliminarUAPType = new RequestEliminarUAPType();
			String idApp = idAppProfile.split(";")[0];
			String idProfile = idAppProfile.split(";")[1];		
			requestEliminarUAPType.setI_cuenta(account);
			requestEliminarUAPType.setI_idaplicacion(idApp);
			requestEliminarUAPType.setI_idperfil(idProfile);
		
		    // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for EliminarUAPService service");
				EliminarUAPBindingStub stub = (EliminarUAPBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
		        
			}
				
	        // Call the webservice
	        logger.finest("Calling webservice EliminarUAPService with parameters: "+ requestEliminarUAPType.toString());
	        ResponseEliminarUAPType responseEliminarUAPType = port.eliminarUAP(requestEliminarUAPType);
	        
	        // Return the webservice result
	        logger.exiting(className, "deleteUserProfile");
	     	return responseEliminarUAPType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "deleteUserProfile - Unexpected error", e);
			logger.exiting(className, "deleteUserProfile");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Get an user in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @param account Account to find
	 * @return Result of the webservice
	 */
	public static ResponseBuscarUsuarioType getUser(	String protocol_ws,
														String host_ws,
														String port_ws,
														String wsdl,
														String username_ws, 
														String password_ws,
														String authentication,
														String account) {
	
		try {
			// Log
			logger.entering(className, "getUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for BuscarUsuarioService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of BuscarUsuarioService service");
			BuscarUsuarioService service = new BuscarUsuarioServiceLocator();
			
			// Put the URL
			logger.fine("Setting URL for BuscarUsuarioService service");
			BuscarUsuarioPorType port = service.getBuscarUsuarioPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for BuscarUsuarioService service");
			RequestBuscarUsuarioType requestBuscarUsuarioType = new RequestBuscarUsuarioType();
			requestBuscarUsuarioType.setI_cuenta(account);
			
			// Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for BuscarUsuarioService service");
				BuscarUsuarioBindingStub stub = (BuscarUsuarioBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
				
			}
			
	        // Call the webservice
	        logger.finest("Calling webservice BuscarUsuarioService with parameters: "+ requestBuscarUsuarioType.toString());
	        ResponseBuscarUsuarioType responseBuscarUsuarioType = port.buscarUsuario(requestBuscarUsuarioType);
	        		
	        // Return the webservice result
	        logger.exiting(className, "getUser");
	     	return responseBuscarUsuarioType;
	     	
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUser - Unexpected error", e);
			logger.exiting(className, "getUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Get the list of users in SAU
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param authentication Flag to include authentication in request
	 * @return Result of the webservice
	 */
	public static ResponseBuscarUsuarioMasType getUsers(	String protocol_ws,
															String host_ws,
															String port_ws,
															String wsdl,
															String username_ws, 
															String password_ws,
															String authentication) {
	
		try {
			// Log
			logger.entering(className, "getUsers");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for BuscarUsuariosMasService service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of BuscarUsuariosMasServiceStub service");
			BuscarUsuariosMasService service = new BuscarUsuariosMasServiceLocator();

			// Put the URL
			logger.fine("Setting URL for BuscarUsuariosMasService service");
			BuscarUsuariosMasPorType port = service.getBuscarUsuariosMasPort(new java.net.URL(url_ws));
						
			// Create the request
			logger.fine("Creating request for BuscarUsuariosMasService service");
			RequestBuscarUsuariosMasType requestBuscarUsuariosMasType = new RequestBuscarUsuariosMasType();
			requestBuscarUsuariosMasType.setI_accion("ALL");
			requestBuscarUsuariosMasType.setI_estadousr("0");
			requestBuscarUsuariosMasType.setI_palabraclave("");
			
	        // Construct the Authentication Header
			if (Boolean.valueOf(authentication)) {
				logger.fine("Creating authenticator for BuscarUsuariosMasService service");
				BuscarUsuariosMasBindingStub stub = (BuscarUsuariosMasBindingStub)port;
		        stub.setUsername(username_ws);
		        stub.setPassword(password_ws);
			}
			
	        // Call the webservice
	        logger.finest("Calling webservice BuscarUsuariosMasService with parameters: "+ requestBuscarUsuariosMasType.toString());
	        ResponseBuscarUsuarioMasType responseBuscarUsuarioMasType = port.buscarUsuariosMas(requestBuscarUsuariosMasType);
	        
	        // Return the webservice result
	        logger.exiting(className, "getUsers");
	     	return responseBuscarUsuarioMasType;
	     	
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsers - Unexpected error", e);
			logger.exiting(className, "getUsers");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	/*
	
	public static void main(String[] args) {
	
		
		ResponseBuscarUsuarioType result = getUser(	
				"https",
				"esbdesa.entel.cl",
				"443", 
				"enterprise/securityapp/accesousuarios/ent_t_px_buscarusuariososbsauserviceps?wsdl",
				"OIMWSSAU", 
				"sauOIM2018",
				"true",
				"jareyes");

		
		System.out.println(result.getE_codigo());
			System.out.println(result.getE_mensaje());
			
			System.out.println(result.getE_apellidos());
			System.out.println(result.getE_idcompania());
			System.out.println(result.getE_cuenta());
			System.out.println(result.getE_email());
			System.out.println(result.getE_estado());
			System.out.println(result.getE_idtipousuario());
			System.out.println(result.getE_nombres());
			System.out.println(result.getE_run());
			
			System.out.println(result.getE_aplicaciones()[0].getApl_nombre());
			
				
	}
	
		
		ResponseCrearUsuarioType result = createUser(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_crearusuarioosbsauserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false",
				"jareyes",
				"dmelean",
				"123456",
				"51",
				"10",
				"Daniel",
				"Melean",
				"26031919-1",
				"daniel.melean@oracle.com",
				"3");

		
		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());
			
			
			
			ResponseEliminarUsuarioType result = deleteUser(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_eliminarusuarioosbsauserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false",
				"jareyes",
				"dferreira");

		
		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());


ResponseObtenerCompaniasType result = lookupCompanies(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_obtenercompaniasallosbserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false");

		
		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());
			
			ResponseObtenerEstadosUsrType result = lookupUserStatus(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_obtenerusrestadososbserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false");

		
		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());
			
			
			
			ResponseBuscarAplicacionesType result = lookupAppStatus(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_obtenerappestadososbserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false");

		
		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());
			
			ListaEstados list = result.getE_estadoapp();
			
			Estado[] estados = list.getItem();
			
			for (int i =0; i < estados.length; i++) {
				Estado est= estados[i];
				
				System.out.println(est.getE_idestado());
				System.out.println(est.getE_descripcion());
			}
			
			
			
			
			ResponseBuscarPerfilesType result = lookupProfiles(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_buscarperfilesosbsauserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false");

		
		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());
			
			ListaPerfiles list = result.getE_perfiles();
			
			Perfiles[] perfiles = list.getItem();
			
			for (int i =0; i < perfiles.length; i++) {
				Perfiles est= perfiles[i];
				
				System.out.println(est.getE_aplicacion());
				System.out.println(est.getE_nombre());
				
				
				
				
			}
			*/
			
			/*
			ResponseAgregarUAPType result = insertUserProfile(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_agregarusrappperfilosbserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false",
				"jareyes",
				"dmelean",
				"1;3",
				"13");

		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());
			
			
			
			
			ResponseEliminarUAPType result = deleteUserProfile(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"enterprise/securityapp/accesousuarios/ent_t_px_eliminarusrappperfilosbserviceps?wsdl",
				"uproiam6456", 
				"entel123",
				"false",
				"dmelean",
				"1;3");

		
		
			System.out.println(result.getE_codigo().getStr4());
			System.out.println(result.getE_mensaje().getStr100());
			
			
			
			
			ResponseBuscarUsuarioType result = getUser(	
					"http",
					"esbdesa.entel.cl",
					"80", 
					"enterprise/securityapp/accesousuarios/ent_t_px_buscarusuariososbsauserviceps?wsdl",
					"uproiam6456", 
					"entel123",
					"false",
					"dmelean");

			
			
			
				System.out.println(result.getE_codigo().getStr4());
				System.out.println(result.getE_mensaje().getStr100());
				
				result.getE_apellidos().getStr100()
				result.getE_idcompania().getStr4()
				result.getE_cuenta().getStr15()
				result.getE_email().getStr100();
				result.getE_estado().getStr10();
				result.getE_idtipousuario().getStr4();
				result.getE_nombres().getStr100();
				result.getE_run().getStr10();
				result.getE_aplicaciones().getItem()[1].getApl_idaplicacion().getStr4()
				
				result.getE_aplicaciones().getItem()[1].getApl_idperfil().getStr4()
				
				
				result.getE_aplicaciones().getItem()[1].getApl_nombre().getStr100()
			
			
			
				
			ResponseBuscarUsuarioMasType response = getUsers(	
					"http",
					"esbdesa.entel.cl",
					"80", 
					"enterprise/securitymgmt/employeeidentificationmgmt/ent_t_px_listarusuariossaups?wsdl",
					"uproiam6456", 
					"entel123",
					"false");

			
			
			
				System.out.println(response.getE_codigo().getStr4());
				System.out.println(response.getE_mensaje().getStr100());
				
				response.getE_usuarios().getItem()[1].getE_apellidos().getStr100();
				response.getE_usuarios().getItem()[1].getE_aplicaciones()
				response.getE_usuarios().getItem()[1].getE_cuenta()
				response.getE_usuarios().getItem()[1].getE_idestado().getStr10()
						response.getE_usuarios().getItem()[1]
			
		
		
		
*/	
			
			

        

	//}*/

}


