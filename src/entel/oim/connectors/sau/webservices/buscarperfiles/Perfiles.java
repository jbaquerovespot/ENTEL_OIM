/**
 * Perfiles.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.buscarperfiles;

public class Perfiles  implements java.io.Serializable {
    private java.lang.String e_idperfil;

    private java.lang.String e_nombre;

    private java.lang.String e_llave;

    private java.lang.String e_idaplicacion;

    private java.lang.String e_aplicacion;

    private java.lang.String e_tipoestado;

    private java.lang.String e_registro;

    public Perfiles() {
    }

    public Perfiles(
           java.lang.String e_idperfil,
           java.lang.String e_nombre,
           java.lang.String e_llave,
           java.lang.String e_idaplicacion,
           java.lang.String e_aplicacion,
           java.lang.String e_tipoestado,
           java.lang.String e_registro) {
           this.e_idperfil = e_idperfil;
           this.e_nombre = e_nombre;
           this.e_llave = e_llave;
           this.e_idaplicacion = e_idaplicacion;
           this.e_aplicacion = e_aplicacion;
           this.e_tipoestado = e_tipoestado;
           this.e_registro = e_registro;
    }


    /**
     * Gets the e_idperfil value for this Perfiles.
     * 
     * @return e_idperfil
     */
    public java.lang.String getE_idperfil() {
        return e_idperfil;
    }


    /**
     * Sets the e_idperfil value for this Perfiles.
     * 
     * @param e_idperfil
     */
    public void setE_idperfil(java.lang.String e_idperfil) {
        this.e_idperfil = e_idperfil;
    }


    /**
     * Gets the e_nombre value for this Perfiles.
     * 
     * @return e_nombre
     */
    public java.lang.String getE_nombre() {
        return e_nombre;
    }


    /**
     * Sets the e_nombre value for this Perfiles.
     * 
     * @param e_nombre
     */
    public void setE_nombre(java.lang.String e_nombre) {
        this.e_nombre = e_nombre;
    }


    /**
     * Gets the e_llave value for this Perfiles.
     * 
     * @return e_llave
     */
    public java.lang.String getE_llave() {
        return e_llave;
    }


    /**
     * Sets the e_llave value for this Perfiles.
     * 
     * @param e_llave
     */
    public void setE_llave(java.lang.String e_llave) {
        this.e_llave = e_llave;
    }


    /**
     * Gets the e_idaplicacion value for this Perfiles.
     * 
     * @return e_idaplicacion
     */
    public java.lang.String getE_idaplicacion() {
        return e_idaplicacion;
    }


    /**
     * Sets the e_idaplicacion value for this Perfiles.
     * 
     * @param e_idaplicacion
     */
    public void setE_idaplicacion(java.lang.String e_idaplicacion) {
        this.e_idaplicacion = e_idaplicacion;
    }


    /**
     * Gets the e_aplicacion value for this Perfiles.
     * 
     * @return e_aplicacion
     */
    public java.lang.String getE_aplicacion() {
        return e_aplicacion;
    }


    /**
     * Sets the e_aplicacion value for this Perfiles.
     * 
     * @param e_aplicacion
     */
    public void setE_aplicacion(java.lang.String e_aplicacion) {
        this.e_aplicacion = e_aplicacion;
    }


    /**
     * Gets the e_tipoestado value for this Perfiles.
     * 
     * @return e_tipoestado
     */
    public java.lang.String getE_tipoestado() {
        return e_tipoestado;
    }


    /**
     * Sets the e_tipoestado value for this Perfiles.
     * 
     * @param e_tipoestado
     */
    public void setE_tipoestado(java.lang.String e_tipoestado) {
        this.e_tipoestado = e_tipoestado;
    }


    /**
     * Gets the e_registro value for this Perfiles.
     * 
     * @return e_registro
     */
    public java.lang.String getE_registro() {
        return e_registro;
    }


    /**
     * Sets the e_registro value for this Perfiles.
     * 
     * @param e_registro
     */
    public void setE_registro(java.lang.String e_registro) {
        this.e_registro = e_registro;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Perfiles)) return false;
        Perfiles other = (Perfiles) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_idperfil==null && other.getE_idperfil()==null) || 
             (this.e_idperfil!=null &&
              this.e_idperfil.equals(other.getE_idperfil()))) &&
            ((this.e_nombre==null && other.getE_nombre()==null) || 
             (this.e_nombre!=null &&
              this.e_nombre.equals(other.getE_nombre()))) &&
            ((this.e_llave==null && other.getE_llave()==null) || 
             (this.e_llave!=null &&
              this.e_llave.equals(other.getE_llave()))) &&
            ((this.e_idaplicacion==null && other.getE_idaplicacion()==null) || 
             (this.e_idaplicacion!=null &&
              this.e_idaplicacion.equals(other.getE_idaplicacion()))) &&
            ((this.e_aplicacion==null && other.getE_aplicacion()==null) || 
             (this.e_aplicacion!=null &&
              this.e_aplicacion.equals(other.getE_aplicacion()))) &&
            ((this.e_tipoestado==null && other.getE_tipoestado()==null) || 
             (this.e_tipoestado!=null &&
              this.e_tipoestado.equals(other.getE_tipoestado()))) &&
            ((this.e_registro==null && other.getE_registro()==null) || 
             (this.e_registro!=null &&
              this.e_registro.equals(other.getE_registro())));
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
        if (getE_idperfil() != null) {
            _hashCode += getE_idperfil().hashCode();
        }
        if (getE_nombre() != null) {
            _hashCode += getE_nombre().hashCode();
        }
        if (getE_llave() != null) {
            _hashCode += getE_llave().hashCode();
        }
        if (getE_idaplicacion() != null) {
            _hashCode += getE_idaplicacion().hashCode();
        }
        if (getE_aplicacion() != null) {
            _hashCode += getE_aplicacion().hashCode();
        }
        if (getE_tipoestado() != null) {
            _hashCode += getE_tipoestado().hashCode();
        }
        if (getE_registro() != null) {
            _hashCode += getE_registro().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Perfiles.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "Perfiles"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idperfil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "e_idperfil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "e_nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_llave");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "e_llave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idaplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "e_idaplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_aplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "e_aplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_tipoestado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "e_tipoestado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_registro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarPerfilesOSBSAUService/types", "e_registro"));
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
