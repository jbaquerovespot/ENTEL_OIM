package entel.oim.plugins.eventhandler.remedy.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PriorityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PriorityType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Critical"/&gt;
 *     &lt;enumeration value="High"/&gt;
 *     &lt;enumeration value="Medium"/&gt;
 *     &lt;enumeration value="Low"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PriorityType")
@XmlEnum
public enum PriorityType {

    @XmlEnumValue("Critical")
    CRITICAL("Critical"),
    @XmlEnumValue("High")
    HIGH("High"),
    @XmlEnumValue("Medium")
    MEDIUM("Medium"),
    @XmlEnumValue("Low")
    LOW("Low");
    private final String value;

    PriorityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PriorityType fromValue(String v) {
        for (PriorityType c: PriorityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
