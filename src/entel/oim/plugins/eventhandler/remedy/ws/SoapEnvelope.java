package entel.oim.plugins.eventhandler.remedy.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"SoapHeader","Body"})
@XmlRootElement(name="Envelope",namespace="http://schemas.xmlsoap.org/soap/envelope/")

public class SoapEnvelope {
	@XmlElement(name="Header",namespace="http://schemas.xmlsoap.org/soap/envelope/")
	protected SoapHeader SoapHeader;	


@XmlType(name = "", propOrder = {"AuthenticationInfo"})
@XmlRootElement(name="Header")
@XmlAccessorType(XmlAccessType.FIELD)

	public static class SoapHeader { 
	 @XmlElement(required = true)
	    protected SoapHeader.AuthenticationInfo AuthenticationInfo;
	

   public SoapHeader.AuthenticationInfo getAuthenticationInfo() {
		return AuthenticationInfo;
	}


	public void setAuthenticationInfo(SoapHeader.AuthenticationInfo authenticationInfo) {
		AuthenticationInfo = authenticationInfo;
	}


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userName",
    "password",
    "authentication",
    "locale",
    "timeZone"
})
public static class AuthenticationInfo {

    @XmlElement(required = true)
    protected String userName;
    @XmlElement(required = true)
    protected String password;
    protected String authentication;
    protected String locale;
    protected String timeZone;

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the authentication property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthentication() {
        return authentication;
    }

    /**
     * Sets the value of the authentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthentication(String value) {
        this.authentication = value;
    }

    /**
     * Gets the value of the locale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the value of the locale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocale(String value) {
        this.locale = value;
    }

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZone(String value) {
        this.timeZone = value;
    }
  
}
public SoapHeader( ) {
	
}
public SoapHeader(String user, String pass) {
	this.AuthenticationInfo = new SoapHeader.AuthenticationInfo() ;
	this.AuthenticationInfo.setUserName(user);
	this.AuthenticationInfo.setPassword(pass);
}
}
@XmlElement(name="Body",namespace="http://schemas.xmlsoap.org/soap/envelope/")
protected Body Body;	


@XmlType(name = "", propOrder = {"creacionWO"})
@XmlRootElement(name="Body")
@XmlAccessorType(XmlAccessType.FIELD)
public static class Body {
	@XmlElement(name="CreacionWO")
protected InputMapping1 creacionWO ;
	Body( ) {
		this.creacionWO = new InputMapping1();
	}
}
		
	   	

public 	SoapEnvelope() {
	}
public SoapEnvelope(RemedyVO remedyVO, RemedyUtil ru) {
	ObjectFactory factory=new ObjectFactory();
	this.SoapHeader = new SoapEnvelope.SoapHeader(ru.loadUsername(),ru.loadPassword());
	this.Body = new Body();
	
    this.Body.creacionWO.setSubmitter(ru.loadSubmitter(remedyVO.getType()));
    this.Body.creacionWO.setAssignedTo("");
    ObjectFactory fact = new ObjectFactory();
    //corregir
    JAXBElement<AddRequestForType> addreqtype = fact.createInputMapping1AddRequestFor(AddRequestForType.COMPANY);
    this.Body.creacionWO.setAddRequestFor(addreqtype);
    
    JAXBElement<StatusType> statusType1 = fact.createInputMapping1Status(StatusType.IN_PROGRESS);
  //  this.Body.creacionWO.status=statusType1;
    this.Body.creacionWO.setStatus(StatusType.IN_PROGRESS);
	this.Body.creacionWO.setSummary(ru.loadSummary(remedyVO.getType()));
	this.Body.creacionWO.setCategorizationTier1(ru.loadCatOperacionalNivel1(remedyVO.getType()));
	this.Body.creacionWO.setCategorizationTier2(ru.loadCatOperacionalNivel2(remedyVO.getType()));
	this.Body.creacionWO.setCategorizationTier3(ru.loadCatOperacionalNivel3(remedyVO.getType()));
	this.Body.creacionWO.setDetailedDescription(ru.loadNotes(remedyVO));
	//String priority=ru.loadPriority(remedyVO.getType());
	//this.Body.creacionWO.setPriority(factory.createInputMapping1Priority2((PriorityType.fromValue(priority))));
	//PriorityType priorityType=PriorityType.fromValue(priority);
	//this.Body.creacionWO.setPriority(priorityType);
	//this.Body.creacionWO.setWorkOrderType(value);
	JAXBElement<PriorityType> pri = fact.createInputMapping1Priority2(PriorityType.MEDIUM);    
	this.Body.creacionWO.priority = pri;
	//leer de properties
	this.Body.creacionWO.priority.setValue(PriorityType.MEDIUM);
	//leer de properties
	JAXBElement<WorkOrderTypeType> wo = fact.createInputMapping1WorkOrderType(WorkOrderTypeType.GENERAL);
	this.Body.creacionWO.setWorkOrderType(wo);
	//this.Body.creacionWO.addRequestFor = fact.createInputMapping1AddRequestFor(AddRequestForType.COMPANY);
	
	
	this.Body.creacionWO.setProductCatTier12(ru.loadCatProductoNivel1(remedyVO.getType()));
	this.Body.creacionWO.setProductCatTier22(ru.loadCatProductoNivel2(remedyVO.getType()));
	this.Body.creacionWO.setProductCatTier32(ru.loadCatProductoNivel3(remedyVO.getType()));
	this.Body.creacionWO.setProductName2("");
	this.Body.creacionWO.setProductModelVersion2("");
	this.Body.creacionWO.setSupportOrganization(ru.loadUAPManagerGrupoSoporte(remedyVO.getType()));
	this.Body.creacionWO.setSupportCompany(ru.loadUAPManagerGrupoSoporteEmpresa(remedyVO.getType()));
	this.Body.creacionWO.setSupportGroupName(ru.loadUAMNombreGrupoSoporte(remedyVO.getType()));
	this.Body.creacionWO.setCustomerFirstName(ru.loadFirstName());
	this.Body.creacionWO.setCustomerLastName(ru.loadLastName());
	this.Body.creacionWO.setCustomerCompany(ru.loadCustomerCompany());
	this.Body.creacionWO.setFuenteReportada(ru.loadFuenteReportada(remedyVO.getType()));
	this.Body.creacionWO.setTipoRequerimiento(ru.loadTipoRequerimiento(remedyVO.getType()));
	this.Body.creacionWO.setRequestManagerCompany(ru.loadEmpresaSupportOrganization(remedyVO.getType()));
	this.Body.creacionWO.setManagerSupportOrganization(ru.loadSupportOrganization(remedyVO.getType()));
	this.Body.creacionWO.setZ1DAction(ru.loadAction(remedyVO.getType()));
	this.Body.creacionWO.setLocationCompany(ru.loadEmpresa(remedyVO.getType()));
	this.Body.creacionWO.setManagerSupportGroupName(ru.loadNombreGrupoSoporte(remedyVO.getType()));
	//this.Body.creacionWO.setAddRequestFor(value);
	 
	
}
}
