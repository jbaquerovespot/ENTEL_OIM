/**
 * ResponseHabilitarUsuarioType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.habilitarusuario;

public class ResponseHabilitarUsuarioType  implements java.io.Serializable {
    private java.lang.String e_codigo;

    private java.lang.String e_mensaje;

    public ResponseHabilitarUsuarioType() {
    }

    public ResponseHabilitarUsuarioType(
           java.lang.String e_codigo,
           java.lang.String e_mensaje) {
           this.e_codigo = e_codigo;
           this.e_mensaje = e_mensaje;
    }


    /**
     * Gets the e_codigo value for this ResponseHabilitarUsuarioType.
     * 
     * @return e_codigo
     */
    public java.lang.String getE_codigo() {
        return e_codigo;
    }


    /**
     * Sets the e_codigo value for this ResponseHabilitarUsuarioType.
     * 
     * @param e_codigo
     */
    public void setE_codigo(java.lang.String e_codigo) {
        this.e_codigo = e_codigo;
    }


    /**
     * Gets the e_mensaje value for this ResponseHabilitarUsuarioType.
     * 
     * @return e_mensaje
     */
    public java.lang.String getE_mensaje() {
        return e_mensaje;
    }


    /**
     * Sets the e_mensaje value for this ResponseHabilitarUsuarioType.
     * 
     * @param e_mensaje
     */
    public void setE_mensaje(java.lang.String e_mensaje) {
        this.e_mensaje = e_mensaje;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResponseHabilitarUsuarioType)) return false;
        ResponseHabilitarUsuarioType other = (ResponseHabilitarUsuarioType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_codigo==null && other.getE_codigo()==null) || 
             (this.e_codigo!=null &&
              this.e_codigo.equals(other.getE_codigo()))) &&
            ((this.e_mensaje==null && other.getE_mensaje()==null) || 
             (this.e_mensaje!=null &&
              this.e_mensaje.equals(other.getE_mensaje())));
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
        if (getE_codigo() != null) {
            _hashCode += getE_codigo().hashCode();
        }
        if (getE_mensaje() != null) {
            _hashCode += getE_mensaje().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResponseHabilitarUsuarioType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/habilitarUsuarioOSBSAUService/types", "ResponseHabilitarUsuarioType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/habilitarUsuarioOSBSAUService/types", "e_codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_mensaje");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/habilitarUsuarioOSBSAUService/types", "e_mensaje"));
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
