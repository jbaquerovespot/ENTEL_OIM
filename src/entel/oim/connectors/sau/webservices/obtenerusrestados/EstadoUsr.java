/**
 * EstadoUsr.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.obtenerusrestados;

public class EstadoUsr  implements java.io.Serializable {
    private java.lang.String e_idestadousr;

    private java.lang.String e_descripcion;

    public EstadoUsr() {
    }

    public EstadoUsr(
           java.lang.String e_idestadousr,
           java.lang.String e_descripcion) {
           this.e_idestadousr = e_idestadousr;
           this.e_descripcion = e_descripcion;
    }


    /**
     * Gets the e_idestadousr value for this EstadoUsr.
     * 
     * @return e_idestadousr
     */
    public java.lang.String getE_idestadousr() {
        return e_idestadousr;
    }


    /**
     * Sets the e_idestadousr value for this EstadoUsr.
     * 
     * @param e_idestadousr
     */
    public void setE_idestadousr(java.lang.String e_idestadousr) {
        this.e_idestadousr = e_idestadousr;
    }


    /**
     * Gets the e_descripcion value for this EstadoUsr.
     * 
     * @return e_descripcion
     */
    public java.lang.String getE_descripcion() {
        return e_descripcion;
    }


    /**
     * Sets the e_descripcion value for this EstadoUsr.
     * 
     * @param e_descripcion
     */
    public void setE_descripcion(java.lang.String e_descripcion) {
        this.e_descripcion = e_descripcion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EstadoUsr)) return false;
        EstadoUsr other = (EstadoUsr) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_idestadousr==null && other.getE_idestadousr()==null) || 
             (this.e_idestadousr!=null &&
              this.e_idestadousr.equals(other.getE_idestadousr()))) &&
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
        if (getE_idestadousr() != null) {
            _hashCode += getE_idestadousr().hashCode();
        }
        if (getE_descripcion() != null) {
            _hashCode += getE_descripcion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EstadoUsr.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerUsrEstadosOSBService/types", "EstadoUsr"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idestadousr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerUsrEstadosOSBService/types", "e_idestadousr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerUsrEstadosOSBService/types", "e_descripcion"));
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
