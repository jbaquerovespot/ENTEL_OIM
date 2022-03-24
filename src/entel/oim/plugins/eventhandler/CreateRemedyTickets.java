package entel.oim.plugins.eventhandler;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_KEY;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import entel.oim.plugins.eventhandler.remedy.ws.InputMapping1;
import entel.oim.plugins.eventhandler.remedy.ws.ObjectFactory;
import entel.oim.plugins.eventhandler.remedy.ws.RemedyUtil;
import entel.oim.plugins.eventhandler.remedy.ws.RemedyVO;
import entel.oim.plugins.eventhandler.remedy.ws.SoapEnvelope;
import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authopss.exception.AccessDeniedException;
import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.kernel.spi.PostProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
/**
 * Event handler that creates Remedy WO when a new user is reconciled from Source Factor
 * @author Oracle
 *
 *
 */
public class CreateRemedyTickets implements PostProcessHandler {
	private final static String className = CreateRemedyTickets.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private static UserManager usrMgr = Platform.getService(UserManager.class);
	private static SystemConfigurationService systemConfService = Platform.getService(SystemConfigurationService.class);

	public void imprimirParametros(HashMap<String, Serializable> orchParam) {
		logger.info("Parámetros no nulos recibidos: ");
		for (Entry<String, Serializable> entry : orchParam.entrySet()) {
			if (entry.getValue() != null)
				logger.info(entry.getKey() + ": " + entry.getValue());
			else {
				logger.info(entry.getKey() + ": NULORemedy");

			}
		}
		logger.info("");
	}

	private String getParameterValue(HashMap<String, Serializable> parameters, String key) {
		if (parameters.containsKey(key)) {

			String value = (parameters.get(key) instanceof ContextAware)
					? (String) ((ContextAware) parameters.get(key)).getObjectValue()
					: (String) parameters.get(key);
			return value;
		} else {
			return null;
		}
	}

	private Date getDateParamaterValue(HashMap<String, Serializable> parameters, String key) {
		Date value = null;
		logger.info("fechaDeIngreso1");// null
		if (parameters.containsKey(key)) {
			value = (parameters.get(key) instanceof ContextAware)
					? (Date) ((ContextAware) parameters.get(key)).getObjectValue()
					: (Date) parameters.get(key);
			logger.info("fechaDeIngreso2");// null
		}
		logger.info("fechaDeIngreso3");// null

		return value;
	}

	public User findUser(String campo, String valor) {
		SearchCriteria userCriteria = new SearchCriteria(campo, valor, SearchCriteria.Operator.EQUAL);
		UserManager um = new OIMClient().getService(UserManager.class);
		List<User> listaUsers = null;
		User user = null;

		if (valor != null) {
			try {
				listaUsers = um.search(userCriteria, null, null);
				if (!listaUsers.isEmpty()) {
					user = listaUsers.get(0);
				}
			} catch (UserSearchException e) {
				e.printStackTrace();
			} catch (AccessDeniedException e) {
				e.printStackTrace();
			}
		} else
			logger.info("El atributo " + campo + " es nulo. Imposible realizar la busqueda");
		return user;
	}

	private long getLongParamaterValue(HashMap<String, Serializable> parameters, String key) {
		logger.entering(className, "getLongParamaterValue", key);
		long value = -1;

		logger.fine("Checking if parameters contains the value wanted");
		if (parameters.containsKey(key)) {
			value = (parameters.get(key) instanceof ContextAware)
					? (long) ((ContextAware) parameters.get(key)).getObjectValue()
					: (long) parameters.get(key);
		}

		logger.exiting(className, "getLongParamaterValue", value);
		return value;
	}

	/**
	 * Implementing mandatory method
	 * 
	 * @param processId
	 * @param eventId
	 * @param orchestration
	 * @return
	 * 
	 */
	@Override
	public EventResult execute(long processId, long eventId, Orchestration orchestration) {
		logger.entering(className, "execute", processId);
		try {
			logger.info("Method not required to be implementes");

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected Error - execute: " + e.getMessage(), e);
		}

		logger.exiting(className, "execute");
		return new EventResult();

	}

	public tcResultSet getLookup(String lookup) {
		tcResultSet tc = null;
		OIMClient oimClient = new OIMClient();
		tcLookupOperationsIntf lookupOps = oimClient.getService(Thor.API.Operations.tcLookupOperationsIntf.class);
		;
		try {
			tc = lookupOps.getLookupValues(lookup);
		} catch (tcAPIException e) {
			e.printStackTrace();
		} catch (tcInvalidLookupException e) {
			e.printStackTrace();
		}
		return tc;
	}

