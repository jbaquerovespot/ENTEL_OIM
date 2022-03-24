
package entel.oim.plugins.eventhandler.remedy.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for InputMapping1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InputMapping1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Submitter" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Assigned_To" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Status" type="{urn:CreacionWO}StatusType"/&gt;
 *         &lt;element name="Summary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Categorization_Tier_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Categorization_Tier_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Categorization_Tier_3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Detailed_Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Priority" type="{urn:CreacionWO}PriorityType" minOccurs="0"/&gt;
 *         &lt;element name="Work_Order_Type" type="{urn:CreacionWO}Work_Order_TypeType" minOccurs="0"/&gt;
 *         &lt;element name="Product_Cat_Tier_1_2_" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Product_Cat_Tier_2__2_" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Product_Cat_Tier_3__2_" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Product_Name__2_" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Product_Model_Version__2_" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Support_Organization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Support_Company" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Support_Group_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Customer_First_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Customer_Last_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Customer_Company" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Fuente_Reportada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TipoRequerimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RequestManagerCompany" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ManagerSupportOrganization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ManagerSupportGroupName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="z1D_Action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LocationCompany" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AddRequestFor" type="{urn:CreacionWO}AddRequestForType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputMapping1", propOrder = {
    "submitter",
    "assignedTo",
    "status",
    "summary",
    "categorizationTier1",
    "categorizationTier2",
    "categorizationTier3",
    "detailedDescription",
    "priority",
    "workOrderType",
    "productCatTier12",
    "productCatTier22",
    "productCatTier32",
    "productName2",
    "productModelVersion2",
    "supportOrganization",
    "supportCompany",
    "supportGroupName",
    "customerFirstName",
    "customerLastName",
    "customerCompany",
    "fuenteReportada",
    "tipoRequerimiento",
    "requestManagerCompany",
    "managerSupportOrganization",
    "managerSupportGroupName",
    "z1DAction",
    "locationCompany",
    "addRequestFor"
})
public class InputMapping1 {

    @XmlElement(name = "Submitter", required = true)
    protected String submitter;
    @XmlElement(name = "Assigned_To")
    protected String assignedTo;
    @XmlElement(name = "Status", required = true)
    @XmlSchemaType(name = "string")
    protected StatusType status;
    @XmlElement(name = "Summary")
    protected String summary;
    @XmlElement(name = "Categorization_Tier_1")
    protected String categorizationTier1;
    @XmlElement(name = "Categorization_Tier_2")
    protected String categorizationTier2;
    @XmlElement(name = "Categorization_Tier_3")
    protected String categorizationTier3;
    @XmlElement(name = "Detailed_Description")
    protected String detailedDescription;
    @XmlElementRef(name = "Priority", namespace = "urn:CreacionWO", type = JAXBElement.class, required = false)
    protected JAXBElement<PriorityType> priority;
    @XmlElementRef(name = "Work_Order_Type", namespace = "urn:CreacionWO", type = JAXBElement.class, required = false)
    protected JAXBElement<WorkOrderTypeType> workOrderType;
    @XmlElement(name = "Product_Cat_Tier_1_2_")
    protected String productCatTier12;
    @XmlElement(name = "Product_Cat_Tier_2__2_")
    protected String productCatTier22;
    @XmlElement(name = "Product_Cat_Tier_3__2_")
    protected String productCatTier32;
    @XmlElement(name = "Product_Name__2_")
    protected String productName2;
    @XmlElement(name = "Product_Model_Version__2_")
    protected String productModelVersion2;
    @XmlElement(name = "Support_Organization")
    protected String supportOrganization;
    @XmlElement(name = "Support_Company")
    protected String supportCompany;
    @XmlElement(name = "Support_Group_Name")
    protected String supportGroupName;
    @XmlElement(name = "Customer_First_Name")
    protected String customerFirstName;
    @XmlElement(name = "Customer_Last_Name")
    protected String customerLastName;
    @XmlElement(name = "Customer_Company")
    protected String customerCompany;
    @XmlElement(name = "Fuente_Reportada")
    protected String fuenteReportada;
    @XmlElement(name = "TipoRequerimiento")
    protected String tipoRequerimiento;
    @XmlElement(name = "RequestManagerCompany")
    protected String requestManagerCompany;
    @XmlElement(name = "ManagerSupportOrganization")
    protected String managerSupportOrganization;
    @XmlElement(name = "ManagerSupportGroupName")
    protected String managerSupportGroupName;
    @XmlElement(name = "z1D_Action")
    protected String z1DAction;
    @XmlElement(name = "LocationCompany")
    protected String locationCompany;
    @XmlElementRef(name = "AddRequestFor", namespace = "urn:CreacionWO", type = JAXBElement.class, required = false)
    protected JAXBElement<AddRequestForType> addRequestFor;

