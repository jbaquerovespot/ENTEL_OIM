package entel.oim.connectors.sga;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import entel.oim.connectors.sga.webservices.actualizargruposusuario.ActualizarGruposUsuarioSGARequestType;
import entel.oim.connectors.sga.webservices.actualizargruposusuario.ActualizarGruposUsuarioSGAResponseType;
import entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGA;
import entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGABindingStub;
import entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGALocator;
import entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGAPortType;
import entel.oim.connectors.sga.webservices.buscarcuentausuario.BuscarCuentaUsuarioSGARequestType;
import entel.oim.connectors.sga.webservices.buscarcuentausuario.BuscarCuentaUsuarioSGAResponseType;
import entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGA;
import entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGABindingStub;
import entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGALocator;
import entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGAPortType;
import entel.oim.connectors.sga.webservices.crearcuentasusuario.CrearCuentasUsuarioSGARequestType;
import entel.oim.connectors.sga.webservices.crearcuentasusuario.CrearCuentasUsuarioSGAResponseType;
import entel.oim.connectors.sga.webservices.crearcuentasusuario.WS_crearCuentasUsuarioSGA;
import entel.oim.connectors.sga.webservices.crearcuentasusuario.WS_crearCuentasUsuarioSGABindingStub;
import entel.oim.connectors.sga.webservices.crearcuentasusuario.WS_crearCuentasUsuarioSGALocator;
import entel.oim.connectors.sga.webservices.crearcuentasusuario.WS_crearCuentasUsuarioSGAPortType;
import entel.oim.connectors.sga.webservices.eliminarcuentasusuario.EliminarCuentasUsuarioSGARequestType;
import entel.oim.connectors.sga.webservices.eliminarcuentasusuario.EliminarCuentasUsuarioSGAResponseType;
import entel.oim.connectors.sga.webservices.eliminarcuentasusuario.WS_eliminarCuentasUsuarioSGA;
import entel.oim.connectors.sga.webservices.eliminarcuentasusuario.WS_eliminarCuentasUsuarioSGABindingStub;
import entel.oim.connectors.sga.webservices.eliminarcuentasusuario.WS_eliminarCuentasUsuarioSGALocator;
import entel.oim.connectors.sga.webservices.eliminarcuentasusuario.WS_eliminarCuentasUsuarioSGAPortType;
import entel.oim.connectors.sga.webservices.listargrupos.ListarGruposSGARequestType;
import entel.oim.connectors.sga.webservices.listargrupos.ListarGruposSGAResponseType;
import entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGA;
import entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGABindingStub;
import entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGALocator;
import entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGAPortType;
import entel.oim.connectors.sga.webservices.listarplataformas.ListarPlataformasSGARequestType;
import entel.oim.connectors.sga.webservices.listarplataformas.ListarPlataformasSGAResponseType;
import entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGA;
import entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGABindingStub;
import entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGALocator;
import entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGAPortType;
import entel.oim.connectors.sga.webservices.modificarcuentasusuario.ModificarCuentasUsuarioSGARequestType;
import entel.oim.connectors.sga.webservices.modificarcuentasusuario.ModificarCuentasUsuarioSGAResponseType;
import entel.oim.connectors.sga.webservices.modificarcuentasusuario.WS_modificarCuentasUsuarioSGA;
import entel.oim.connectors.sga.webservices.modificarcuentasusuario.WS_modificarCuentasUsuarioSGABindingStub;
import entel.oim.connectors.sga.webservices.modificarcuentasusuario.WS_modificarCuentasUsuarioSGALocator;
import entel.oim.connectors.sga.webservices.modificarcuentasusuario.WS_modificarCuentasUsuarioSGAPortType;
import entel.oim.connectors.sga.webservices.obtenerlistausuarios.ObtenerListaUsuarioSGARequestType;
import entel.oim.connectors.sga.webservices.obtenerlistausuarios.ObtenerListaUsuarioSGAResponseType;
import entel.oim.connectors.sga.webservices.obtenerlistausuarios.WS_obtenerListaUsuarioSGA;
import entel.oim.connectors.sga.webservices.obtenerlistausuarios.WS_obtenerListaUsuarioSGABindingStub;
import entel.oim.connectors.sga.webservices.obtenerlistausuarios.WS_obtenerListaUsuarioSGALocator;
import entel.oim.connectors.sga.webservices.obtenerlistausuarios.WS_obtenerListaUsuarioSGAPortType;


