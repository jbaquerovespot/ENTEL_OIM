package entel.oim.plugins.eventhandler.remedy.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StatusType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Assigned"/&gt;
 *     &lt;enumeration value="Pending"/&gt;
 *     &lt;enumeration value="Waiting Approval"/&gt;
 *     &lt;enumeration value="Planning"/&gt;
 *     &lt;enumeration value="In Progress"/&gt;
 *     &lt;enumeration value="Completed"/&gt;
 *     &lt;enumeration value="Rejected"/&gt;
 *     &lt;enumeration value="Cancelled"/&gt;
 *     &lt;enumeration value="Closed"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StatusType")
@XmlEnum
public enum StatusType {

    @XmlEnumValue("Assigned")
    ASSIGNED("Assigned"),
    @XmlEnumValue("Pending")
    PENDING("Pending"),
    @XmlEnumValue("Waiting Approval")
    WAITING_APPROVAL("Waiting Approval"),
    @XmlEnumValue("Planning")
    PLANNING("Planning"),
    @XmlEnumValue("In Progress")
    IN_PROGRESS("In Progress"),
    @XmlEnumValue("Completed")
    COMPLETED("Completed"),
    @XmlEnumValue("Rejected")
    REJECTED("Rejected"),
    @XmlEnumValue("Cancelled")
    CANCELLED("Cancelled"),
    @XmlEnumValue("Closed")
    CLOSED("Closed");
    private final String value;

    StatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StatusType fromValue(String v) {
        for (StatusType c: StatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