	public String getDecodeFromCode(String lookup, String code) {
		int i = 0;
		tcResultSet tc = getLookup(lookup);
		String decode = "";
		try {
			while (i < tc.getTotalRowCount()) {
				tc.goToRow(i);
				if (tc.getStringValueFromColumn(2).equals(code)) {
					decode = tc.getStringValueFromColumn(3);
					i = tc.getTotalRowCount();
				} else
					i++;
			}
		} catch (tcAPIException e) {
			e.printStackTrace();
		} catch (tcColumnNotFoundException e) {
			e.printStackTrace();
		}
		return decode;
	}

	/* 
	 * Process WO creation
	 * @see oracle.iam.platform.kernel.spi.PostProcessHandler#execute(long, long, oracle.iam.platform.kernel.vo.BulkOrchestration)
	 */
	@Override
	public BulkEventResult execute(long arg0, long arg1, BulkOrchestration arg2) {
		logger.info("AddingRemedy Tickets WO Bulk1 ");
		HashMap<String, Serializable>[] params = arg2.getBulkParameters();

		String rut = "";
		String silla = "";
		String nombreYApellidos = "";
		String cargo = "";
		String estamento = "";
		String empresaContratante = "";
		String lugarDeTrabajo = "";
		String fechaDeIngreso = "";
		String nombresYApellidosJefatura = "";
		String correoElectronico = "";
		String telefono = "";
		String gerencia = "";
		String centroDeCosto = "";
		String strVicepresidency = "";
		String strEstamento = "";
		String strCargo = "";
		String strEmpresaContratante = "";
		String strCentroDeCosto = "";

		long managerKey = 0;
		if (params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				HashMap<String, Serializable> orchParam = params[i];
				imprimirParametros(orchParam);
				rut = getParameterValue(orchParam, "RUT");
				logger.info("remedyRut" + rut);
				silla = getParameterValue(orchParam, "Position");
				logger.info("remedySilla" + silla);
				nombreYApellidos = getParameterValue(orchParam, "First Name") + " "
						+ getParameterValue(orchParam, "Last Name");
				logger.info("nombreYApellidos" + nombreYApellidos); 
				cargo = getParameterValue(orchParam, "Job");
				logger.info("remedyJob codigo" + cargo); 
				estamento = getParameterValue(orchParam, "Stratum");
				logger.info("Stratum" + estamento); 
				empresaContratante = getParameterValue(orchParam, "Society");
				logger.info("empresacontratante" + empresaContratante); 
				lugarDeTrabajo = getParameterValue(orchParam, "Address");
				logger.info("lugardetrabajo" + lugarDeTrabajo);
				logger.info("fechaDeIngreso inicio"); 
				fechaDeIngreso = getDateParamaterValue(orchParam, "Hire Date").toString();
				logger.info("fechaDeIngreso" + fechaDeIngreso);
				gerencia = getParameterValue(orchParam, "VicePresidency");
				logger.info("vicePresidency" + gerencia); 
				centroDeCosto = getParameterValue(orchParam, "CostCenter");
				logger.info("costCenter" + centroDeCosto); 
				managerKey = getLongParamaterValue(orchParam, "usr_manager_key");
				logger.info("managerKey1" + managerKey);
				strVicepresidency = getDecodeFromCode("Lookup.Users.VicePresidency", gerencia);
				logger.info("managerKeystrViceprecidency" + strVicepresidency);
				strEstamento = getDecodeFromCode("Lookup.Users.Stratum", estamento);
				logger.info("managerKeystrEstamento" + strEstamento);
				strCargo = getDecodeFromCode("Lookup.Users.Job", cargo);
				logger.info("managerKeystrCargo" + strCargo);
				strEmpresaContratante = getDecodeFromCode("Lookup.Users.Society", empresaContratante);
				logger.info("managerKeystrEmpresaContratante" + strEmpresaContratante);
				strCentroDeCosto = getDecodeFromCode("Lookup.Users.CostCenter", centroDeCosto);
				logger.info("managerKeystrCentroDeCosto" + strCentroDeCosto);
			}

		} else {
			logger.info("Parameter list is empty");
		}

		
		if (managerKey != -1) {
			try {

				logger.finest("Setting the attributes to find");
				Set<String> searchAttrs = new HashSet<String>();
				searchAttrs.add("VicePresidency");
				searchAttrs.add(UserManagerConstants.AttributeName.LASTNAME.getId());
				searchAttrs.add(UserManagerConstants.AttributeName.FIRSTNAME.getId());
				searchAttrs.add(UserManagerConstants.AttributeName.MOBILE.getId());
				searchAttrs.add(UserManagerConstants.AttributeName.EMAIL.getId());
				User mgr;
				
				mgr = usrMgr.getDetails(USER_KEY.getId(), managerKey, searchAttrs);
				 
				 
				HashMap<String,Object> mapAttrs = mgr.getAttributes();
				gerencia=(String) mapAttrs.get("VicePresidency");
		 		logger.info("VicePresidencyMgrVicePresidency1" + (String) mapAttrs.get("VicePresidency"));
		 		strVicepresidency = getDecodeFromCode("Lookup.Users.VicePresidency", gerencia);
		 		logger.info("VicePresidencyMgrVicePresidency2" + strVicepresidency );
		 		nombresYApellidosJefatura=(String) mapAttrs.get(UserManagerConstants.AttributeName.FIRSTNAME.getId()) +
		 				" "+(String) mapAttrs.get(UserManagerConstants.AttributeName.LASTNAME.getId());
		 		logger.info("VicePresidencyMgrName" + nombresYApellidosJefatura);
		 		correoElectronico=(String) mapAttrs.get(UserManagerConstants.AttributeName.EMAIL.getId());
		 		logger.info("VicePresidencyMgrcorreoElectronico" + correoElectronico);
		 		telefono=(String) mapAttrs.get(UserManagerConstants.AttributeName.MOBILE.getId());
		 		logger.info("VicePresidencyMgrtelefono" + telefono);
		 	} catch (NoSuchUserException | UserLookupException | SearchKeyNotUniqueException
					| oracle.iam.platform.authz.exception.AccessDeniedException e) {
				e.printStackTrace();
			}
			
			
		}

		
		RemedyVO remedyVO = new RemedyVO();
		remedyVO.setRut(rut);
		remedyVO.setSilla(silla);
		remedyVO.setNombreYApellidos(nombreYApellidos);
		remedyVO.setCargo(strCargo);
		remedyVO.setEstamento(strEstamento);
		remedyVO.setEmpresaContratante(strEmpresaContratante);
		remedyVO.setLugarDeTrabajo(lugarDeTrabajo);
		remedyVO.setFechaDeIngreso(fechaDeIngreso);
		remedyVO.setNombresYApellidosJefatura(nombresYApellidosJefatura);
		remedyVO.setCorreoElectronico(correoElectronico);
		remedyVO.setTelefono(telefono);
		remedyVO.setGerencia(strVicepresidency);
		remedyVO.setCentroDeCosto(centroDeCosto);
		callService(remedyVO);
		return null;
	}

	private void callService(RemedyVO remedyVO) {
		Properties properties = new Properties();

		try { 
			FileInputStream fileInputStream = new FileInputStream(systemConfService.getSystemProperty("Entel.PropertiesFileLoc").getPtyValue());
			properties.load(new InputStreamReader(fileInputStream, Charset.forName("UTF-8")));
			
		} catch (IOException | SystemConfigurationServiceException e1) {
			logger.info("Error opening remedy properties");
			e1.printStackTrace();
		}

		List<String> wo = new ArrayList<String>();
		wo.add("vendomatica");
		wo.add("equipo");
		wo.add("puesto");
		wo.add("credencial");
		wo.add("anexo");
		StringWriter writer =null;
		for (int i = 0; i < wo.size(); i++) {
			remedyVO.setType(wo.get(i));
			RemedyUtil remedyUtil = new RemedyUtil(properties);
			logger.info("jaxbinicio");
			 SoapEnvelope envelope = new SoapEnvelope( remedyVO,remedyUtil);
				
			 try {
			 JAXBContext jaxbCtx =
			 JAXBContext.newInstance(InputMapping1 .class, ObjectFactory.class,SoapEnvelope.class);
			 javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
			 marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
			 marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT,
			 Boolean.TRUE);
			 writer = new StringWriter();
			 marshaller.marshal(envelope, writer);
			 }
			 catch(Exception e) {
				 e.printStackTrace();
			 }

			 
			String message = writer.toString();
			SOAPConnection conn = null;
			try {
				conn = SOAPConnectionFactory.newInstance().createConnection();
			} catch (UnsupportedOperationException | SOAPException e) {
				e.printStackTrace();
			}

			try {
				InputStream Soapmessage = new ByteArrayInputStream(message.getBytes("UTF-8"));
				SOAPMessage msg = MessageFactory.newInstance().createMessage(null, Soapmessage);
				SOAPMessage resp = conn.call(msg, remedyUtil.loadUrl());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				resp.writeTo(out);
				String strMsg = new String(out.toByteArray());
				logger.info(strMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
						
		}

	}

	@Override
	public void initialize(HashMap<String, String> arg0) {
	}

	@Override
	public void compensate(long arg0, long arg1, AbstractGenericOrchestration arg2) {
	}

	@Override
	public boolean cancel(long arg0, long arg1, AbstractGenericOrchestration arg2) {
		return false;
	}

}
