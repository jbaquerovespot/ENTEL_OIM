/**
 * DetalleGruposType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.obtenerlistausuarios;

public class DetalleGruposType  implements java.io.Serializable {
    private java.lang.String nombreGrupo;

    private java.lang.String idGrupo;

    public DetalleGruposType() {
    }

    public DetalleGruposType(
           java.lang.String nombreGrupo,
           java.lang.String idGrupo) {
           this.nombreGrupo = nombreGrupo;
           this.idGrupo = idGrupo;
    }


    /**
     * Gets the nombreGrupo value for this DetalleGruposType.
     * 
     * @return nombreGrupo
     */
    public java.lang.String getNombreGrupo() {
        return nombreGrupo;
    }


    /**
     * Sets the nombreGrupo value for this DetalleGruposType.
     * 
     * @param nombreGrupo
     */
    public void setNombreGrupo(java.lang.String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }


    /**
     * Gets the idGrupo value for this DetalleGruposType.
     * 
     * @return idGrupo
     */
    public java.lang.String getIdGrupo() {
        return idGrupo;
    }


    /**
     * Sets the idGrupo value for this DetalleGruposType.
     * 
     * @param idGrupo
     */
    public void setIdGrupo(java.lang.String idGrupo) {
        this.idGrupo = idGrupo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetalleGruposType)) return false;
        DetalleGruposType other = (DetalleGruposType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nombreGrupo==null && other.getNombreGrupo()==null) || 
             (this.nombreGrupo!=null &&
              this.nombreGrupo.equals(other.getNombreGrupo()))) &&
            ((this.idGrupo==null && other.getIdGrupo()==null) || 
             (this.idGrupo!=null &&
              this.idGrupo.equals(other.getIdGrupo())));
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
        if (getNombreGrupo() != null) {
            _hashCode += getNombreGrupo().hashCode();
        }
        if (getIdGrupo() != null) {
            _hashCode += getIdGrupo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetalleGruposType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/obtenerListaUsuarioSGA/Response", "DetalleGruposType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreGrupo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreGrupo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idGrupo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idGrupo"));
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
