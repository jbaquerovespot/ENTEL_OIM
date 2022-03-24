/**
 * RequestBuscarUAPType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.buscarusrappperfil;

public class RequestBuscarUAPType  implements java.io.Serializable {
    private java.lang.String i_cuenta;

    private java.lang.String i_idaplicacion;

    public RequestBuscarUAPType() {
    }

    public RequestBuscarUAPType(
           java.lang.String i_cuenta,
           java.lang.String i_idaplicacion) {
           this.i_cuenta = i_cuenta;
           this.i_idaplicacion = i_idaplicacion;
    }


    /**
     * Gets the i_cuenta value for this RequestBuscarUAPType.
     * 
     * @return i_cuenta
     */
    public java.lang.String getI_cuenta() {
        return i_cuenta;
    }


    /**
     * Sets the i_cuenta value for this RequestBuscarUAPType.
     * 
     * @param i_cuenta
     */
    public void setI_cuenta(java.lang.String i_cuenta) {
        this.i_cuenta = i_cuenta;
    }


    /**
     * Gets the i_idaplicacion value for this RequestBuscarUAPType.
     * 
     * @return i_idaplicacion
     */
    public java.lang.String getI_idaplicacion() {
        return i_idaplicacion;
    }


    /**
     * Sets the i_idaplicacion value for this RequestBuscarUAPType.
     * 
     * @param i_idaplicacion
     */
    public void setI_idaplicacion(java.lang.String i_idaplicacion) {
        this.i_idaplicacion = i_idaplicacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestBuscarUAPType)) return false;
        RequestBuscarUAPType other = (RequestBuscarUAPType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.i_cuenta==null && other.getI_cuenta()==null) || 
             (this.i_cuenta!=null &&
              this.i_cuenta.equals(other.getI_cuenta()))) &&
            ((this.i_idaplicacion==null && other.getI_idaplicacion()==null) || 
             (this.i_idaplicacion!=null &&
              this.i_idaplicacion.equals(other.getI_idaplicacion())));
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
        if (getI_cuenta() != null) {
            _hashCode += getI_cuenta().hashCode();
        }
        if (getI_idaplicacion() != null) {
            _hashCode += getI_idaplicacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestBuscarUAPType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsrAppPerfilOSBService/types", "RequestBuscarUAPType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_cuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsrAppPerfilOSBService/types", "i_cuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_idaplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsrAppPerfilOSBService/types", "i_idaplicacion"));
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
