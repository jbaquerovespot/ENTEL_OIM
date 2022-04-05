package entel.oim.connectors.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



//import Thor.API.tcResultSet;
//import Thor.API.Operations.tcLookupOperationsIntf;
//import oracle.iam.platform.Platform;


public class SuccessFactorsConnection {
	
	private final static String className = SuccessFactorsConnection.class.getName();
	private static Logger logger = Logger.getLogger(className);
	
	//private static final tcLookupOperationsIntf lookupOprInf= Platform.getService(tcLookupOperationsIntf.class);
    
	
	/**
	 * Call to the Identity provider (iDP)
	 * @param hostIDP Host of the iDP Server
	 * @param client_id Client Id from SuccessFacotors
	 * @param user_id User Id from SuccessFacotors
	 * @param token_url URL from token service from SuccessFacotors
	 * @param private_key Private key from SuccessFacotors
	 * @return Assertion String for OAUTH connection
	 */
	private static String callIDP(String hostIDP, String client_id,String user_id,String token_url, String private_key, String proxyHost, String proxyPort, String proxyUser, String proxyPassword) { 

		logger.entering(className, "callIDP");
		String idpResult = null;
		
		try {
	
			logger.fine("Establishing the headers for the request");
			URL url = new URL(hostIDP);
			HttpURLConnection conn = null;
			if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null && !proxyPort.isEmpty()) {
				conn = (HttpURLConnection) url.openConnection(getProxy(proxyHost, proxyPort, proxyUser, proxyPassword));
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	
			logger.fine("Establishing the body of the request");
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
			osw.write( "client_id="+client_id 
					 + "&user_id="+user_id
					 + "&token_url="+token_url
					 + "&private_key="+private_key);
			osw.flush();
			osw.close();
			os.close();
		
			logger.fine("Trying connection...");
			conn.connect();
	
			logger.fine("Verifying success on the connection");
			if (conn.getResponseCode() != 200) {
				logger.severe("Error establishing connection wiht idP. Error code: " +conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			} else {
				logger.fine("Connection success to iDP");
				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
		
				String output;
				logger.finest("Output from iDP server...");
				while ((output = br.readLine()) != null) {
					logger.finest(output);
					idpResult = output;
				}
		
				logger.fine("Disconnection from server.");
				conn.disconnect();
		
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "callIDP - Unexpected error",e);

		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "callIDP - Unexpected error", e);
		}
	
		logger.exiting(className, "callIDP",idpResult);
		return idpResult;

	}
	
