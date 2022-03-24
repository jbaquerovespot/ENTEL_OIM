/**
 * ListarPlataformasSGAResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.listarplataformas;

public class ListarPlataformasSGAResponseType  implements java.io.Serializable {
    private java.lang.String codigo;

    private java.lang.String descripcion;

    private entel.oim.connectors.sga.webservices.listarplataformas.DetallePlataformasType[] plataformas;

    public ListarPlataformasSGAResponseType() {
    }

    public ListarPlataformasSGAResponseType(
           java.lang.String codigo,
           java.lang.String descripcion,
           entel.oim.connectors.sga.webservices.listarplataformas.DetallePlataformasType[] plataformas) {
           this.codigo = codigo;
           this.descripcion = descripcion;
           this.plataformas = plataformas;
    }


    /**
     * Gets the codigo value for this ListarPlataformasSGAResponseType.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this ListarPlataformasSGAResponseType.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the descripcion value for this ListarPlataformasSGAResponseType.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this ListarPlataformasSGAResponseType.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the plataformas value for this ListarPlataformasSGAResponseType.
     * 
     * @return plataformas
     */
    public entel.oim.connectors.sga.webservices.listarplataformas.DetallePlataformasType[] getPlataformas() {
        return plataformas;
    }


    /**
     * Sets the plataformas value for this ListarPlataformasSGAResponseType.
     * 
     * @param plataformas
     */
    public void setPlataformas(entel.oim.connectors.sga.webservices.listarplataformas.DetallePlataformasType[] plataformas) {
        this.plataformas = plataformas;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListarPlataformasSGAResponseType)) return false;
        ListarPlataformasSGAResponseType other = (ListarPlataformasSGAResponseType) obj;
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
            ((this.plataformas==null && other.getPlataformas()==null) || 
             (this.plataformas!=null &&
              java.util.Arrays.equals(this.plataformas, other.getPlataformas())));
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
        if (getPlataformas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPlataformas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPlataformas(), i);
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
        new org.apache.axis.description.TypeDesc(ListarPlataformasSGAResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarPlataformasSGA/Response", "listarPlataformasSGAResponseType"));
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
        elemField.setFieldName("plataformas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "plataformas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarPlataformasSGA/Response", "DetallePlataformasType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "ListadoPlataformas"));
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
