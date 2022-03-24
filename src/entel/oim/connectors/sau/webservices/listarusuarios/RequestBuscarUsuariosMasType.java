/**
 * RequestBuscarUsuariosMasType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.listarusuarios;

public class RequestBuscarUsuariosMasType  implements java.io.Serializable {
    private java.lang.String i_accion;

    private java.lang.String i_palabraclave;

    private java.lang.String i_estadousr;

    public RequestBuscarUsuariosMasType() {
    }

    public RequestBuscarUsuariosMasType(
           java.lang.String i_accion,
           java.lang.String i_palabraclave,
           java.lang.String i_estadousr) {
           this.i_accion = i_accion;
           this.i_palabraclave = i_palabraclave;
           this.i_estadousr = i_estadousr;
    }


    /**
     * Gets the i_accion value for this RequestBuscarUsuariosMasType.
     * 
     * @return i_accion
     */
    public java.lang.String getI_accion() {
        return i_accion;
    }


    /**
     * Sets the i_accion value for this RequestBuscarUsuariosMasType.
     * 
     * @param i_accion
     */
    public void setI_accion(java.lang.String i_accion) {
        this.i_accion = i_accion;
    }


    /**
     * Gets the i_palabraclave value for this RequestBuscarUsuariosMasType.
     * 
     * @return i_palabraclave
     */
    public java.lang.String getI_palabraclave() {
        return i_palabraclave;
    }


    /**
     * Sets the i_palabraclave value for this RequestBuscarUsuariosMasType.
     * 
     * @param i_palabraclave
     */
    public void setI_palabraclave(java.lang.String i_palabraclave) {
        this.i_palabraclave = i_palabraclave;
    }


    /**
     * Gets the i_estadousr value for this RequestBuscarUsuariosMasType.
     * 
     * @return i_estadousr
     */
    public java.lang.String getI_estadousr() {
        return i_estadousr;
    }


    /**
     * Sets the i_estadousr value for this RequestBuscarUsuariosMasType.
     * 
     * @param i_estadousr
     */
    public void setI_estadousr(java.lang.String i_estadousr) {
        this.i_estadousr = i_estadousr;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestBuscarUsuariosMasType)) return false;
        RequestBuscarUsuariosMasType other = (RequestBuscarUsuariosMasType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.i_accion==null && other.getI_accion()==null) || 
             (this.i_accion!=null &&
              this.i_accion.equals(other.getI_accion()))) &&
            ((this.i_palabraclave==null && other.getI_palabraclave()==null) || 
             (this.i_palabraclave!=null &&
              this.i_palabraclave.equals(other.getI_palabraclave()))) &&
            ((this.i_estadousr==null && other.getI_estadousr()==null) || 
             (this.i_estadousr!=null &&
              this.i_estadousr.equals(other.getI_estadousr())));
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
        if (getI_accion() != null) {
            _hashCode += getI_accion().hashCode();
        }
        if (getI_palabraclave() != null) {
            _hashCode += getI_palabraclave().hashCode();
        }
        if (getI_estadousr() != null) {
            _hashCode += getI_estadousr().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestBuscarUsuariosMasType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityMgmt/T/listarUsuariosSAU/types", "RequestBuscarUsuariosMasType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_accion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityMgmt/T/listarUsuariosSAU/types", "i_accion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_palabraclave");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityMgmt/T/listarUsuariosSAU/types", "i_palabraclave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_estadousr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityMgmt/T/listarUsuariosSAU/types", "i_estadousr"));
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