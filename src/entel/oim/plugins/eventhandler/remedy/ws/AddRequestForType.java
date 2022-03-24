
package entel.oim.plugins.eventhandler.remedy.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AddRequestForType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AddRequestForType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Individual"/&gt;
 *     &lt;enumeration value="Organization"/&gt;
 *     &lt;enumeration value="Department"/&gt;
 *     &lt;enumeration value="Company"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AddRequestForType")
@XmlEnum
public enum AddRequestForType {

    @XmlEnumValue("Individual")
    INDIVIDUAL("Individual"),
    @XmlEnumValue("Organization")
    ORGANIZATION("Organization"),
    @XmlEnumValue("Department")
    DEPARTMENT("Department"),
    @XmlEnumValue("Company")
    COMPANY("Company");
    private final String value;

    AddRequestForType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AddRequestForType fromValue(String v) {
        for (AddRequestForType c: AddRequestForType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