    /**
     * Gets the value of the submitter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubmitter() {
        return submitter;
    }

    /**
     * Sets the value of the submitter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubmitter(String value) {
        this.submitter = value;
    }

    /**
     * Gets the value of the assignedTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * Sets the value of the assignedTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignedTo(String value) {
        this.assignedTo = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSummary(String value) {
        this.summary = value;
    }

    /**
     * Gets the value of the categorizationTier1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategorizationTier1() {
        return categorizationTier1;
    }

    /**
     * Sets the value of the categorizationTier1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategorizationTier1(String value) {
        this.categorizationTier1 = value;
    }

    /**
     * Gets the value of the categorizationTier2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategorizationTier2() {
        return categorizationTier2;
    }

    /**
     * Sets the value of the categorizationTier2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategorizationTier2(String value) {
        this.categorizationTier2 = value;
    }

    /**
     * Gets the value of the categorizationTier3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategorizationTier3() {
        return categorizationTier3;
    }

    /**
     * Sets the value of the categorizationTier3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategorizationTier3(String value) {
        this.categorizationTier3 = value;
    }

    /**
     * Gets the value of the detailedDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailedDescription() {
        return detailedDescription;
    }

    /**
     * Sets the value of the detailedDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailedDescription(String value) {
        this.detailedDescription = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PriorityType }{@code >}
     *     
     */
    public JAXBElement<PriorityType> getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PriorityType }{@code >}
     *     
     */
    public void setPriority(JAXBElement<PriorityType> value) {
        this.priority = value;
    }

    /**
     * Gets the value of the workOrderType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link WorkOrderTypeType }{@code >}
     *     
     */
    public JAXBElement<WorkOrderTypeType> getWorkOrderType() {
        return workOrderType;
    }

    /**
     * Sets the value of the workOrderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link WorkOrderTypeType }{@code >}
     *     
     */
    public void setWorkOrderType(JAXBElement<WorkOrderTypeType> value) {
        this.workOrderType = value;
    }

    /**
     * Gets the value of the productCatTier12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCatTier12() {
        return productCatTier12;
    }

    /**
     * Sets the value of the productCatTier12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCatTier12(String value) {
        this.productCatTier12 = value;
    }

    /**
     * Gets the value of the productCatTier22 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCatTier22() {
        return productCatTier22;
    }

    /**
     * Sets the value of the productCatTier22 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCatTier22(String value) {
        this.productCatTier22 = value;
    }

    /**
     * Gets the value of the productCatTier32 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCatTier32() {
        return productCatTier32;
    }

    /**
     * Sets the value of the productCatTier32 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCatTier32(String value) {
        this.productCatTier32 = value;
    }

    /**
     * Gets the value of the productName2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductName2() {
        return productName2;
    }

    /**
     * Sets the value of the productName2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductName2(String value) {
        this.productName2 = value;
    }

    /**
     * Gets the value of the productModelVersion2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductModelVersion2() {
        return productModelVersion2;
    }

    /**
     * Sets the value of the productModelVersion2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductModelVersion2(String value) {
        this.productModelVersion2 = value;
    }

    /**
     * Gets the value of the supportOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupportOrganization() {
        return supportOrganization;
    }

    /**
     * Sets the value of the supportOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupportOrganization(String value) {
        this.supportOrganization = value;
    }

    /**
     * Gets the value of the supportCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupportCompany() {
        return supportCompany;
    }

    /**
     * Sets the value of the supportCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupportCompany(String value) {
        this.supportCompany = value;
    }

    /**
     * Gets the value of the supportGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupportGroupName() {
        return supportGroupName;
    }

    /**
     * Sets the value of the supportGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupportGroupName(String value) {
        this.supportGroupName = value;
    }

    /**
     * Gets the value of the customerFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerFirstName() {
        return customerFirstName;
    }

    /**
     * Sets the value of the customerFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerFirstName(String value) {
        this.customerFirstName = value;
    }

    /**
     * Gets the value of the customerLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerLastName() {
        return customerLastName;
    }

    /**
     * Sets the value of the customerLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerLastName(String value) {
        this.customerLastName = value;
    }

    /**
     * Gets the value of the customerCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerCompany() {
        return customerCompany;
    }

    /**
     * Sets the value of the customerCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerCompany(String value) {
        this.customerCompany = value;
    }

    /**
     * Gets the value of the fuenteReportada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuenteReportada() {
        return fuenteReportada;
    }

    /**
     * Sets the value of the fuenteReportada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuenteReportada(String value) {
        this.fuenteReportada = value;
    }

    /**
     * Gets the value of the tipoRequerimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRequerimiento() {
        return tipoRequerimiento;
    }

    /**
     * Sets the value of the tipoRequerimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRequerimiento(String value) {
        this.tipoRequerimiento = value;
    }

    /**
     * Gets the value of the requestManagerCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestManagerCompany() {
        return requestManagerCompany;
    }

    /**
     * Sets the value of the requestManagerCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestManagerCompany(String value) {
        this.requestManagerCompany = value;
    }

    /**
     * Gets the value of the managerSupportOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagerSupportOrganization() {
        return managerSupportOrganization;
    }

    /**
     * Sets the value of the managerSupportOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagerSupportOrganization(String value) {
        this.managerSupportOrganization = value;
    }

    /**
     * Gets the value of the managerSupportGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagerSupportGroupName() {
        return managerSupportGroupName;
    }

    /**
     * Sets the value of the managerSupportGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagerSupportGroupName(String value) {
        this.managerSupportGroupName = value;
    }

    /**
     * Gets the value of the z1DAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZ1DAction() {
        return z1DAction;
    }

    /**
     * Sets the value of the z1DAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZ1DAction(String value) {
        this.z1DAction = value;
    }

    /**
     * Gets the value of the locationCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationCompany() {
        return locationCompany;
    }

    /**
     * Sets the value of the locationCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationCompany(String value) {
        this.locationCompany = value;
    }

    /**
     * Gets the value of the addRequestFor property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AddRequestForType }{@code >}
     *     
     */
    public JAXBElement<AddRequestForType> getAddRequestFor() {
        return addRequestFor;
    }

    /**
     * Sets the value of the addRequestFor property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AddRequestForType }{@code >}
     *     
     */
    public void setAddRequestFor(JAXBElement<AddRequestForType> value) {
        this.addRequestFor = value;
    }

}
