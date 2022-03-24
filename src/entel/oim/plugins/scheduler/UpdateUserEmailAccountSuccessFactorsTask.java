package entel.oim.plugins.scheduler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import entel.oim.connectors.utilities.SuccessFactorsConnection;
import oracle.iam.platform.Platform;
import oracle.iam.scheduler.vo.TaskSupport;

public class UpdateUserEmailAccountSuccessFactorsTask extends TaskSupport {
	private static final String className = UpdateUserEmailAccountSuccessFactorsTask.class.getName();
	private static final Logger logger = Logger.getLogger(className);

	private tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);
	private tcITResourceInstanceOperationsIntf itResOps = Platform.getService(tcITResourceInstanceOperationsIntf.class);

	@Override
	public void execute(HashMap hm) throws Exception {

		logger.entering(className, "execute", hm);
		try {

			logger.finer("Loading parameters");
			String itResource = (String) hm.get("IT Resource Name");
			String service = (String) hm.get("Service");
			String serviceContactInfo = (String) hm.get("Contact Info Service");
			String lookupUserLoginName = (String) hm.get("Lookup User Login");
			String lookupUserEmailName = (String) hm.get("Lookup User Email");

			logger.log(Level.FINER, "Getting details of IT Resource [{0}]", itResource);
			Map<String, String> itResourceDetails = getITResourceDetails(itResource);

			logger.finer("Call to establish connection with SuccessFactors");
			String accessToken = SuccessFactorsConnection.getSuccessFactorsConnection(itResourceDetails);

			logger.finer("Call to update pending UserLogin in SuccessFactors");
			updateUserAttributeSuccessFactorsUsername(itResourceDetails, accessToken, lookupUserLoginName, service);

			logger.finer("Call to update pending Email in SuccessFactors");
			updateUserAttributeSuccessFactorsEmail(itResourceDetails, accessToken, lookupUserEmailName, service,
					serviceContactInfo);

			logger.exiting(className, "execute");

		} catch (Exception e) {
			// Do not stop the task if an exception occurs.
			e.printStackTrace();
			logger.log(Level.SEVERE, "execute - Unexpected error", e);
			throw e;

		}

	}

	/**
	 * Update the pending attribute of the user in SuccessFactors
	 * 
	 * @param itResourceDetails
	 *            It Resource details of SuccessFactors
	 * @param accessToken
	 *            Access Token of SuccessFactors
	 * @param lookupName
	 *            Lookups of user/attribute pending to update
	 * @param service
	 *            Service to call to SuccessFactors
	 * @param attribute
	 *            Attribute element to update
	 * @throws Exception
	 */
	private void updateUserAttributeSuccessFactorsUsername(Map<String, String> itResourceDetails, String accessToken,
			String lookupName, String service) throws Exception {

		logger.entering(className, "updateUserAttributeSuccessFactorsUsername", lookupName);
		String usernameAttr = "username";
		String loginMethodAttr = "loginMethod";
		try {
			logger.fine("Getting lookup values from  " + lookupName);
			tcResultSet rs = lookupOps.getLookupValues(lookupName);

			logger.finer("Looping over users login to updates");
			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				String code = rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
				String decode = rs.getStringValue("Lookup Definition.Lookup Code Information.Decode");
				logger.finer("Code: " + code + " | Decode: " + decode);

				logger.fine("Checking if user is active in Success Factors");
				String filterUser = service + "?$filter=userId%20eq%20'" + code + "'";
				String usersXML = SuccessFactorsConnection.getServiceResponse(itResourceDetails, filterUser,
						accessToken);
				logger.finest("Response from Success Factors: " + usersXML);

				logger.fine("Constructing user id list");
				List<String> usersIdList = getUsersIdList(usersXML, itResourceDetails, accessToken);

				logger.fine("Checking if user is active in SuccessFactors");
				if (usersIdList.size() > 0) {
					logger.finest("User is active");
					logger.finer("Constructing parameters to call");
					String filter = service + "('" + code + "')?$format=JSON";
					String payload = "{ \"" + usernameAttr + "\" : \"" + decode + "\", \"" + loginMethodAttr
							+ "\" : \"PWD\" }";

					logger.finer("Calling " + filter + " service from Success Factors with " + payload);
					boolean success = SuccessFactorsConnection.setServiceRequest(itResourceDetails, filter, payload,
							accessToken);
					logger.finest("Success from Success Factors: " + success);
					if (success) {
						lookupOps.removeLookupValue(lookupName,
								rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"));
					} else {
						logger.log(Level.SEVERE, "Unable to update the " + usernameAttr + " and " + loginMethodAttr
								+ " of the User " + code + " in Success Factors");
						continue;
					}

				}

			}

			logger.exiting(className, "updateUserAttributeSuccessFactorsUsername");

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateUserAttributeSuccessFactorsUsername - Unexpected error", e);
			throw new Exception("Unable to update the User in Success Factors");
		}

	}

	/**
	 * Update the pending attribute of the user in SuccessFactors
	 * 
	 * @param itResourceDetails
	 *            It Resource details of SuccessFactors
	 * @param accessToken
	 *            Access Token of SuccessFactors
	 * @param lookupName
	 *            Lookups of user/attribute pending to update
	 * @param service
	 *            Service to call to SuccessFactors
	 * @param serviceContactInfo
	 *            Service to call to SuccessFactors to the primary contact info
	 * @param attribute
	 *            Attribute element to update
	 * @throws Exception
	 */
	private void updateUserAttributeSuccessFactorsEmail(Map<String, String> itResourceDetails, String accessToken,
			String lookupName, String service, String serviceContactInfo) throws Exception {

		logger.entering(className, "updateUserAttributeSuccessFactorsEmail", lookupName);
		String emailAttr = "email";
		String emailAddressAttr = "emailAddress";
		try {
			logger.fine("Getting lookup values from  " + lookupName);
			tcResultSet rs = lookupOps.getLookupValues(lookupName);

			logger.finer("Looping over users login to updates");
			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				String code = rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
				String decode = rs.getStringValue("Lookup Definition.Lookup Code Information.Decode");

				logger.finer("Code: " + code + " | Decode: " + decode);

				if (decode != null && !decode.isEmpty()) {
					logger.fine("Validating email format: " + decode);

					String[] split = decode.split("@");

					for (int j = 0; j < split.length; j++) {
						if (j == 0) {
							decode = split[j].toUpperCase().trim() + "@";
						} else {
							decode = decode.concat(split[j]).trim();
						}
					}

					logger.fine("Email format validated: " + decode);

				}

				logger.fine("Checking if user is active in Success Factors");
				String filterUser = service + "?$filter=userId%20eq%20'" + code + "'";
				String usersXML = SuccessFactorsConnection.getServiceResponse(itResourceDetails, filterUser,
						accessToken);
				logger.finest("Response from Success Factors: " + usersXML);

				logger.fine("Constructing user id list");
				List<String> usersIdList = getUsersIdList(usersXML, itResourceDetails, accessToken);

				logger.fine("Checking if user is active in SuccessFactors");
				if (usersIdList.size() > 0) {
					logger.finest("User is active");
					logger.finer("Constructing parameters to call");
					String filter = service + "('" + code + "')?$format=JSON";
					String payload = "{ \"" + emailAttr + "\" : \"" + decode + "\" }";

					logger.finer("Calling " + filter + " service from Success Factors with " + payload);
					boolean success = SuccessFactorsConnection.setServiceRequest(itResourceDetails, filter, payload,
							accessToken);
					logger.finest("Success from Success Factors: " + success);
					if (success) {

						logger.fine("Getting id of the Entity PerEmail of the user");
						String filterEmail = serviceContactInfo + "?$filter=personIdExternal%20eq%20'" + code
								+ "'%20and%20isPrimary%20eq%20true";
						String emailXML = SuccessFactorsConnection.getServiceResponse(itResourceDetails, filterEmail,
								accessToken);
						logger.finest("Response from Success Factors: " + usersXML);

						logger.fine("Getting the ID of the Email");
						String entityId = getEntityId(emailXML, itResourceDetails, accessToken, serviceContactInfo);

						logger.finer("Constructing parameters to call de Contact Info");
						filter = "upsert?$format=json";
						payload = "{\"__metadata\":{\"uri\":\"" + entityId + "\"},\"" + emailAddressAttr + "\" : \""
								+ decode + "\" }";

						logger.finer("Calling " + filter + " service from Success Factors with " + payload);
						success = SuccessFactorsConnection.setServiceRequest(itResourceDetails, filter, payload,
								accessToken);
						logger.finest("Success from Success Factors: " + success);
						if (success) {
							lookupOps.removeLookupValue(lookupName,
									rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"));
						} else {
							logger.log(Level.SEVERE, "Unable to update the " + emailAddressAttr + " of the User " + code
									+ " in Success Factors");
							continue;
						}

					} else {
						logger.log(Level.SEVERE,
								"Unable to update the " + emailAttr + " of the User " + code + " in Success Factors");
						continue;
					}

				}

			}

			logger.exiting(className, "updateUserAttributeSuccessFactorsEmail");

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "updateUserAttributeSuccessFactorsEmail - Unexpected error", e);
			throw new Exception("Unable to update the " + emailAttr + " and " + emailAddressAttr
					+ " of the User in Success Factors");
		}

	}

	@Override
	public HashMap getAttributes() {
		return null;
	}

	@Override
	public void setAttributes() {

	}

	/**
	 * Return the user id list of employee of Success Factors
	 * 
	 * @param xmlUsers
	 *            XML response from service User of Success Factors
	 * @param itResourceDetails
	 *            The it resource details
	 * @param accessToken
	 *            Token to call the service
	 * @return The user id list (rut) of employee of Success Factors
	 */
	public static List<String> getUsersIdList(String xmlUsers, Map<String, String> itResourceDetails,
			String accessToken) {

		logger.entering(className, "getUsersIdList");
		List<String> usersIdList = new ArrayList<String>();

		try {

			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlUsers));

			logger.fine("Get all elements returned by the service");
			List<Node> list = document.selectNodes("//feed/*");

			logger.fine("Loop over the users in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ("entry".equals(list.get(i).getName())) {

					logger.fine("Get the userId of the current user");
					Element entry = (Element) list.get(i);
					Element content = (Element) entry.element("content");
					Element properties = (Element) content.element("properties");
					Element userId = (Element) properties.element("userId");

					logger.finest("Adding the userId: \"" + userId.getStringValue() + "\" to the list");
					usersIdList.add(userId.getStringValue());
				}

			}

		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getUsersIdList - Unexpected error", e);

		}

		logger.exiting(className, "getUsersIdList");
		return usersIdList;

	}

	/**
	 * Return the Entity id of the response of Success Factors
	 * 
	 * @param xmlUsers
	 *            XML response from service User of Success Factors
	 * @param itResourceDetails
	 *            The it resource details
	 * @param accessToken
	 *            Token to call the service
	 * @return The entity id list of the response of Success Factors
	 */
	public static String getEntityId(String xmlUsers, Map<String, String> itResourceDetails, String accessToken,
			String service) {

		logger.entering(className, "getEntityId");
		String entityId = null;

		try {

			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xmlUsers));

			logger.fine("Get all elements returned by the service");
			List<Node> list = document.selectNodes("//feed/*");

			logger.fine("Loop over the emails in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ("entry".equals(list.get(i).getName())) {

					logger.fine("Get the id of the current entity");
					Element entry = (Element) list.get(i);
					Element idElement = (Element) entry.element("id");
					String idStr = idElement.getStringValue();
					idStr = idStr.substring(idStr.indexOf(service));

					logger.finest("Setting the EntityId: \"" + idStr + "\" of the response");
					entityId = idStr;
					break;
				}

			}

		} catch (DocumentException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getEntityId - Unexpected error", e);

		}

		logger.exiting(className, "getEntityId");
		return entityId;

	}

	/**
	 * Finds the specified IT Resource details
	 * 
	 * @param itResourceName
	 *            the name of the IT Resource to find. Case sensitive
	 * @return a map with the found IT Resource details
	 * @throws tcAPIException
	 * @throws tcColumnNotFoundException
	 * @throws tcITResourceNotFoundException
	 */
	public Map<String, String> getITResourceDetails(String itResourceName)
			throws tcAPIException, tcColumnNotFoundException, tcITResourceNotFoundException {

		logger.entering(className, "getITResourceDetails");

		HashMap<String, String> searchcriteria = new HashMap<>();
		searchcriteria.put("IT Resources.Name", itResourceName);

		tcResultSet trs = itResOps.findITResourceInstances(searchcriteria);
		int rowCount = trs.getRowCount();
		long itResourceKey = -1;

		if (rowCount > 0) {
			trs.goToRow(0);
			itResourceKey = trs.getLongValue("IT Resource.Key");
		}

		Map<String, String> itAttrib = new HashMap<>();
		if (itResourceKey > 0) {
			tcResultSet paramsRs = itResOps.getITResourceInstanceParameters(itResourceKey);
			for (int i = 0; i < paramsRs.getRowCount(); i++) {
				paramsRs.goToRow(i);

				String name = paramsRs.getStringValue("IT Resources Type Parameter.Name");
				String value = paramsRs.getStringValue("IT Resources Type Parameter Value.Value");

				itAttrib.put(name, value);
			}
		}

		logger.exiting(className, "getITResourceDetails");
		return itAttrib;
	}

}