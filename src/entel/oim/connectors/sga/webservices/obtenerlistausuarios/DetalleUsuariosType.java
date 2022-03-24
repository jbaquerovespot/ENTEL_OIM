/**
 * DetalleUsuariosType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.obtenerlistausuarios;

public class DetalleUsuariosType  implements java.io.Serializable {
    private java.lang.String login;

    private java.lang.String plataforma;

    private java.lang.String rutUsuario;

    private java.lang.String tipoLicencia;

    private java.lang.String nombres;

    private java.lang.String email;

    private java.lang.String estadoBack;

    private java.lang.String loginGenesys;

    private java.lang.String fono;

    private entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleGruposType[] grupos;

    public DetalleUsuariosType() {
    }

    public DetalleUsuariosType(
           java.lang.String login,
           java.lang.String plataforma,
           java.lang.String rutUsuario,
           java.lang.String tipoLicencia,
           java.lang.String nombres,
           java.lang.String email,
           java.lang.String estadoBack,
           java.lang.String loginGenesys,
           java.lang.String fono,
           entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleGruposType[] grupos) {
           this.login = login;
           this.plataforma = plataforma;
           this.rutUsuario = rutUsuario;
           this.tipoLicencia = tipoLicencia;
           this.nombres = nombres;
           this.email = email;
           this.estadoBack = estadoBack;
           this.loginGenesys = loginGenesys;
           this.fono = fono;
           this.grupos = grupos;
    }


    /**
     * Gets the login value for this DetalleUsuariosType.
     * 
     * @return login
     */
    public java.lang.String getLogin() {
        return login;
    }


    /**
     * Sets the login value for this DetalleUsuariosType.
     * 
     * @param login
     */
    public void setLogin(java.lang.String login) {
        this.login = login;
    }


    /**
     * Gets the plataforma value for this DetalleUsuariosType.
     * 
     * @return plataforma
     */
    public java.lang.String getPlataforma() {
        return plataforma;
    }


    /**
     * Sets the plataforma value for this DetalleUsuariosType.
     * 
     * @param plataforma
     */
    public void setPlataforma(java.lang.String plataforma) {
        this.plataforma = plataforma;
    }


    /**
     * Gets the rutUsuario value for this DetalleUsuariosType.
     * 
     * @return rutUsuario
     */
    public java.lang.String getRutUsuario() {
        return rutUsuario;
    }


    /**
     * Sets the rutUsuario value for this DetalleUsuariosType.
     * 
     * @param rutUsuario
     */
    public void setRutUsuario(java.lang.String rutUsuario) {
        this.rutUsuario = rutUsuario;
    }


    /**
     * Gets the tipoLicencia value for this DetalleUsuariosType.
     * 
     * @return tipoLicencia
     */
    public java.lang.String getTipoLicencia() {
        return tipoLicencia;
    }


    /**
     * Sets the tipoLicencia value for this DetalleUsuariosType.
     * 
     * @param tipoLicencia
     */
    public void setTipoLicencia(java.lang.String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }


    /**
     * Gets the nombres value for this DetalleUsuariosType.
     * 
     * @return nombres
     */
    public java.lang.String getNombres() {
        return nombres;
    }


    /**
     * Sets the nombres value for this DetalleUsuariosType.
     * 
     * @param nombres
     */
    public void setNombres(java.lang.String nombres) {
        this.nombres = nombres;
    }


    /**
     * Gets the email value for this DetalleUsuariosType.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this DetalleUsuariosType.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the estadoBack value for this DetalleUsuariosType.
     * 
     * @return estadoBack
     */
    public java.lang.String getEstadoBack() {
        return estadoBack;
    }


    /**
     * Sets the estadoBack value for this DetalleUsuariosType.
     * 
     * @param estadoBack
     */
    public void setEstadoBack(java.lang.String estadoBack) {
        this.estadoBack = estadoBack;
    }


    /**
     * Gets the loginGenesys value for this DetalleUsuariosType.
     * 
     * @return loginGenesys
     */
    public java.lang.String getLoginGenesys() {
        return loginGenesys;
    }


    /**
     * Sets the loginGenesys value for this DetalleUsuariosType.
     * 
     * @param loginGenesys
     */
    public void setLoginGenesys(java.lang.String loginGenesys) {
        this.loginGenesys = loginGenesys;
    }


    /**
     * Gets the fono value for this DetalleUsuariosType.
     * 
     * @return fono
     */
    public java.lang.String getFono() {
        return fono;
    }


    /**
     * Sets the fono value for this DetalleUsuariosType.
     * 
     * @param fono
     */
    public void setFono(java.lang.String fono) {
        this.fono = fono;
    }


    /**
     * Gets the grupos value for this DetalleUsuariosType.
     * 
     * @return grupos
     */
    public entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleGruposType[] getGrupos() {
        return grupos;
    }


    /**
     * Sets the grupos value for this DetalleUsuariosType.
     * 
     * @param grupos
     */
    public void setGrupos(entel.oim.connectors.sga.webservices.obtenerlistausuarios.DetalleGruposType[] grupos) {
        this.grupos = grupos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetalleUsuariosType)) return false;
        DetalleUsuariosType other = (DetalleUsuariosType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.login==null && other.getLogin()==null) || 
             (this.login!=null &&
              this.login.equals(other.getLogin()))) &&
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
            ((this.fono==null && other.getFono()==null) || 
             (this.fono!=null &&
              this.fono.equals(other.getFono()))) &&
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
        if (getLogin() != null) {
            _hashCode += getLogin().hashCode();
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
        if (getFono() != null) {
            _hashCode += getFono().hashCode();
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
        new org.apache.axis.description.TypeDesc(DetalleUsuariosType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/obtenerListaUsuarioSGA/Response", "DetalleUsuariosType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("login");
        elemField.setXmlName(new javax.xml.namespace.QName("", "login"));
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
        elemField.setFieldName("fono");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grupos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "grupos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/obtenerListaUsuarioSGA/Response", "DetalleGruposType"));
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
