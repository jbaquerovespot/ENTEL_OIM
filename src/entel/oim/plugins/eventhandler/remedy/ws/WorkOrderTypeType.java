package entel.oim.plugins.eventhandler.remedy.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Work_Order_TypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Work_Order_TypeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Project"/&gt;
 *     &lt;enumeration value="General"/&gt;
 *     &lt;enumeration value="Release"/&gt;
 *     &lt;enumeration value="Asset Configuration"/&gt;
 *     &lt;enumeration value="Asset Management"/&gt;
 *     &lt;enumeration value="Asset Lease"/&gt;
 *     &lt;enumeration value="Purchase Requisition"/&gt;
 *     &lt;enumeration value="Asset Maintenance"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Work_Order_TypeType")
@XmlEnum
public enum WorkOrderTypeType {

    @XmlEnumValue("Project")
    PROJECT("Project"),
    @XmlEnumValue("General")
    GENERAL("General"),
    @XmlEnumValue("Release")
    RELEASE("Release"),
    @XmlEnumValue("Asset Configuration")
    ASSET_CONFIGURATION("Asset Configuration"),
    @XmlEnumValue("Asset Management")
    ASSET_MANAGEMENT("Asset Management"),
    @XmlEnumValue("Asset Lease")
    ASSET_LEASE("Asset Lease"),
    @XmlEnumValue("Purchase Requisition")
    PURCHASE_REQUISITION("Purchase Requisition"),
    @XmlEnumValue("Asset Maintenance")
    ASSET_MAINTENANCE("Asset Maintenance");
    private final String value;

    WorkOrderTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WorkOrderTypeType fromValue(String v) {
        for (WorkOrderTypeType c: WorkOrderTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
