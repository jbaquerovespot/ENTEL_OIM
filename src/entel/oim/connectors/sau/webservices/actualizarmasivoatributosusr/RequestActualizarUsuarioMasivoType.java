/**
 * RequestActualizarUsuarioMasivoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr;

public class RequestActualizarUsuarioMasivoType  implements java.io.Serializable {
    private entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Usuario[] i_usuarios;

    public RequestActualizarUsuarioMasivoType() {
    }

    public RequestActualizarUsuarioMasivoType(
           entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Usuario[] i_usuarios) {
           this.i_usuarios = i_usuarios;
    }


    /**
     * Gets the i_usuarios value for this RequestActualizarUsuarioMasivoType.
     * 
     * @return i_usuarios
     */
    public entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Usuario[] getI_usuarios() {
        return i_usuarios;
    }


    /**
     * Sets the i_usuarios value for this RequestActualizarUsuarioMasivoType.
     * 
     * @param i_usuarios
     */
    public void setI_usuarios(entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Usuario[] i_usuarios) {
        this.i_usuarios = i_usuarios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestActualizarUsuarioMasivoType)) return false;
        RequestActualizarUsuarioMasivoType other = (RequestActualizarUsuarioMasivoType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.i_usuarios==null && other.getI_usuarios()==null) || 
             (this.i_usuarios!=null &&
              java.util.Arrays.equals(this.i_usuarios, other.getI_usuarios())));
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
        if (getI_usuarios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getI_usuarios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getI_usuarios(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestActualizarUsuarioMasivoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarMasivoAtributosUsrOSBSAUService/types", "RequestActualizarUsuarioMasivoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_usuarios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarMasivoAtributosUsrOSBSAUService/types", "i_usuarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarMasivoAtributosUsrOSBSAUService/types", "Usuario"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarMasivoAtributosUsrOSBSAUService/types", "item"));
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
