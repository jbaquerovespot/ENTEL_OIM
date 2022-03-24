/**
 * Estado.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.obtenerappestados;

public class Estado  implements java.io.Serializable {
    private java.lang.String e_idestado;

    private java.lang.String e_descripcion;

    public Estado() {
    }

    public Estado(
           java.lang.String e_idestado,
           java.lang.String e_descripcion) {
           this.e_idestado = e_idestado;
           this.e_descripcion = e_descripcion;
    }


    /**
     * Gets the e_idestado value for this Estado.
     * 
     * @return e_idestado
     */
    public java.lang.String getE_idestado() {
        return e_idestado;
    }


    /**
     * Sets the e_idestado value for this Estado.
     * 
     * @param e_idestado
     */
    public void setE_idestado(java.lang.String e_idestado) {
        this.e_idestado = e_idestado;
    }


    /**
     * Gets the e_descripcion value for this Estado.
     * 
     * @return e_descripcion
     */
    public java.lang.String getE_descripcion() {
        return e_descripcion;
    }


    /**
     * Sets the e_descripcion value for this Estado.
     * 
     * @param e_descripcion
     */
    public void setE_descripcion(java.lang.String e_descripcion) {
        this.e_descripcion = e_descripcion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Estado)) return false;
        Estado other = (Estado) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_idestado==null && other.getE_idestado()==null) || 
             (this.e_idestado!=null &&
              this.e_idestado.equals(other.getE_idestado()))) &&
            ((this.e_descripcion==null && other.getE_descripcion()==null) || 
             (this.e_descripcion!=null &&
              this.e_descripcion.equals(other.getE_descripcion())));
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
        if (getE_idestado() != null) {
            _hashCode += getE_idestado().hashCode();
        }
        if (getE_descripcion() != null) {
            _hashCode += getE_descripcion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Estado.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerAppEstadosOSBService/types", "Estado"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idestado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerAppEstadosOSBService/types", "e_idestado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerAppEstadosOSBService/types", "e_descripcion"));
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
