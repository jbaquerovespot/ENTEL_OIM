/**
 * Aplicaciones.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.buscarusuarios;

public class Aplicaciones  implements java.io.Serializable {
    private java.lang.String apl_idaplicacion;

    private java.lang.String apl_nombre;

    private java.lang.String apl_idperfil;

    public Aplicaciones() {
    }

    public Aplicaciones(
           java.lang.String apl_idaplicacion,
           java.lang.String apl_nombre,
           java.lang.String apl_idperfil) {
           this.apl_idaplicacion = apl_idaplicacion;
           this.apl_nombre = apl_nombre;
           this.apl_idperfil = apl_idperfil;
    }


    /**
     * Gets the apl_idaplicacion value for this Aplicaciones.
     * 
     * @return apl_idaplicacion
     */
    public java.lang.String getApl_idaplicacion() {
        return apl_idaplicacion;
    }


    /**
     * Sets the apl_idaplicacion value for this Aplicaciones.
     * 
     * @param apl_idaplicacion
     */
    public void setApl_idaplicacion(java.lang.String apl_idaplicacion) {
        this.apl_idaplicacion = apl_idaplicacion;
    }


    /**
     * Gets the apl_nombre value for this Aplicaciones.
     * 
     * @return apl_nombre
     */
    public java.lang.String getApl_nombre() {
        return apl_nombre;
    }


    /**
     * Sets the apl_nombre value for this Aplicaciones.
     * 
     * @param apl_nombre
     */
    public void setApl_nombre(java.lang.String apl_nombre) {
        this.apl_nombre = apl_nombre;
    }


    /**
     * Gets the apl_idperfil value for this Aplicaciones.
     * 
     * @return apl_idperfil
     */
    public java.lang.String getApl_idperfil() {
        return apl_idperfil;
    }


    /**
     * Sets the apl_idperfil value for this Aplicaciones.
     * 
     * @param apl_idperfil
     */
    public void setApl_idperfil(java.lang.String apl_idperfil) {
        this.apl_idperfil = apl_idperfil;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Aplicaciones)) return false;
        Aplicaciones other = (Aplicaciones) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.apl_idaplicacion==null && other.getApl_idaplicacion()==null) || 
             (this.apl_idaplicacion!=null &&
              this.apl_idaplicacion.equals(other.getApl_idaplicacion()))) &&
            ((this.apl_nombre==null && other.getApl_nombre()==null) || 
             (this.apl_nombre!=null &&
              this.apl_nombre.equals(other.getApl_nombre()))) &&
            ((this.apl_idperfil==null && other.getApl_idperfil()==null) || 
             (this.apl_idperfil!=null &&
              this.apl_idperfil.equals(other.getApl_idperfil())));
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
        if (getApl_idaplicacion() != null) {
            _hashCode += getApl_idaplicacion().hashCode();
        }
        if (getApl_nombre() != null) {
            _hashCode += getApl_nombre().hashCode();
        }
        if (getApl_idperfil() != null) {
            _hashCode += getApl_idperfil().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Aplicaciones.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "Aplicaciones"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apl_idaplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "apl_idaplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apl_nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "apl_nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apl_idperfil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "apl_idperfil"));
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
