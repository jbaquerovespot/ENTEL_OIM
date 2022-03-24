/**
 * DetallePlataformasType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.listarplataformas;

public class DetallePlataformasType  implements java.io.Serializable {
    private java.lang.String nombrePlataforma;

    public DetallePlataformasType() {
    }

    public DetallePlataformasType(
           java.lang.String nombrePlataforma) {
           this.nombrePlataforma = nombrePlataforma;
    }


    /**
     * Gets the nombrePlataforma value for this DetallePlataformasType.
     * 
     * @return nombrePlataforma
     */
    public java.lang.String getNombrePlataforma() {
        return nombrePlataforma;
    }


    /**
     * Sets the nombrePlataforma value for this DetallePlataformasType.
     * 
     * @param nombrePlataforma
     */
    public void setNombrePlataforma(java.lang.String nombrePlataforma) {
        this.nombrePlataforma = nombrePlataforma;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetallePlataformasType)) return false;
        DetallePlataformasType other = (DetallePlataformasType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nombrePlataforma==null && other.getNombrePlataforma()==null) || 
             (this.nombrePlataforma!=null &&
              this.nombrePlataforma.equals(other.getNombrePlataforma())));
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
        if (getNombrePlataforma() != null) {
            _hashCode += getNombrePlataforma().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetallePlataformasType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarPlataformasSGA/Response", "DetallePlataformasType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombrePlataforma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombrePlataforma"));
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
