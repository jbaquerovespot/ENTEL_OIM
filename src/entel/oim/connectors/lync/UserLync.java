package entel.oim.connectors.lync;

/**
 * Class that store all the information of a reconciliated user of Lync
 * @author Oracle
 *
 */
public class UserLync {
	
	private String identity;
	private String samAccountName;
	private boolean audioVideoDisabled;
	private boolean enabled;
	private String registrarPool;
	private boolean enterpriseVoiceEnabled;
	private boolean hostedVoiceMail;
	private String lineURI;
	private String lineServerURI;
	private String privateLine;
	private boolean remoteCallControlTelephonyEnabled;
	private String sipAddress;
	        
	
	/**
	 * Constructor of the class
	 */
	public UserLync() {
		super();
	}

	/**
	 * Constructor of the class
	 * @param identity
	 * @param samAccountName
	 * @param audioVideoDisabled
	 * @param enabled
	 * @param registrarPool
	 * @param enterpriseVoiceEnabled
	 * @param hostedVoiceMail
	 * @param lineURI
	 * @param lineServerURI
	 * @param privateLine
	 * @param remoteCallControlTelephonyEnabled
	 * @param sipAddress
	 */
	public UserLync(String identity, String samAccountName, boolean audioVideoDisabled, boolean enabled,
			String registrarPool, boolean enterpriseVoiceEnabled, boolean hostedVoiceMail, String lineURI,
			String lineServerURI, String privateLine, boolean remoteCallControlTelephonyEnabled, String sipAddress) {
		super();
		this.identity = identity;
		this.samAccountName = samAccountName;
		this.audioVideoDisabled = audioVideoDisabled;
		this.enabled = enabled;
		this.registrarPool = registrarPool;
		this.enterpriseVoiceEnabled = enterpriseVoiceEnabled;
		this.hostedVoiceMail = hostedVoiceMail;
		this.lineURI = lineURI;
		this.lineServerURI = lineServerURI;
		this.privateLine = privateLine;
		this.remoteCallControlTelephonyEnabled = remoteCallControlTelephonyEnabled;
		this.sipAddress = sipAddress;
	}


	public String getIdentity() {
		return identity;
	}


	public void setIdentity(String identity) {
		this.identity = identity;
	}


	public String getSamAccountName() {
		return samAccountName;
	}


	public void setSamAccountName(String samAccountName) {
		this.samAccountName = samAccountName;
	}


	public boolean isAudioVideoDisabled() {
		return audioVideoDisabled;
	}


	public void setAudioVideoDisabled(boolean audioVideoDisabled) {
		this.audioVideoDisabled = audioVideoDisabled;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getRegistrarPool() {
		return registrarPool;
	}


	public void setRegistrarPool(String registrarPool) {
		this.registrarPool = registrarPool;
	}


	public boolean isEnterpriseVoiceEnabled() {
		return enterpriseVoiceEnabled;
	}


	public void setEnterpriseVoiceEnabled(boolean enterpriseVoiceEnabled) {
		this.enterpriseVoiceEnabled = enterpriseVoiceEnabled;
	}


	public boolean isHostedVoiceMail() {
		return hostedVoiceMail;
	}


	public void setHostedVoiceMail(boolean hostedVoiceMail) {
		this.hostedVoiceMail = hostedVoiceMail;
	}


	public String getLineURI() {
		return lineURI;
	}


	public void setLineURI(String lineURI) {
		this.lineURI = lineURI;
	}


	public String getLineServerURI() {
		return lineServerURI;
	}


	public void setLineServerURI(String lineServerURI) {
		this.lineServerURI = lineServerURI;
	}


	public String getPrivateLine() {
		return privateLine;
	}


	public void setPrivateLine(String privateLine) {
		this.privateLine = privateLine;
	}


	public boolean isRemoteCallControlTelephonyEnabled() {
		return remoteCallControlTelephonyEnabled;
	}


	public void setRemoteCallControlTelephonyEnabled(boolean remoteCallControlTelephonyEnabled) {
		this.remoteCallControlTelephonyEnabled = remoteCallControlTelephonyEnabled;
	}


	public String getSipAddress() {
		return sipAddress;
	}


	public void setSipAddress(String sipAddress) {
		this.sipAddress = sipAddress;
	}
	
	    
}
