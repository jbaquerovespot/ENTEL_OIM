
package entel.oim.plugins.eventhandler.remedy.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ente.oim.r package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreacionWO_QNAME = new QName("urn:CreacionWO", "CreacionWO");
    private final static QName _CreacionWOResponse_QNAME = new QName("urn:CreacionWO", "CreacionWOResponse");
    private final static QName _AuthenticationInfo_QNAME = new QName("urn:CreacionWO", "AuthenticationInfo");
    private final static QName _InputMapping1Priority_QNAME = new QName("urn:CreacionWO", "Priority");
    private final static QName _InputMapping1WorkOrderType_QNAME = new QName("urn:CreacionWO", "Work_Order_Type");
    private final static QName _InputMapping1AddRequestFor_QNAME = new QName("urn:CreacionWO", "AddRequestFor");
    private final static QName _InputMapping1Status_QNAME = new QName("urn:CreacionWO", "Status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ente.oim.r
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InputMapping1 }
     * 
     */
//    public InputMapping1 createInputMapping1() {
//        return new InputMapping1();
//    }

    /**
     * Create an instance of {@link OutputMapping1 }
     * 
     */
    public OutputMapping1 createOutputMapping1() {
        return new OutputMapping1();
    }

    /**
     * Create an instance of {@link AuthenticationInfo }
     * 
     */
    public AuthenticationInfo createAuthenticationInfo() {
        return new AuthenticationInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InputMapping1 }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InputMapping1 }{@code >}
     */
    @XmlElementDecl(namespace = "urn:CreacionWO", name = "CreacionWO")
    public JAXBElement<InputMapping1> createCreacionWO(InputMapping1 value) {
        return new JAXBElement<InputMapping1>(_CreacionWO_QNAME, InputMapping1 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutputMapping1 }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OutputMapping1 }{@code >}
     */
    @XmlElementDecl(namespace = "urn:CreacionWO", name = "CreacionWOResponse")
    public JAXBElement<OutputMapping1> createCreacionWOResponse(OutputMapping1 value) {
        return new JAXBElement<OutputMapping1>(_CreacionWOResponse_QNAME, OutputMapping1 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AuthenticationInfo }{@code >}
     */
    @XmlElementDecl(namespace = "urn:CreacionWO", name = "AuthenticationInfo")
    public JAXBElement<AuthenticationInfo> createAuthenticationInfo(AuthenticationInfo value) {
        return new JAXBElement<AuthenticationInfo>(_AuthenticationInfo_QNAME, AuthenticationInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PriorityType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PriorityType }{@code >}
     */
    @XmlElementDecl(namespace = "urn:CreacionWO", name = "Priority", scope = InputMapping1 .class)
    public JAXBElement<PriorityType> createInputMapping1Priority2(PriorityType value) {
        return new JAXBElement<PriorityType>(_InputMapping1Priority_QNAME, PriorityType.class, InputMapping1 .class, value);
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WorkOrderTypeType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WorkOrderTypeType }{@code >}
     */
    @XmlElementDecl(namespace = "urn:CreacionWO", name = "Work_Order_Type", scope = InputMapping1 .class)
    public JAXBElement<WorkOrderTypeType> createInputMapping1WorkOrderType(WorkOrderTypeType value) {
        return new JAXBElement<WorkOrderTypeType>(_InputMapping1WorkOrderType_QNAME, WorkOrderTypeType.class, InputMapping1 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRequestForType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AddRequestForType }{@code >}
     */
    @XmlElementDecl(namespace = "urn:CreacionWO", name = "AddRequestFor", scope = InputMapping1 .class)
    public JAXBElement<AddRequestForType> createInputMapping1AddRequestFor(AddRequestForType value) {
        return new JAXBElement<AddRequestForType>(_InputMapping1AddRequestFor_QNAME, AddRequestForType.class, InputMapping1 .class, value);
    }

    @XmlElementDecl(namespace = "urn:CreacionWO", name = "Status", scope = InputMapping1 .class)
    public JAXBElement<StatusType> createInputMapping1Status(StatusType value) {
        return new JAXBElement<StatusType>(_InputMapping1Status_QNAME, StatusType.class, InputMapping1 .class, value);
    }
    

}
