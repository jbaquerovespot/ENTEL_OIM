/**
 * ListarGruposSGAResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.listargrupos;

public class ListarGruposSGAResponseType  implements java.io.Serializable {
    private java.lang.String codigo;

    private java.lang.String descripcion;

    private entel.oim.connectors.sga.webservices.listargrupos.DetalleGruposType[] grupos;

    public ListarGruposSGAResponseType() {
    }

    public ListarGruposSGAResponseType(
           java.lang.String codigo,
           java.lang.String descripcion,
           entel.oim.connectors.sga.webservices.listargrupos.DetalleGruposType[] grupos) {
           this.codigo = codigo;
           this.descripcion = descripcion;
           this.grupos = grupos;
    }


    /**
     * Gets the codigo value for this ListarGruposSGAResponseType.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this ListarGruposSGAResponseType.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the descripcion value for this ListarGruposSGAResponseType.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this ListarGruposSGAResponseType.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the grupos value for this ListarGruposSGAResponseType.
     * 
     * @return grupos
     */
    public entel.oim.connectors.sga.webservices.listargrupos.DetalleGruposType[] getGrupos() {
        return grupos;
    }


    /**
     * Sets the grupos value for this ListarGruposSGAResponseType.
     * 
     * @param grupos
     */
    public void setGrupos(entel.oim.connectors.sga.webservices.listargrupos.DetalleGruposType[] grupos) {
        this.grupos = grupos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListarGruposSGAResponseType)) return false;
        ListarGruposSGAResponseType other = (ListarGruposSGAResponseType) obj;
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
            ((this.grupos==null && other.getGrupos()==null) || 
             (this.grupos!=null &&
              java.util.Arrays.equals(this.grupos, other.getGrupos())));
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
        if (getGrupos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGrupos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGrupos(), i);
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
        new org.apache.axis.description.TypeDesc(ListarGruposSGAResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarGruposSGA/Response", "listarGruposSGAResponseType"));
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
        elemField.setFieldName("grupos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "grupos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarGruposSGA/Response", "DetalleGruposType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "ListadoGrupos"));
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
