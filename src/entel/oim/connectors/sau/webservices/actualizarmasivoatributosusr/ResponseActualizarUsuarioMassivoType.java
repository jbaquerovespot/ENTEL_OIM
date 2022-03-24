/**
 * ResponseActualizarUsuarioMassivoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr;

public class ResponseActualizarUsuarioMassivoType  implements java.io.Serializable {
    private entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Respuesta[] e_respuestas;

    public ResponseActualizarUsuarioMassivoType() {
    }

    public ResponseActualizarUsuarioMassivoType(
           entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Respuesta[] e_respuestas) {
           this.e_respuestas = e_respuestas;
    }


    /**
     * Gets the e_respuestas value for this ResponseActualizarUsuarioMassivoType.
     * 
     * @return e_respuestas
     */
    public entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Respuesta[] getE_respuestas() {
        return e_respuestas;
    }


    /**
     * Sets the e_respuestas value for this ResponseActualizarUsuarioMassivoType.
     * 
     * @param e_respuestas
     */
    public void setE_respuestas(entel.oim.connectors.sau.webservices.actualizarmasivoatributosusr.Respuesta[] e_respuestas) {
        this.e_respuestas = e_respuestas;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResponseActualizarUsuarioMassivoType)) return false;
        ResponseActualizarUsuarioMassivoType other = (ResponseActualizarUsuarioMassivoType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_respuestas==null && other.getE_respuestas()==null) || 
             (this.e_respuestas!=null &&
              java.util.Arrays.equals(this.e_respuestas, other.getE_respuestas())));
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
        if (getE_respuestas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getE_respuestas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getE_respuestas(), i);
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
        new org.apache.axis.description.TypeDesc(ResponseActualizarUsuarioMassivoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarMasivoAtributosUsrOSBSAUService/types", "ResponseActualizarUsuarioMassivoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_respuestas");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarMasivoAtributosUsrOSBSAUService/types", "e_respuestas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarMasivoAtributosUsrOSBSAUService/types", "Respuesta"));
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
