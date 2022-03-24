/**
 * RequestResetPasswordType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.reiniciarpassword;

public class RequestResetPasswordType  implements java.io.Serializable {
    private java.lang.String i_cuenta;

    private java.lang.String i_password;

    public RequestResetPasswordType() {
    }

    public RequestResetPasswordType(
           java.lang.String i_cuenta,
           java.lang.String i_password) {
           this.i_cuenta = i_cuenta;
           this.i_password = i_password;
    }


    /**
     * Gets the i_cuenta value for this RequestResetPasswordType.
     * 
     * @return i_cuenta
     */
    public java.lang.String getI_cuenta() {
        return i_cuenta;
    }


    /**
     * Sets the i_cuenta value for this RequestResetPasswordType.
     * 
     * @param i_cuenta
     */
    public void setI_cuenta(java.lang.String i_cuenta) {
        this.i_cuenta = i_cuenta;
    }


    /**
     * Gets the i_password value for this RequestResetPasswordType.
     * 
     * @return i_password
     */
    public java.lang.String getI_password() {
        return i_password;
    }


    /**
     * Sets the i_password value for this RequestResetPasswordType.
     * 
     * @param i_password
     */
    public void setI_password(java.lang.String i_password) {
        this.i_password = i_password;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestResetPasswordType)) return false;
        RequestResetPasswordType other = (RequestResetPasswordType) obj;
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
            ((this.i_password==null && other.getI_password()==null) || 
             (this.i_password!=null &&
              this.i_password.equals(other.getI_password())));
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
        if (getI_password() != null) {
            _hashCode += getI_password().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestResetPasswordType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/reiniciarPasswordOSBSAUService/types", "RequestResetPasswordType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_cuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/reiniciarPasswordOSBSAUService/types", "i_cuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/reiniciarPasswordOSBSAUService/types", "i_password"));
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