/**
 * Contains all the connector operations for SGA application
 * @author Oracle
 *
 */
public class OperationsSGA {
	
	private final static String className = OperationsSGA.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	
	/**
	 * Create an user in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param loginName User login of the user
	 * @param plataforma Platform of the user
	 * @param rutUsuario Government ID of the user
	 * @param tipoLicencia License type of the user
	 * @param nombres Names of the user
	 * @param email Email of the user
	 * @param estadoBack Status of the user
	 * @param loginGenesys ID of the Login Genesis of the user
	 * @param telefono Telephone number of the user
	 * @param grupos Groups of the users
	 * @return Result of the webservice
	 */
	public static CrearCuentasUsuarioSGAResponseType createUser(	String protocol_ws,
																	String host_ws,
																	String port_ws,
																	String wsdl,
																	String username_ws, 
																	String password_ws,
																	String loginName, 
																	String plataforma,
																	String rutUsuario,
																	String tipoLicencia,
																	String nombres,
																	String email,
																	String estadoBack,
																	String loginGenesys,
																	String telefono,
																	String grupos) {
		
		try {
			// Log
			logger.entering(className, "createUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_crearCuentasUsuarioSGA service: " + url_ws);
			
			// Default values for optional parameters
			if ("null".equalsIgnoreCase(email)) email = ""; 
			if ("null".equalsIgnoreCase(loginGenesys)) loginGenesys = ""; 
			if ("null".equalsIgnoreCase(telefono)) telefono = "";
			if ("null".equalsIgnoreCase(grupos)) grupos = "";
			
			// Initiate service
			logger.fine("Creating instance of WS_crearCuentasUsuarioSGA service");
			WS_crearCuentasUsuarioSGA service = new WS_crearCuentasUsuarioSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_crearCuentasUsuarioSGA service");
			WS_crearCuentasUsuarioSGAPortType port = service.getWS_crearCuentasUsuarioSGAPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for WS_crearCuentasUsuarioSGA service");
			CrearCuentasUsuarioSGARequestType crearCuentasUsuarioSGARequestType = new CrearCuentasUsuarioSGARequestType();
			crearCuentasUsuarioSGARequestType.setLoginName(loginName);
			crearCuentasUsuarioSGARequestType.setPlataforma(plataforma);
			crearCuentasUsuarioSGARequestType.setRutUsuario(rutUsuario);
			crearCuentasUsuarioSGARequestType.setTipoLicencia(tipoLicencia);
			crearCuentasUsuarioSGARequestType.setNombres(nombres);
			crearCuentasUsuarioSGARequestType.setEstadoBack(estadoBack);
			crearCuentasUsuarioSGARequestType.setEmail(email);
			crearCuentasUsuarioSGARequestType.setLoginGenesys(loginGenesys);
			crearCuentasUsuarioSGARequestType.setTelefono(telefono);
			crearCuentasUsuarioSGARequestType.setGrupos(grupos);
			
	        // Construct the Authentication Header
	        logger.fine("Creating authenticator for WS_crearCuentasUsuarioSGA service");
	        WS_crearCuentasUsuarioSGABindingStub stub = (WS_crearCuentasUsuarioSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_crearCuentasUsuarioSGA with parameters: "+ crearCuentasUsuarioSGARequestType.toString());
	        CrearCuentasUsuarioSGAResponseType crearCuentasUsuarioSGAResponseType = port.crearCuentasUsuarioSGA(crearCuentasUsuarioSGARequestType);
	        
	        // Return the webservice result
	        logger.exiting(className, "createUser");
	     	return crearCuentasUsuarioSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "createUser - Unexpected error: " + e.toString());
			logger.exiting(className, "createUser");
	     	
			// Return
			return null;
			
		}
	
	}
		

	
	/**
	 * Update an user in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param loginName User login of the user
	 * @param plataforma Platform of the user
	 * @param rutUsuario Government ID of the user
	 * @param tipoLicencia License type of the user
	 * @param nombres Names of the user
	 * @param email Email of the user
	 * @param estadoBack Status of the user
	 * @param loginGenesys ID of the Login Genesis of the user
	 * @param telefono Telephone number of the user
	 * @return Result of the webservice
	 */
	public static ModificarCuentasUsuarioSGAResponseType updateUser(	String protocol_ws,
																		String host_ws,
																		String port_ws,
																		String wsdl,
																		String username_ws, 
																		String password_ws,
																		String loginName, 
																		String plataforma,
																		String rutUsuario,
																		String tipoLicencia,
																		String nombres,
																		String email,
																		String estadoBack,
																		String loginGenesys,
																		String telefono) {
		
		try {
			// Log
			logger.entering(className, "updateUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_modificarCuentasUsuarioSGA service: " + url_ws);
			
				
			// Default values for optional parameters
			if ("null".equalsIgnoreCase(email)) email = ""; 
			if ("null".equalsIgnoreCase(loginGenesys)) loginGenesys = ""; 
			if ("null".equalsIgnoreCase(telefono)) telefono = "";
			if ("null".equalsIgnoreCase(plataforma)) plataforma = ""; 
			if ("null".equalsIgnoreCase(rutUsuario)) rutUsuario = ""; 
			if ("null".equalsIgnoreCase(tipoLicencia)) tipoLicencia = "";
			if ("null".equalsIgnoreCase(nombres)) nombres = ""; 
			if ("null".equalsIgnoreCase(estadoBack)) estadoBack = ""; 
			
			
			// Initiate service
			logger.fine("Creating instance of WS_modificarCuentasUsuarioSGA service");
			WS_modificarCuentasUsuarioSGA service = new WS_modificarCuentasUsuarioSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_crearCuentasUsuarioSGA service");
			WS_modificarCuentasUsuarioSGAPortType port = service.getWS_modificarCuentasUsuarioSGAPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for WS_modificarCuentasUsuarioSGA service");
			ModificarCuentasUsuarioSGARequestType modificarCuentasUsuarioSGARequestType = new ModificarCuentasUsuarioSGARequestType();
			modificarCuentasUsuarioSGARequestType.setLoginName(loginName);
			modificarCuentasUsuarioSGARequestType.setPlataforma(plataforma);
			modificarCuentasUsuarioSGARequestType.setRutUsuario(rutUsuario);
			modificarCuentasUsuarioSGARequestType.setTipoLicencia(tipoLicencia);
			modificarCuentasUsuarioSGARequestType.setNombres(nombres);
			modificarCuentasUsuarioSGARequestType.setEstadoBack(estadoBack);
			modificarCuentasUsuarioSGARequestType.setEmail(email);
			modificarCuentasUsuarioSGARequestType.setLoginGenesys(loginGenesys);
			modificarCuentasUsuarioSGARequestType.setTelefono(telefono);
			
	        // Construct the Authentication Header
	        logger.fine("Creating authenticator for WS_modificarCuentasUsuarioSGA service");
	        WS_modificarCuentasUsuarioSGABindingStub stub = (WS_modificarCuentasUsuarioSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_modificarCuentasUsuarioSGA with parameters: "+ modificarCuentasUsuarioSGARequestType.toString());
	        ModificarCuentasUsuarioSGAResponseType modificarCuentasUsuarioSGAResponseType = port.modificarCuentasUsuarioSGA(modificarCuentasUsuarioSGARequestType);
	        
	        // Return the webservice result
	        logger.exiting(className, "updateUser");
	        return modificarCuentasUsuarioSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateUser - Unexpected error: " + e.toString());
			logger.exiting(className, "updateUser");
			
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Delete an user in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param loginName User login to delete
	 * @return Result of the webservice
	 */
	public static EliminarCuentasUsuarioSGAResponseType deleteUser(	String protocol_ws,
																	String host_ws,
																	String port_ws,
																	String wsdl,
																	String username_ws, 
																	String password_ws,
																	String loginName) {
		
		try {
			// Log
			logger.entering(className, "deleteUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_eliminarCuentasUsuarioSGA service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of WS_eliminarCuentasUsuarioSGA service");
			WS_eliminarCuentasUsuarioSGA service = new WS_eliminarCuentasUsuarioSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_eliminarCuentasUsuarioSGA service");
			WS_eliminarCuentasUsuarioSGAPortType port = service.getWS_eliminarCuentasUsuarioSGAPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for WS_eliminarCuentasUsuarioSGA service");
			EliminarCuentasUsuarioSGARequestType eliminarCuentasUsuarioSGARequestType = new EliminarCuentasUsuarioSGARequestType();
			eliminarCuentasUsuarioSGARequestType.setLoginName(loginName);
			
	        // Construct the Authentication Header
	        logger.fine("Creating authenticator for WS_eliminarCuentasUsuarioSGA service");
	        WS_eliminarCuentasUsuarioSGABindingStub stub = (WS_eliminarCuentasUsuarioSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_eliminarCuentasUsuarioSGA with parameters: "+ eliminarCuentasUsuarioSGARequestType.toString());
	        EliminarCuentasUsuarioSGAResponseType eliminarCuentasUsuarioSGAResponseType = port.eliminarCuentasUsuarioSGA(eliminarCuentasUsuarioSGARequestType);
			
	        // Return the webservice result
	        logger.exiting(className, "deleteUser");
	     	return eliminarCuentasUsuarioSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "deleteUser - Unexpected error: " + e.toString());
			logger.exiting(className, "deleteUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	/**
	 * List all platforms in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @return Result of the webservice
	 */
	public static ListarPlataformasSGAResponseType lookupPlatforms(	String protocol_ws,
																	String host_ws,
																	String port_ws,
																	String wsdl,
																	String username_ws, 
																	String password_ws) {
		
		try {
			// Log
			logger.entering(className, "lookupPlatforms");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_listarPlataformasSGA service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of WS_listarPlataformasSGA service");
			WS_listarPlataformasSGA service = new WS_listarPlataformasSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_listarPlataformasSGA service");
			WS_listarPlataformasSGAPortType port = service.getWS_listarPlataformasSGAPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for WS_listarPlataformasSGA service");
			ListarPlataformasSGARequestType listarPlataformasSGARequestType = new ListarPlataformasSGARequestType();
        
	        // Construct the Authentication Header
	        logger.fine("Creating authenticator for WS_listarPlataformasSGA service");
	        WS_listarPlataformasSGABindingStub stub = (WS_listarPlataformasSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_listarPlataformasSGA with parameters: "+ listarPlataformasSGARequestType.toString());
	        ListarPlataformasSGAResponseType listarPlataformasSGAResponseType = port.listarPlataformasSGA(listarPlataformasSGARequestType);
	        
	        // Return the webservice result
	        logger.exiting(className, "lookupPlatforms");
	     	return listarPlataformasSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "lookupPlatforms - Unexpected error: " + e.toString());
			logger.exiting(className, "lookupPlatforms");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	/**
	 * List all groups in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @return Result of the webservice
	 */
	public static ListarGruposSGAResponseType lookupGroups(	String protocol_ws,
															String host_ws,
															String port_ws,
															String wsdl,
															String username_ws, 
															String password_ws) {
		
		try {
			// Log
			logger.entering(className, "lookupGroups");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_listarGruposSGA service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of WS_listarGruposSGA service");
			WS_listarGruposSGA service = new WS_listarGruposSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_listarGruposSGA service");
			WS_listarGruposSGAPortType port = service.getWS_listarGruposSGAPort(new java.net.URL(url_ws));
			
						
			// Create the request
			logger.fine("Creating request for WS_listarGruposSGA service");
			ListarGruposSGARequestType listarGruposSGARequestType = new ListarGruposSGARequestType();
			
	        // Construct the Authentication Header
			logger.fine("Creating authenticator for WS_listarGruposSGA service");
			WS_listarGruposSGABindingStub stub = (WS_listarGruposSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_listarGruposSGA with parameters: "+ listarGruposSGARequestType.toString());
	        ListarGruposSGAResponseType listarGruposSGAResponseType = port.listarGruposSGA(listarGruposSGARequestType);
	        
	        // Return the webservice result
	        logger.exiting(className, "lookupGroups");
	     	return listarGruposSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "lookupGroups - Unexpected error: " + e.toString());
			logger.exiting(className, "lookupGroups");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	/**
	 * Update a group of an user in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param loginName User login to update
	 * @param action Action to execute (Agregar or Eliminar)
	 * @param group Code of Group to update. Must finish with semicolon (;) 
	 * @return Result of the webservice
	 */
	public static ActualizarGruposUsuarioSGAResponseType updateGroup(	String protocol_ws,
																		String host_ws,
																		String port_ws,
																		String wsdl,
																		String username_ws, 
																		String password_ws,
																		String loginName,
																		String action,
																		String group) {
		
		try {
			// Log
			logger.entering(className, "updateGroup");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_actualizarGruposUsuarioSGA service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of WS_actualizarGruposUsuarioSGA service");
			WS_actualizarGruposUsuarioSGA service = new WS_actualizarGruposUsuarioSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_actualizarGruposUsuarioSGA service");
			WS_actualizarGruposUsuarioSGAPortType port = service.getWS_actualizarGruposUsuarioSGAPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for WS_actualizarGruposUsuarioSGA service");
			ActualizarGruposUsuarioSGARequestType actualizarGruposUsuarioSGARequestType = new ActualizarGruposUsuarioSGARequestType();
			actualizarGruposUsuarioSGARequestType.setLoginName(loginName);
			actualizarGruposUsuarioSGARequestType.setAccion(action);
			actualizarGruposUsuarioSGARequestType.setGrupo(group);
			
	        // Construct the Authentication Header
			logger.fine("Creating authenticator for WS_actualizarGruposUsuarioSGA service");
			WS_actualizarGruposUsuarioSGABindingStub stub = (WS_actualizarGruposUsuarioSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_actualizarGruposUsuarioSGA with parameters: "+ actualizarGruposUsuarioSGARequestType.toString());
	        ActualizarGruposUsuarioSGAResponseType actualizarGruposUsuarioSGAResponseType = port.actualizarGruposUsuarioSGA(actualizarGruposUsuarioSGARequestType);
	        
	        // Return the webservice result
	        logger.exiting(className, "updateGroup");
	     	return actualizarGruposUsuarioSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateGroup - Unexpected error: " + e.toString());
			logger.exiting(className, "updateGroup");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	
	
	/**
	 * Get an user in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @param indicator Type of filter for search the user (R - RUT | L- Login | RL - RUT y Login)
	 * @param value Value of the filter field
	 * @return Result of the webservice
	 */
	public static BuscarCuentaUsuarioSGAResponseType getUser(	String protocol_ws,
																String host_ws,
																String port_ws,
																String wsdl,
																String username_ws, 
																String password_ws,
																String indicator,
																String value) {
		
		try {
			// Log
			logger.entering(className, "getUser");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_buscarCuentaUsuarioSGA service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of WS_buscarCuentaUsuarioSGA service");
			WS_buscarCuentaUsuarioSGA service = new WS_buscarCuentaUsuarioSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_buscarCuentaUsuarioSGA service");
			WS_buscarCuentaUsuarioSGAPortType port = service.getWS_buscarCuentaUsuarioSGAPort(new java.net.URL(url_ws));
						
			// Create the request
			logger.fine("Creating request for WS_buscarCuentaUsuarioSGA service");
			BuscarCuentaUsuarioSGARequestType buscarCuentaUsuarioSGARequestType = new BuscarCuentaUsuarioSGARequestType();
			buscarCuentaUsuarioSGARequestType.setIndicador(indicator);
			buscarCuentaUsuarioSGARequestType.setValor(value);
			
	        // Construct the Authentication Header
			logger.fine("Creating authenticator for WS_buscarCuentaUsuarioSGA service");
			WS_buscarCuentaUsuarioSGABindingStub stub = (WS_buscarCuentaUsuarioSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_buscarCuentaUsuarioSGA with parameters: "+ buscarCuentaUsuarioSGARequestType.toString());
	        BuscarCuentaUsuarioSGAResponseType buscarCuentaUsuarioSGAResponseType = port.buscarCuentaUsuarioSGA(buscarCuentaUsuarioSGARequestType);
	        
	        // Return the webservice result
	        logger.exiting(className, "getUser");
	     	return buscarCuentaUsuarioSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUser - Unexpected error: " + e.toString());
			logger.exiting(className, "getUser");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	/**
	 * List all users in SGA
	 * @param protocol_ws  Protocol of the webservice
	 * @param host_ws  Host of the webservice
	 * @param port_ws  Port of the webservice
	 * @param wsdl  WSDL of the webservice
	 * @param username_ws Username of the webservice
	 * @param password_ws Password of the webservice
	 * @return Result of the webservice
	 */
	public static ObtenerListaUsuarioSGAResponseType getUsers(	String protocol_ws,
																String host_ws,
																String port_ws,
																String wsdl,
																String username_ws, 
																String password_ws) {
		
		try {
			// Log
			logger.entering(className, "getUsers");
			
			// Construct the URL of the webservice
			String url_ws = protocol_ws + "://" + host_ws + ":" + port_ws + "/" + wsdl;
			logger.finest("URL for WS_obtenerListaUsuarioSGA service: " + url_ws);
			
			// Initiate service
			logger.fine("Creating instance of WS_obtenerListaUsuarioSGA service");
			WS_obtenerListaUsuarioSGA service = new WS_obtenerListaUsuarioSGALocator();

			// Put the URL
			logger.fine("Setting URL for WS_obtenerListaUsuarioSGA service");
			WS_obtenerListaUsuarioSGAPortType port = service.getWS_obtenerListaUsuarioSGAPort(new java.net.URL(url_ws));
			
			// Create the request
			logger.fine("Creating request for WS_obtenerListaUsuarioSGA service");
			ObtenerListaUsuarioSGARequestType obtenerListaUsuarioSGARequestType = new ObtenerListaUsuarioSGARequestType();
			obtenerListaUsuarioSGARequestType.setTimestamp("");
			
	        // Construct the Authentication Header
			logger.fine("Creating authenticator for WS_obtenerListaUsuarioSGA service");
			WS_obtenerListaUsuarioSGABindingStub stub = (WS_obtenerListaUsuarioSGABindingStub)port;
	        stub.setUsername(username_ws);
	        stub.setPassword(password_ws);
	        
	        // Call the webservice
	        logger.finest("Calling webservice WS_obtenerListaUsuarioSGA with parameters: "+ obtenerListaUsuarioSGARequestType.toString());
	        ObtenerListaUsuarioSGAResponseType obtenerListaUsuarioSGAResponseType = port.obtenerListaUsuarioSGA(obtenerListaUsuarioSGARequestType);
	        
	        // Return the webservice result
	        logger.exiting(className, "getUsers");
	     	return obtenerListaUsuarioSGAResponseType;
			
		} catch (Exception e) {
			
			// Unexpected exception
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsers - Unexpected error: " + e.toString());
			logger.exiting(className, "getUsers");
	     	
			// Return
			return null;
			
		}
	
	}
	
	
	public static void main(String[] args) throws RemoteException {
		
		/* CrearCuentasUsuarioSGAResponseType result = createUser(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123",
				"dferreira", 
				"Sucursales",
				"25078127K",
				"Read",
				"DANIEL JESUS FERREIRA ANDRADE",
				"null",
				"Activo",
				"null",
				"null",
				"null");
				
		
		ModificarCuentasUsuarioSGAResponseType result = updateUser(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123",
				"dferreira", 
				"null",
				"null",
				"null",
				"DANIEL FERREIRA ANDRADE",
				"null",
				"Activo",
				"null",
				"null");
		
		
		EliminarCuentasUsuarioSGAResponseType result = deleteUser(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123",
				"dferreira");
				
		ListarPlataformasSGAResponseType result = lookupPlatforms(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123");
				
		ListarGruposSGAResponseType result = lookupGroups(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123");
			
				
				ActualizarGruposUsuarioSGAResponseType result = updateGroup(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123",
				"dferreira",
				"Agregar",
				"10414;");
				
				
				BuscarCuentaUsuarioSGAResponseType result = getUser(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123",
				"R",
				"25078127K");
			
			
			
				ObtenerListaUsuarioSGAResponseType result = getUsers(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"uproiam6456", 
				"entel123");
				
			
				
				ListarPlataformasSGAResponseType result = lookupPlatforms(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"crm/crmloyalty/customerintermanag/crm_t_px_listarplataformassgaps?wsdl",
				"uproiam6456", 
				"entel123");
				
	
		DetallePlataformasType[] det = result.getPlataformas().getListadoPlataformas();
		
		for (int i=0; i < det.length ; i++) {
			
			DetallePlataformasType plat = det[i];
			System.out.println(plat.getNombrePlataforma());
			
			
		}
		
			*/		
				
				
		/*	
		ListarGruposSGAResponseType result = lookupGroups(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"crm/crmloyalty/customerintermanag/crm_t_px_listarplataformassgaps?wsdl",
				"uproiam6456", 
				"entel123");
				
	
		DetalleGruposType[] det = result.getGrupos().getListadoGrupos();
		
		for (int i=0; i < det.length ; i++) {
			
			DetalleGruposType plat = det[i];
			System.out.println(plat.getIdGrupo() + " - " + plat.getNombreGrupo());
			
			
		}
		
		
		
		BuscarCuentaUsuarioSGAResponseType result = getUser(	
				"http",
				"esbdesa.entel.cl",
				"80", 
				"crm/crmloyalty/customerintermanag/crm_t_px_buscarcuentausuariosgaps?wsdl",
				"uproiam6456", 
				"entel123",
				"R",
				"25078127K");

		
		
		
			String  det0 = result.getEmail();
			String  det1 = result.getEstadoBack();
			String  det2 = result.getFono();
			String  det3 = result.getLogin();
			String  det4 = result.getLoginGenesys();
			String  det5 = result.getNombres();
			String  det6 = result.getPlataforma();
			String  det7 = result.getRutUsuario();
			String  det8 = result.getTipoLicencia();
			entel.oim.connectors.sga.webservices.buscarcuentausuario.DetalleGruposType[]  det = result.getGrupos();
						
		for (int i=0; i < det.length ; i++) {
			
			entel.oim.connectors.sga.webservices.buscarcuentausuario.DetalleGruposType plat = det[i];
			System.out.println(plat.getIdGrupo() + " - " + plat.getNombreGrupo());
			
			
		}
		
		
		*/
		

        

	}

}


