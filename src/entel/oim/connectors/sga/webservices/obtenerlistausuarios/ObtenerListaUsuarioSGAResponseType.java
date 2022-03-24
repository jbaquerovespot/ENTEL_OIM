/**
 * ObtenerListaUsuarioSGAResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.obtenerlistausuarios;

public class ObtenerListaUsuarioSGAResponseType  implements java.io.Serializable {
    private java.lang.String codigo;

    private java.lang.String descripcion;

    private entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleUsuariosType[] listaUsuarios;

    public ObtenerListaUsuarioSGAResponseType() {
    }

    public ObtenerListaUsuarioSGAResponseType(
           java.lang.String codigo,
           java.lang.String descripcion,
           entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleUsuariosType[] listaUsuarios) {
           this.codigo = codigo;
           this.descripcion = descripcion;
           this.listaUsuarios = listaUsuarios;
    }


    /**
     * Gets the codigo value for this ObtenerListaUsuarioSGAResponseType.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this ObtenerListaUsuarioSGAResponseType.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the descripcion value for this ObtenerListaUsuarioSGAResponseType.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this ObtenerListaUsuarioSGAResponseType.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the listaUsuarios value for this ObtenerListaUsuarioSGAResponseType.
     * 
     * @return listaUsuarios
     */
    public entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleUsuariosType[] getListaUsuarios() {
        return listaUsuarios;
    }


    /**
     * Sets the listaUsuarios value for this ObtenerListaUsuarioSGAResponseType.
     * 
     * @param listaUsuarios
     */
    public void setListaUsuarios(entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleUsuariosType[] listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerListaUsuarioSGAResponseType)) return false;
        ObtenerListaUsuarioSGAResponseType other = (ObtenerListaUsuarioSGAResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            ((this.listaUsuarios==null && other.getListaUsuarios()==null) || 
             (this.listaUsuarios!=null &&
              java.util.Arrays.equals(this.listaUsuarios, other.getListaUsuarios())));
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
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        if (getListaUsuarios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListaUsuarios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListaUsuarios(), i);
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
        new org.apache.axis.description.TypeDesc(ObtenerListaUsuarioSGAResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/obtenerListaUsuarioSGA/Response", "obtenerListaUsuarioSGAResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listaUsuarios");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listaUsuarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/obtenerListaUsuarioSGA/Response", "DetalleUsuariosType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "usuario"));
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
