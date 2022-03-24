/**
 * RequestCrearUsuarioType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.crearusuario;

public class RequestCrearUsuarioType  implements java.io.Serializable {
    private java.lang.String i_rut;

    private java.lang.String i_nombre;

    private java.lang.String i_apellido;

    private java.lang.String i_email;

    private java.lang.String i_cuenta;

    private java.lang.String i_password;

    private java.lang.String i_tipocompania;

    private java.lang.String i_idtipoestado;

    private java.lang.String i_usuario;

    private java.lang.String i_tipousuario;

    public RequestCrearUsuarioType() {
    }

    public RequestCrearUsuarioType(
           java.lang.String i_rut,
           java.lang.String i_nombre,
           java.lang.String i_apellido,
           java.lang.String i_email,
           java.lang.String i_cuenta,
           java.lang.String i_password,
           java.lang.String i_tipocompania,
           java.lang.String i_idtipoestado,
           java.lang.String i_usuario,
           java.lang.String i_tipousuario) {
           this.i_rut = i_rut;
           this.i_nombre = i_nombre;
           this.i_apellido = i_apellido;
           this.i_email = i_email;
           this.i_cuenta = i_cuenta;
           this.i_password = i_password;
           this.i_tipocompania = i_tipocompania;
           this.i_idtipoestado = i_idtipoestado;
           this.i_usuario = i_usuario;
           this.i_tipousuario = i_tipousuario;
    }


    /**
     * Gets the i_rut value for this RequestCrearUsuarioType.
     * 
     * @return i_rut
     */
    public java.lang.String getI_rut() {
        return i_rut;
    }


    /**
     * Sets the i_rut value for this RequestCrearUsuarioType.
     * 
     * @param i_rut
     */
    public void setI_rut(java.lang.String i_rut) {
        this.i_rut = i_rut;
    }


    /**
     * Gets the i_nombre value for this RequestCrearUsuarioType.
     * 
     * @return i_nombre
     */
    public java.lang.String getI_nombre() {
        return i_nombre;
    }


    /**
     * Sets the i_nombre value for this RequestCrearUsuarioType.
     * 
     * @param i_nombre
     */
    public void setI_nombre(java.lang.String i_nombre) {
        this.i_nombre = i_nombre;
    }


    /**
     * Gets the i_apellido value for this RequestCrearUsuarioType.
     * 
     * @return i_apellido
     */
    public java.lang.String getI_apellido() {
        return i_apellido;
    }


    /**
     * Sets the i_apellido value for this RequestCrearUsuarioType.
     * 
     * @param i_apellido
     */
    public void setI_apellido(java.lang.String i_apellido) {
        this.i_apellido = i_apellido;
    }


    /**
     * Gets the i_email value for this RequestCrearUsuarioType.
     * 
     * @return i_email
     */
    public java.lang.String getI_email() {
        return i_email;
    }


    /**
     * Sets the i_email value for this RequestCrearUsuarioType.
     * 
     * @param i_email
     */
    public void setI_email(java.lang.String i_email) {
        this.i_email = i_email;
    }


    /**
     * Gets the i_cuenta value for this RequestCrearUsuarioType.
     * 
     * @return i_cuenta
     */
    public java.lang.String getI_cuenta() {
        return i_cuenta;
    }


    /**
     * Sets the i_cuenta value for this RequestCrearUsuarioType.
     * 
     * @param i_cuenta
     */
    public void setI_cuenta(java.lang.String i_cuenta) {
        this.i_cuenta = i_cuenta;
    }


    /**
     * Gets the i_password value for this RequestCrearUsuarioType.
     * 
     * @return i_password
     */
    public java.lang.String getI_password() {
        return i_password;
    }


    /**
     * Sets the i_password value for this RequestCrearUsuarioType.
     * 
     * @param i_password
     */
    public void setI_password(java.lang.String i_password) {
        this.i_password = i_password;
    }


    /**
     * Gets the i_tipocompania value for this RequestCrearUsuarioType.
     * 
     * @return i_tipocompania
     */
    public java.lang.String getI_tipocompania() {
        return i_tipocompania;
    }


    /**
     * Sets the i_tipocompania value for this RequestCrearUsuarioType.
     * 
     * @param i_tipocompania
     */
    public void setI_tipocompania(java.lang.String i_tipocompania) {
        this.i_tipocompania = i_tipocompania;
    }


    /**
     * Gets the i_idtipoestado value for this RequestCrearUsuarioType.
     * 
     * @return i_idtipoestado
     */
    public java.lang.String getI_idtipoestado() {
        return i_idtipoestado;
    }


    /**
     * Sets the i_idtipoestado value for this RequestCrearUsuarioType.
     * 
     * @param i_idtipoestado
     */
    public void setI_idtipoestado(java.lang.String i_idtipoestado) {
        this.i_idtipoestado = i_idtipoestado;
    }


    /**
     * Gets the i_usuario value for this RequestCrearUsuarioType.
     * 
     * @return i_usuario
     */
    public java.lang.String getI_usuario() {
        return i_usuario;
    }


    /**
     * Sets the i_usuario value for this RequestCrearUsuarioType.
     * 
     * @param i_usuario
     */
    public void setI_usuario(java.lang.String i_usuario) {
        this.i_usuario = i_usuario;
    }


    /**
     * Gets the i_tipousuario value for this RequestCrearUsuarioType.
     * 
     * @return i_tipousuario
     */
    public java.lang.String getI_tipousuario() {
        return i_tipousuario;
    }


    /**
     * Sets the i_tipousuario value for this RequestCrearUsuarioType.
     * 
     * @param i_tipousuario
     */
    public void setI_tipousuario(java.lang.String i_tipousuario) {
        this.i_tipousuario = i_tipousuario;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RequestCrearUsuarioType)) return false;
        RequestCrearUsuarioType other = (RequestCrearUsuarioType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.i_rut==null && other.getI_rut()==null) || 
             (this.i_rut!=null &&
              this.i_rut.equals(other.getI_rut()))) &&
            ((this.i_nombre==null && other.getI_nombre()==null) || 
             (this.i_nombre!=null &&
              this.i_nombre.equals(other.getI_nombre()))) &&
            ((this.i_apellido==null && other.getI_apellido()==null) || 
             (this.i_apellido!=null &&
              this.i_apellido.equals(other.getI_apellido()))) &&
            ((this.i_email==null && other.getI_email()==null) || 
             (this.i_email!=null &&
              this.i_email.equals(other.getI_email()))) &&
            ((this.i_cuenta==null && other.getI_cuenta()==null) || 
             (this.i_cuenta!=null &&
              this.i_cuenta.equals(other.getI_cuenta()))) &&
            ((this.i_password==null && other.getI_password()==null) || 
             (this.i_password!=null &&
              this.i_password.equals(other.getI_password()))) &&
            ((this.i_tipocompania==null && other.getI_tipocompania()==null) || 
             (this.i_tipocompania!=null &&
              this.i_tipocompania.equals(other.getI_tipocompania()))) &&
            ((this.i_idtipoestado==null && other.getI_idtipoestado()==null) || 
             (this.i_idtipoestado!=null &&
              this.i_idtipoestado.equals(other.getI_idtipoestado()))) &&
            ((this.i_usuario==null && other.getI_usuario()==null) || 
             (this.i_usuario!=null &&
              this.i_usuario.equals(other.getI_usuario()))) &&
            ((this.i_tipousuario==null && other.getI_tipousuario()==null) || 
             (this.i_tipousuario!=null &&
              this.i_tipousuario.equals(other.getI_tipousuario())));
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
        if (getI_rut() != null) {
            _hashCode += getI_rut().hashCode();
        }
        if (getI_nombre() != null) {
            _hashCode += getI_nombre().hashCode();
        }
        if (getI_apellido() != null) {
            _hashCode += getI_apellido().hashCode();
        }
        if (getI_email() != null) {
            _hashCode += getI_email().hashCode();
        }
        if (getI_cuenta() != null) {
            _hashCode += getI_cuenta().hashCode();
        }
        if (getI_password() != null) {
            _hashCode += getI_password().hashCode();
        }
        if (getI_tipocompania() != null) {
            _hashCode += getI_tipocompania().hashCode();
        }
        if (getI_idtipoestado() != null) {
            _hashCode += getI_idtipoestado().hashCode();
        }
        if (getI_usuario() != null) {
            _hashCode += getI_usuario().hashCode();
        }
        if (getI_tipousuario() != null) {
            _hashCode += getI_tipousuario().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RequestCrearUsuarioType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "RequestCrearUsuarioType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_rut");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_rut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_apellido");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_apellido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_cuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_cuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_tipocompania");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_tipocompania"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_idtipoestado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_idtipoestado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("i_tipousuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/crearUsuarioOSBSAUService/types", "i_tipousuario"));
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
