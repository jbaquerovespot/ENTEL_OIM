/**
 * PApp.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.obtenerperfilesporapp;

public class PApp  implements java.io.Serializable {
    private java.lang.String e_idperfil;

    private java.lang.String e_nomperfil;

    public PApp() {
    }

    public PApp(
           java.lang.String e_idperfil,
           java.lang.String e_nomperfil) {
           this.e_idperfil = e_idperfil;
           this.e_nomperfil = e_nomperfil;
    }


    /**
     * Gets the e_idperfil value for this PApp.
     * 
     * @return e_idperfil
     */
    public java.lang.String getE_idperfil() {
        return e_idperfil;
    }


    /**
     * Sets the e_idperfil value for this PApp.
     * 
     * @param e_idperfil
     */
    public void setE_idperfil(java.lang.String e_idperfil) {
        this.e_idperfil = e_idperfil;
    }


    /**
     * Gets the e_nomperfil value for this PApp.
     * 
     * @return e_nomperfil
     */
    public java.lang.String getE_nomperfil() {
        return e_nomperfil;
    }


    /**
     * Sets the e_nomperfil value for this PApp.
     * 
     * @param e_nomperfil
     */
    public void setE_nomperfil(java.lang.String e_nomperfil) {
        this.e_nomperfil = e_nomperfil;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PApp)) return false;
        PApp other = (PApp) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_idperfil==null && other.getE_idperfil()==null) || 
             (this.e_idperfil!=null &&
              this.e_idperfil.equals(other.getE_idperfil()))) &&
            ((this.e_nomperfil==null && other.getE_nomperfil()==null) || 
             (this.e_nomperfil!=null &&
              this.e_nomperfil.equals(other.getE_nomperfil())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getE_idperfil() != null) {
            _hashCode += getE_idperfil().hashCode();
        }
        if (getE_nomperfil() != null) {
            _hashCode += getE_nomperfil().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PApp.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerPerfilesPorAppOSBService/types", "PApp"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idperfil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerPerfilesPorAppOSBService/types", "e_idperfil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_nomperfil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerPerfilesPorAppOSBService/types", "e_nomperfil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