	/**
     * 
     * Method used to get proxy settings with authentication
     *
     * @param ip
     * 			the proxy IP
     * @param port
     * 			the proxy port
     * @param username
     * 			the proxy username
     * @param password
     * 			the proxy password
     * @return 
     * 			THe proxy object
     */
    private static Proxy getProxy(String ip, String port, String username, String password) {
    	logger.entering(className, "getProxy");
    	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,Long.valueOf(port).intValue()));
    	
    	logger.finer("Checking if username is required for proxy");
    	if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
    		Authenticator authenticator = new Authenticator() {
        		public PasswordAuthentication getPasswordAuthentication() {
    	        return (new PasswordAuthentication(username, password.toCharArray()));
    	        }
    	    };
    	    Authenticator.setDefault(authenticator);
    	}
    	
    	logger.exiting(className, "getProxy");
	    return proxy;
    }
    
    /**
	 * Call to the generation access token of Success Factors
	 * @param hostToken Host of the Token Server
	 * @param company_id Company Id from SuccessFactors
	 * @param grant_type Grant type from SuccessFactors
	 * @param client_id Client Id from SuccessFactors
	 * @param user_id User Id from SuccessFactors
	 * @param idpResponse URL from token service from SuccessFactors
	 * @return Token result for OAUTH connection
	 */
	private static String callGenerateToken(String hostToken, String company_id, String client_id,String grant_type,String user_id,String idpResponse,String proxyHost, String proxyPort, String proxyUser, String proxyPassword) { 
		
		logger.entering(className, "callGenerateToken");
		String tokenResult = null;
		
		try {

			logger.fine("Establishing the headers for the request");  
			URL url = new URL(hostToken);
			HttpURLConnection conn = null;
			if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null && !proxyPort.isEmpty()) {
				conn = (HttpURLConnection) url.openConnection(getProxy(proxyHost, proxyPort, proxyUser, proxyPassword));
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			logger.fine("Establishing the body of the request");
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
			osw.write("company_id="+company_id
					 + "&client_id="+client_id
					 + "&grant_type="+grant_type
					 + "&user_id="+user_id
					 + "&assertion="+idpResponse);
			osw.flush();
			osw.close();
			os.close();
		
			logger.fine("Trying connection...");
			conn.connect();

			logger.fine("Verifying success on the connection");
			if (conn.getResponseCode() != 200) {
				logger.severe("Error establishing connection with Token Service. Error code: " +conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			} else {
				logger.fine("Connection success to Token Service");
				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
		
				String output;
				logger.finest("Output from token server...");
				while ((output = br.readLine()) != null) {
					logger.finest(output);
					tokenResult = output;
				}
		
				logger.fine("Disconnection from server.");
				conn.disconnect();
			}
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "callGenerateToken - Unexpected error", e);

		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "callGenerateToken - Unexpected error", e);
		}
	
		logger.exiting(className, "callGenerateToken",tokenResult);
		return tokenResult;

	}
	
	
	/**
	 * Parse the result of the Token Service and get the access token
	 * @param tokenResult Result of the Token Service
	 * @return the access token of the Success Factors
	 */
	private static String getAccessToken(String tokenResult) {
		
		logger.entering(className, "getAccessToken",tokenResult);
		ObjectMapper objectMapper = new ObjectMapper();
		String access_token = null;
		
		try {
			
			logger.fine("Reading the result of the Token Service");
			Map<String, Object> map = objectMapper.readValue(tokenResult, new TypeReference<Map<String,Object>>(){});
			
			logger.fine("Getting the attribute access_token");
			access_token = (String)map.get("access_token");
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getAccessToken - Unexpected error", e);
			
		}
		
		logger.exiting(className, "getAccessToken",access_token);
		return access_token;
	}
	
	
	
	/**
	 * 
	 * @param itResourceDetails
	 * 						IT Resource details of the SuccessFactors connector
	 * @return
	 * 			Access Token for SuccessFactors invocations
	 */
	public static String getSuccessFactorsConnection(Map<String, String> itResourceDetails) {
		
		logger.entering(className, "getSuccessFactorsConnection");
		
		logger.fine("Calling the iDP Service from Success Factors");
		String idpResponse = callIDP(itResourceDetails.get("authorizationUrl"),
									 itResourceDetails.get("clientId"),
									 itResourceDetails.get("username"),
									 itResourceDetails.get("authenticationServerUrl"),
									 getPrivateKeyFromCertificate(itResourceDetails.get("privateKeyLocation")),
									 itResourceDetails.get("proxyHost"),
									 itResourceDetails.get("proxyPort"),
									 itResourceDetails.get("proxyUser"),
									 itResourceDetails.get("proxyPassword"));
		logger.finest("iDP service returned: " + idpResponse);
		
		/*Test*/
		System.out.println("idpResponse : " + idpResponse);
		
		logger.fine("Calling the Token Service from Success Factors");
		String tokenResponse = callGenerateToken(	itResourceDetails.get("authenticationServerUrl"),
													itResourceDetails.get("companyId"),
													itResourceDetails.get("clientId"),
													itResourceDetails.get("grantType"),
													itResourceDetails.get("username"),
													idpResponse,
													itResourceDetails.get("proxyHost"),
													itResourceDetails.get("proxyPort"),
													itResourceDetails.get("proxyUser"),
													itResourceDetails.get("proxyPassword"));
		logger.finest("Token service returned: " + idpResponse);
		
		/*Test*/
		System.out.println("tokenResponse : " + tokenResponse);
		
		String accessToken = getAccessToken(tokenResponse);
		logger.finest("Access token: " + accessToken);
		
		logger.exiting(className, "getSuccessFactorsConnection",accessToken);
		return accessToken;
		
	}
	
	
	
	/**
     * Return the private encrypted key of a certificate
     * @param path Path of the certificate
     * @return The private encrypted key from the certificate
     */
    private static String getPrivateKeyFromCertificate(String path) {
    	
    	logger.entering(className, "getPrivateKeyFromCertificate");
    	String privateKey = null;
		
    	
    	try {
    		
    		logger.fine("Reading the certificate file");
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			privateKey = new String(encoded, Charset.defaultCharset());
			
			logger.fine("Extracting the private key string");
			privateKey = (privateKey.substring(privateKey.indexOf("-----BEGIN ENCRYPTED PRIVATE KEY-----"),privateKey.indexOf("-----END ENCRYPTED PRIVATE KEY-----"))).replace("-----BEGIN ENCRYPTED PRIVATE KEY-----", "").trim();
			  
		
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getPrivateKeyFromCertificate - Unexpected error", e);
			
			/* test */
			System.out.println("privateKey" + privateKey);
		}
    		  
    	logger.exiting(className, "getPrivateKeyFromCertificate",privateKey);
		return privateKey;
		
    }
    
    
    
    /**
	 * Call to service in SuccessFactors
	 * @param hostURL Host URl of the SuccessFactors
	 * @param service Service to call
	 * @param accessToken Access Token of the session of SuccessFactors
	 * @return XML result
	 */
	public static String getServiceResponse(Map<String, String> itResourceDetails, String service, String accessToken) { 
		
		logger.entering(className, "getServiceResponse",service);
		String result = null;
		
		try {
			
			logger.finer("Getting connection properties of SuccessFactors");  
			String host = itResourceDetails.get("clientUrl");
			String proxyHost = itResourceDetails.get("proxyHost"); 
			String proxyPort = itResourceDetails.get("proxyPort"); 
			String proxyUser = itResourceDetails.get("proxyUser"); 
			String proxyPassword = itResourceDetails.get("proxyPassword");
			
			logger.finer("Constructing URL to call");
			String urlService = host + "odata/v2/"+ service;
			logger.finest("URL to call: " + urlService);  
			
			logger.finer("Establishing the headers for the request");  
			URL url = new URL(urlService);
			HttpURLConnection conn = null;
			if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null && !proxyPort.isEmpty()) {
				conn = (HttpURLConnection) url.openConnection(getProxy(proxyHost, proxyPort, proxyUser, proxyPassword));
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			logger.fine("Trying connection...");
			conn.connect();

			logger.fine("Verifying success on the connection");
			if (conn.getResponseCode() != 200) {
				logger.severe("Error establishing connection with the Service. Error code: " +conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			} else {
				logger.fine("Connection success to the Service");
				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
		
				logger.finest("Saving the Output from the Service");
				result =br.lines().collect(Collectors.joining());;
		
				logger.fine("Disconnection from server.");
				conn.disconnect();
			}
		
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getServiceResponse - Unexpected error",e);

		} 
		
		logger.exiting(className, "getServiceResponse",result);
		return result;

	}
	
	
	/**
	 * Get more codes from service (if exists)
	 * @param xml XML response from service of Success Factors
	 * @return Next search result page from service
	 * @throws Exception 
	 */
	public static String getMoreCodeValues (String xml) throws Exception {
		
		logger.entering(className, "getMoreCodeValues");
		String href = null;
		
		try {
		
			logger.fine("Parsing the XML to Document");
			SAXReader saxBuilder = new SAXReader();
			Document document = saxBuilder.read(new StringReader(xml));
			
			logger.fine("Get all elements returned by the service");
			List<Node> list =  document.selectNodes("//feed/*");

			logger.fine("Loop over the links in the XML");
			for (int i = 0; i < list.size(); i++) {
				if ( "link".equals(list.get(i).getName())) {
					
					logger.fine("Get the the atribute rel from the current element");
					Element link = (Element )list.get(i);
			        String val = link.attribute("rel").getValue();
					
			        if ("next".equals(val)) {
			        	logger.finest("Exists more information to fetch from service.");
			       
			        	href = link.attribute("href").getValue();
			        	break;
			        }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getMoreCodeValues - Unexpected error",e);
			throw e;
		}
		
		logger.exiting(className, "getMoreCodeValues",href);
		return href;
		
	}
	
	
	
	/**
	 * Call to service in SuccessFactors
	 * @param hostURL Host URl of the SuccessFactors
	 * @param service Service to call
	 * @param accessToken Access Token of the session of SuccessFactors
	 * @return Flag of success
	 */
	public static boolean setServiceRequest(Map<String, String> itResourceDetails, String service, String payload, String accessToken) { 
		
		logger.entering(className, "setServiceRequest",service);
		boolean success = false;
		
		try {
			
			logger.finer("Getting connection properties of SuccessFactors");  
			String host = itResourceDetails.get("clientUrl");
			String proxyHost = itResourceDetails.get("proxyHost"); 
			String proxyPort = itResourceDetails.get("proxyPort"); 
			String proxyUser = itResourceDetails.get("proxyUser"); 
			String proxyPassword = itResourceDetails.get("proxyPassword");
			
			logger.finer("Constructing URL to call");
			String urlService = host + "odata/v2/"+ service;
			logger.finest("URL to call: " + urlService);  
			
			logger.finer("Establishing the headers for the request");  
			URL url = new URL(urlService);
			HttpURLConnection conn = null;
			if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null && !proxyPort.isEmpty()) {
				conn = (HttpURLConnection) url.openConnection(getProxy(proxyHost, proxyPort, proxyUser, proxyPassword));
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("x-http-method", "MERGE");
			
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(payload);
			out.flush();
			out.close();
			
			logger.fine("Verifying success on the connection");
			if (conn.getResponseCode() != 200) {
				logger.severe("Error establishing connection with the Service. Error code: " +conn.getResponseCode());
			} else {
				logger.fine("Connection success to the Service");
				success = true;
				
			}
			
			logger.fine("Disconnection from server.");
			conn.disconnect();
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "setServiceRequest - Unexpected error",e);

		} 
		
		logger.exiting(className, "setServiceRequest",success);
		return success;

	}
	

	

}
