/**
 * ModificarCuentasUsuarioSGARequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.modificarcuentasusuario;

public class ModificarCuentasUsuarioSGARequestType  implements java.io.Serializable {
    private java.lang.String loginName;

    private java.lang.String plataforma;

    private java.lang.String rutUsuario;

    private java.lang.String tipoLicencia;

    private java.lang.String nombres;

    private java.lang.String email;

    private java.lang.String estadoBack;

    private java.lang.String loginGenesys;

    private java.lang.String telefono;

    public ModificarCuentasUsuarioSGARequestType() {
    }

    public ModificarCuentasUsuarioSGARequestType(
           java.lang.String loginName,
           java.lang.String plataforma,
           java.lang.String rutUsuario,
           java.lang.String tipoLicencia,
           java.lang.String nombres,
           java.lang.String email,
           java.lang.String estadoBack,
           java.lang.String loginGenesys,
           java.lang.String telefono) {
           this.loginName = loginName;
           this.plataforma = plataforma;
           this.rutUsuario = rutUsuario;
           this.tipoLicencia = tipoLicencia;
           this.nombres = nombres;
           this.email = email;
           this.estadoBack = estadoBack;
           this.loginGenesys = loginGenesys;
           this.telefono = telefono;
    }


    /**
     * Gets the loginName value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return loginName
     */
    public java.lang.String getLoginName() {
        return loginName;
    }


    /**
     * Sets the loginName value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param loginName
     */
    public void setLoginName(java.lang.String loginName) {
        this.loginName = loginName;
    }


    /**
     * Gets the plataforma value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return plataforma
     */
    public java.lang.String getPlataforma() {
        return plataforma;
    }


    /**
     * Sets the plataforma value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param plataforma
     */
    public void setPlataforma(java.lang.String plataforma) {
        this.plataforma = plataforma;
    }


    /**
     * Gets the rutUsuario value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return rutUsuario
     */
    public java.lang.String getRutUsuario() {
        return rutUsuario;
    }


    /**
     * Sets the rutUsuario value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param rutUsuario
     */
    public void setRutUsuario(java.lang.String rutUsuario) {
        this.rutUsuario = rutUsuario;
    }


    /**
     * Gets the tipoLicencia value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return tipoLicencia
     */
    public java.lang.String getTipoLicencia() {
        return tipoLicencia;
    }


    /**
     * Sets the tipoLicencia value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param tipoLicencia
     */
    public void setTipoLicencia(java.lang.String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }


    /**
     * Gets the nombres value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return nombres
     */
    public java.lang.String getNombres() {
        return nombres;
    }


    /**
     * Sets the nombres value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param nombres
     */
    public void setNombres(java.lang.String nombres) {
        this.nombres = nombres;
    }


    /**
     * Gets the email value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the estadoBack value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return estadoBack
     */
    public java.lang.String getEstadoBack() {
        return estadoBack;
    }


    /**
     * Sets the estadoBack value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param estadoBack
     */
    public void setEstadoBack(java.lang.String estadoBack) {
        this.estadoBack = estadoBack;
    }


    /**
     * Gets the loginGenesys value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return loginGenesys
     */
    public java.lang.String getLoginGenesys() {
        return loginGenesys;
    }


    /**
     * Sets the loginGenesys value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param loginGenesys
     */
    public void setLoginGenesys(java.lang.String loginGenesys) {
        this.loginGenesys = loginGenesys;
    }


    /**
     * Gets the telefono value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @return telefono
     */
    public java.lang.String getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this ModificarCuentasUsuarioSGARequestType.
     * 
     * @param telefono
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ModificarCuentasUsuarioSGARequestType)) return false;
        ModificarCuentasUsuarioSGARequestType other = (ModificarCuentasUsuarioSGARequestType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.loginName==null && other.getLoginName()==null) || 
             (this.loginName!=null &&
              this.loginName.equals(other.getLoginName()))) &&
            ((this.plataforma==null && other.getPlataforma()==null) || 
             (this.plataforma!=null &&
              this.plataforma.equals(other.getPlataforma()))) &&
            ((this.rutUsuario==null && other.getRutUsuario()==null) || 
             (this.rutUsuario!=null &&
              this.rutUsuario.equals(other.getRutUsuario()))) &&
            ((this.tipoLicencia==null && other.getTipoLicencia()==null) || 
             (this.tipoLicencia!=null &&
              this.tipoLicencia.equals(other.getTipoLicencia()))) &&
            ((this.nombres==null && other.getNombres()==null) || 
             (this.nombres!=null &&
              this.nombres.equals(other.getNombres()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.estadoBack==null && other.getEstadoBack()==null) || 
             (this.estadoBack!=null &&
              this.estadoBack.equals(other.getEstadoBack()))) &&
            ((this.loginGenesys==null && other.getLoginGenesys()==null) || 
             (this.loginGenesys!=null &&
              this.loginGenesys.equals(other.getLoginGenesys()))) &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono())));
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
        if (getLoginName() != null) {
            _hashCode += getLoginName().hashCode();
        }
        if (getPlataforma() != null) {
            _hashCode += getPlataforma().hashCode();
        }
        if (getRutUsuario() != null) {
            _hashCode += getRutUsuario().hashCode();
        }
        if (getTipoLicencia() != null) {
            _hashCode += getTipoLicencia().hashCode();
        }
        if (getNombres() != null) {
            _hashCode += getNombres().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getEstadoBack() != null) {
            _hashCode += getEstadoBack().hashCode();
        }
        if (getLoginGenesys() != null) {
            _hashCode += getLoginGenesys().hashCode();
        }
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ModificarCuentasUsuarioSGARequestType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/modificarCuentasUsuarioSGA/Request", "modificarCuentasUsuarioSGARequestType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "loginName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plataforma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "plataforma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rutUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rutUsuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoLicencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoLicencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombres");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoBack");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estadoBack"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginGenesys");
        elemField.setXmlName(new javax.xml.namespace.QName("", "loginGenesys"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("", "telefono"));
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
