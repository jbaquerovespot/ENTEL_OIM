/**
 * RequestAppUsuarioType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.actualizaraplicacionesusr;

public class RequestAppUsuarioType  implements java.io.Serializable {
    private java.lang.String i_accion;

    private java.lang.String i_cuenta;

    private java.lang.String i_idaplicacion;

    private java.lang.String i_idtipoestado;

    private java.lang.String i_usuario;

    public RequestAppUsuarioType() {
    }

    public RequestAppUsuarioType(
           java.lang.String i_accion,
           java.lang.String i_cuenta,
           java.lang.String i_idaplicacion,
           java.lang.String i_idtipoestado,
           java.lang.String i_usuario) {
           this.i_accion = i_accion;
           this.i_cuenta = i_cuenta;
           this.i_idaplicacion = i_idaplicacion;
           this.i_idtipoestado = i_idtipoestado;
           this.i_usuario = i_usuario;
    }


    /**
     * Gets the i_accion value for this RequestAppUsuarioType.
     * 
     * @return i_accion
     */
    public java.lang.String getI_accion() {
        return i_accion;
    }


    /**
     * Sets the i_accion value for this RequestAppUsuarioType.
     * 
     * @param i_accion
     */
    public void setI_accion(java.lang.String i_accion) {
        this.i_accion = i_accion;
    }


    /**
     * Gets the i_cuenta value for this RequestAppUsuarioType.
     * 
     * @return i_cuenta
     */
    public java.lang.String getI_cuenta() {
        return i_cuenta;
    }


    /**
     * Sets the i_cuenta value for this RequestAppUsuarioType.
     * 
     * @param i_cuenta
     */
    public void setI_cuenta(java.lang.String i_cuenta) {
        this.i_cuenta = i_cuenta;
    }


    /**
     * Gets the i_idaplicacion value for this RequestAppUsuarioType.
     * 
     * @return i_idaplicacion
     */
    public java.lang.String getI_idaplicacion() {
        return i_idaplicacion;
    }


    /**
     * Sets the i_idaplicacion value for this RequestAppUsuarioType.
     * 
     * @param i_idaplicacion
     */
    public void setI_idaplicacion(java.lang.String i_idaplicacion) {
        this.i_idaplicacion = i_idaplicacion;
    }


    /**
     * Gets the i_idtipoestado value for this RequestAppUsuarioType.
     * 
     * @return i_idtipoestado
     */
    public java.lang.String getI_idtipoestado() {
        return i_idtipoestado;
    }


    /**
     * Sets the i_idtipoestado value for this RequestAppUsuarioType.
     * 
     * @param i_idtipoestado
     */
    public void setI_idtipoestado(java.lang.String i_idtipoestado) {
        this.i_idtipoestado = i_idtipoestado;
    }


    /**
     * Gets the i_usuario value for this RequestAppUsuarioType.
     * 
     * @return i_usuario
     */
    public java.lang.String getI_usuario() {
        return i_usuario;
    }


    /**
     * Sets the i_usuario value for this RequestAppUsuarioType.
     * 
     * @param i_usuario
     */
    public void setI_usuario(java.lang.String i_usuario) {
        this.i_usuario = i_usuario;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestAppUsuarioType)) return false;
        RequestAppUsuarioType other = (RequestAppUsuarioType) obj;
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
            ((this.i_cuenta==null && other.getI_cuenta()==null) || 
             (this.i_cuenta!=null &&
              this.i_cuenta.equals(other.getI_cuenta()))) &&
            ((this.i_idaplicacion==null && other.getI_idaplicacion()==null) || 
             (this.i_idaplicacion!=null &&
              this.i_idaplicacion.equals(other.getI_idaplicacion()))) &&
            ((this.i_idtipoestado==null && other.getI_idtipoestado()==null) || 
             (this.i_idtipoestado!=null &&
              this.i_idtipoestado.equals(other.getI_idtipoestado()))) &&
            ((this.i_usuario==null && other.getI_usuario()==null) || 
             (this.i_usuario!=null &&
              this.i_usuario.equals(other.getI_usuario())));
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
        if (getI_cuenta() != null) {
            _hashCode += getI_cuenta().hashCode();
        }
        if (getI_idaplicacion() != null) {
            _hashCode += getI_idaplicacion().hashCode();
        }
        if (getI_idtipoestado() != null) {
            _hashCode += getI_idtipoestado().hashCode();
        }
        if (getI_usuario() != null) {
            _hashCode += getI_usuario().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestAppUsuarioType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarAplicacionesUsrOSBSAUService/types", "RequestAppUsuarioType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_accion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarAplicacionesUsrOSBSAUService/types", "i_accion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_cuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarAplicacionesUsrOSBSAUService/types", "i_cuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_idaplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarAplicacionesUsrOSBSAUService/types", "i_idaplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_idtipoestado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarAplicacionesUsrOSBSAUService/types", "i_idtipoestado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/actualizarAplicacionesUsrOSBSAUService/types", "i_usuario"));
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
