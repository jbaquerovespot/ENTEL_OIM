/**
 * Aplicacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.obteneraplicacionesall;

public class Aplicacion  implements java.io.Serializable {
    private java.lang.String e_idaplicacion;

    private java.lang.String e_nombre;

    public Aplicacion() {
    }

    public Aplicacion(
           java.lang.String e_idaplicacion,
           java.lang.String e_nombre) {
           this.e_idaplicacion = e_idaplicacion;
           this.e_nombre = e_nombre;
    }


    /**
     * Gets the e_idaplicacion value for this Aplicacion.
     * 
     * @return e_idaplicacion
     */
    public java.lang.String getE_idaplicacion() {
        return e_idaplicacion;
    }


    /**
     * Sets the e_idaplicacion value for this Aplicacion.
     * 
     * @param e_idaplicacion
     */
    public void setE_idaplicacion(java.lang.String e_idaplicacion) {
        this.e_idaplicacion = e_idaplicacion;
    }


    /**
     * Gets the e_nombre value for this Aplicacion.
     * 
     * @return e_nombre
     */
    public java.lang.String getE_nombre() {
        return e_nombre;
    }


    /**
     * Sets the e_nombre value for this Aplicacion.
     * 
     * @param e_nombre
     */
    public void setE_nombre(java.lang.String e_nombre) {
        this.e_nombre = e_nombre;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Aplicacion)) return false;
        Aplicacion other = (Aplicacion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_idaplicacion==null && other.getE_idaplicacion()==null) || 
             (this.e_idaplicacion!=null &&
              this.e_idaplicacion.equals(other.getE_idaplicacion()))) &&
            ((this.e_nombre==null && other.getE_nombre()==null) || 
             (this.e_nombre!=null &&
              this.e_nombre.equals(other.getE_nombre())));
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
        if (getE_idaplicacion() != null) {
            _hashCode += getE_idaplicacion().hashCode();
        }
        if (getE_nombre() != null) {
            _hashCode += getE_nombre().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Aplicacion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerAplicacionesAllOSBService/types", "Aplicacion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idaplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerAplicacionesAllOSBService/types", "e_idaplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerAplicacionesAllOSBService/types", "e_nombre"));
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
