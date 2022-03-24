/**
 * ResponseBuscarUsuarioType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.buscarusuario;

public class ResponseBuscarUsuarioType  implements java.io.Serializable {
    private java.lang.String e_codigousr;

    private java.lang.String e_run;

    private java.lang.String e_nombres;

    private java.lang.String e_apellidos;

    private java.lang.String e_email;

    private java.lang.String e_cuenta;

    private java.lang.String e_idcompania;

    private java.lang.String e_compania;

    private java.lang.String e_estado;

    private java.lang.String e_idtipousuario;

    private java.lang.String e_tipousuario;

    private entel.oim.connectors.sau.webservices.buscarusuario.Aplicaciones[] e_aplicaciones;

    private java.lang.String e_fchconexion;

    private java.lang.String e_codigo;

    private java.lang.String e_mensaje;

    public ResponseBuscarUsuarioType() {
    }

    public ResponseBuscarUsuarioType(
           java.lang.String e_codigousr,
           java.lang.String e_run,
           java.lang.String e_nombres,
           java.lang.String e_apellidos,
           java.lang.String e_email,
           java.lang.String e_cuenta,
           java.lang.String e_idcompania,
           java.lang.String e_compania,
           java.lang.String e_estado,
           java.lang.String e_idtipousuario,
           java.lang.String e_tipousuario,
           entel.oim.connectors.sau.webservices.buscarusuario.Aplicaciones[] e_aplicaciones,
           java.lang.String e_fchconexion,
           java.lang.String e_codigo,
           java.lang.String e_mensaje) {
           this.e_codigousr = e_codigousr;
           this.e_run = e_run;
           this.e_nombres = e_nombres;
           this.e_apellidos = e_apellidos;
           this.e_email = e_email;
           this.e_cuenta = e_cuenta;
           this.e_idcompania = e_idcompania;
           this.e_compania = e_compania;
           this.e_estado = e_estado;
           this.e_idtipousuario = e_idtipousuario;
           this.e_tipousuario = e_tipousuario;
           this.e_aplicaciones = e_aplicaciones;
           this.e_fchconexion = e_fchconexion;
           this.e_codigo = e_codigo;
           this.e_mensaje = e_mensaje;
    }


    /**
     * Gets the e_codigousr value for this ResponseBuscarUsuarioType.
     * 
     * @return e_codigousr
     */
    public java.lang.String getE_codigousr() {
        return e_codigousr;
    }


    /**
     * Sets the e_codigousr value for this ResponseBuscarUsuarioType.
     * 
     * @param e_codigousr
     */
    public void setE_codigousr(java.lang.String e_codigousr) {
        this.e_codigousr = e_codigousr;
    }


    /**
     * Gets the e_run value for this ResponseBuscarUsuarioType.
     * 
     * @return e_run
     */
    public java.lang.String getE_run() {
        return e_run;
    }


    /**
     * Sets the e_run value for this ResponseBuscarUsuarioType.
     * 
     * @param e_run
     */
    public void setE_run(java.lang.String e_run) {
        this.e_run = e_run;
    }


    /**
     * Gets the e_nombres value for this ResponseBuscarUsuarioType.
     * 
     * @return e_nombres
     */
    public java.lang.String getE_nombres() {
        return e_nombres;
    }


    /**
     * Sets the e_nombres value for this ResponseBuscarUsuarioType.
     * 
     * @param e_nombres
     */
    public void setE_nombres(java.lang.String e_nombres) {
        this.e_nombres = e_nombres;
    }


    /**
     * Gets the e_apellidos value for this ResponseBuscarUsuarioType.
     * 
     * @return e_apellidos
     */
    public java.lang.String getE_apellidos() {
        return e_apellidos;
    }


    /**
     * Sets the e_apellidos value for this ResponseBuscarUsuarioType.
     * 
     * @param e_apellidos
     */
    public void setE_apellidos(java.lang.String e_apellidos) {
        this.e_apellidos = e_apellidos;
    }


    /**
     * Gets the e_email value for this ResponseBuscarUsuarioType.
     * 
     * @return e_email
     */
    public java.lang.String getE_email() {
        return e_email;
    }


    /**
     * Sets the e_email value for this ResponseBuscarUsuarioType.
     * 
     * @param e_email
     */
    public void setE_email(java.lang.String e_email) {
        this.e_email = e_email;
    }


    /**
     * Gets the e_cuenta value for this ResponseBuscarUsuarioType.
     * 
     * @return e_cuenta
     */
    public java.lang.String getE_cuenta() {
        return e_cuenta;
    }


    /**
     * Sets the e_cuenta value for this ResponseBuscarUsuarioType.
     * 
     * @param e_cuenta
     */
    public void setE_cuenta(java.lang.String e_cuenta) {
        this.e_cuenta = e_cuenta;
    }


    /**
     * Gets the e_idcompania value for this ResponseBuscarUsuarioType.
     * 
     * @return e_idcompania
     */
    public java.lang.String getE_idcompania() {
        return e_idcompania;
    }


    /**
     * Sets the e_idcompania value for this ResponseBuscarUsuarioType.
     * 
     * @param e_idcompania
     */
    public void setE_idcompania(java.lang.String e_idcompania) {
        this.e_idcompania = e_idcompania;
    }


    /**
     * Gets the e_compania value for this ResponseBuscarUsuarioType.
     * 
     * @return e_compania
     */
    public java.lang.String getE_compania() {
        return e_compania;
    }


    /**
     * Sets the e_compania value for this ResponseBuscarUsuarioType.
     * 
     * @param e_compania
     */
    public void setE_compania(java.lang.String e_compania) {
        this.e_compania = e_compania;
    }


    /**
     * Gets the e_estado value for this ResponseBuscarUsuarioType.
     * 
     * @return e_estado
     */
    public java.lang.String getE_estado() {
        return e_estado;
    }


    /**
     * Sets the e_estado value for this ResponseBuscarUsuarioType.
     * 
     * @param e_estado
     */
    public void setE_estado(java.lang.String e_estado) {
        this.e_estado = e_estado;
    }


    /**
     * Gets the e_idtipousuario value for this ResponseBuscarUsuarioType.
     * 
     * @return e_idtipousuario
     */
    public java.lang.String getE_idtipousuario() {
        return e_idtipousuario;
    }


    /**
     * Sets the e_idtipousuario value for this ResponseBuscarUsuarioType.
     * 
     * @param e_idtipousuario
     */
    public void setE_idtipousuario(java.lang.String e_idtipousuario) {
        this.e_idtipousuario = e_idtipousuario;
    }


    /**
     * Gets the e_tipousuario value for this ResponseBuscarUsuarioType.
     * 
     * @return e_tipousuario
     */
    public java.lang.String getE_tipousuario() {
        return e_tipousuario;
    }


    /**
     * Sets the e_tipousuario value for this ResponseBuscarUsuarioType.
     * 
     * @param e_tipousuario
     */
    public void setE_tipousuario(java.lang.String e_tipousuario) {
        this.e_tipousuario = e_tipousuario;
    }


    /**
     * Gets the e_aplicaciones value for this ResponseBuscarUsuarioType.
     * 
     * @return e_aplicaciones
     */
    public entel.oim.connectors.sau.webservices.buscarusuario.Aplicaciones[] getE_aplicaciones() {
        return e_aplicaciones;
    }


    /**
     * Sets the e_aplicaciones value for this ResponseBuscarUsuarioType.
     * 
     * @param e_aplicaciones
     */
    public void setE_aplicaciones(entel.oim.connectors.sau.webservices.buscarusuario.Aplicaciones[] e_aplicaciones) {
        this.e_aplicaciones = e_aplicaciones;
    }


    /**
     * Gets the e_fchconexion value for this ResponseBuscarUsuarioType.
     * 
     * @return e_fchconexion
     */
    public java.lang.String getE_fchconexion() {
        return e_fchconexion;
    }


    /**
     * Sets the e_fchconexion value for this ResponseBuscarUsuarioType.
     * 
     * @param e_fchconexion
     */
    public void setE_fchconexion(java.lang.String e_fchconexion) {
        this.e_fchconexion = e_fchconexion;
    }


    /**
     * Gets the e_codigo value for this ResponseBuscarUsuarioType.
     * 
     * @return e_codigo
     */
    public java.lang.String getE_codigo() {
        return e_codigo;
    }


    /**
     * Sets the e_codigo value for this ResponseBuscarUsuarioType.
     * 
     * @param e_codigo
     */
    public void setE_codigo(java.lang.String e_codigo) {
        this.e_codigo = e_codigo;
    }


    /**
     * Gets the e_mensaje value for this ResponseBuscarUsuarioType.
     * 
     * @return e_mensaje
     */
    public java.lang.String getE_mensaje() {
        return e_mensaje;
    }


    /**
     * Sets the e_mensaje value for this ResponseBuscarUsuarioType.
     * 
     * @param e_mensaje
     */
    public void setE_mensaje(java.lang.String e_mensaje) {
        this.e_mensaje = e_mensaje;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResponseBuscarUsuarioType)) return false;
        ResponseBuscarUsuarioType other = (ResponseBuscarUsuarioType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_codigousr==null && other.getE_codigousr()==null) || 
             (this.e_codigousr!=null &&
              this.e_codigousr.equals(other.getE_codigousr()))) &&
            ((this.e_run==null && other.getE_run()==null) || 
             (this.e_run!=null &&
              this.e_run.equals(other.getE_run()))) &&
            ((this.e_nombres==null && other.getE_nombres()==null) || 
             (this.e_nombres!=null &&
              this.e_nombres.equals(other.getE_nombres()))) &&
            ((this.e_apellidos==null && other.getE_apellidos()==null) || 
             (this.e_apellidos!=null &&
              this.e_apellidos.equals(other.getE_apellidos()))) &&
            ((this.e_email==null && other.getE_email()==null) || 
             (this.e_email!=null &&
              this.e_email.equals(other.getE_email()))) &&
            ((this.e_cuenta==null && other.getE_cuenta()==null) || 
             (this.e_cuenta!=null &&
              this.e_cuenta.equals(other.getE_cuenta()))) &&
            ((this.e_idcompania==null && other.getE_idcompania()==null) || 
             (this.e_idcompania!=null &&
              this.e_idcompania.equals(other.getE_idcompania()))) &&
            ((this.e_compania==null && other.getE_compania()==null) || 
             (this.e_compania!=null &&
              this.e_compania.equals(other.getE_compania()))) &&
            ((this.e_estado==null && other.getE_estado()==null) || 
             (this.e_estado!=null &&
              this.e_estado.equals(other.getE_estado()))) &&
            ((this.e_idtipousuario==null && other.getE_idtipousuario()==null) || 
             (this.e_idtipousuario!=null &&
              this.e_idtipousuario.equals(other.getE_idtipousuario()))) &&
            ((this.e_tipousuario==null && other.getE_tipousuario()==null) || 
             (this.e_tipousuario!=null &&
              this.e_tipousuario.equals(other.getE_tipousuario()))) &&
            ((this.e_aplicaciones==null && other.getE_aplicaciones()==null) || 
             (this.e_aplicaciones!=null &&
              java.util.Arrays.equals(this.e_aplicaciones, other.getE_aplicaciones()))) &&
            ((this.e_fchconexion==null && other.getE_fchconexion()==null) || 
             (this.e_fchconexion!=null &&
              this.e_fchconexion.equals(other.getE_fchconexion()))) &&
            ((this.e_codigo==null && other.getE_codigo()==null) || 
             (this.e_codigo!=null &&
              this.e_codigo.equals(other.getE_codigo()))) &&
            ((this.e_mensaje==null && other.getE_mensaje()==null) || 
             (this.e_mensaje!=null &&
              this.e_mensaje.equals(other.getE_mensaje())));
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
        if (getE_codigousr() != null) {
            _hashCode += getE_codigousr().hashCode();
        }
        if (getE_run() != null) {
            _hashCode += getE_run().hashCode();
        }
        if (getE_nombres() != null) {
            _hashCode += getE_nombres().hashCode();
        }
        if (getE_apellidos() != null) {
            _hashCode += getE_apellidos().hashCode();
        }
        if (getE_email() != null) {
            _hashCode += getE_email().hashCode();
        }
        if (getE_cuenta() != null) {
            _hashCode += getE_cuenta().hashCode();
        }
        if (getE_idcompania() != null) {
            _hashCode += getE_idcompania().hashCode();
        }
        if (getE_compania() != null) {
            _hashCode += getE_compania().hashCode();
        }
        if (getE_estado() != null) {
            _hashCode += getE_estado().hashCode();
        }
        if (getE_idtipousuario() != null) {
            _hashCode += getE_idtipousuario().hashCode();
        }
        if (getE_tipousuario() != null) {
            _hashCode += getE_tipousuario().hashCode();
        }
        if (getE_aplicaciones() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getE_aplicaciones());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getE_aplicaciones(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getE_fchconexion() != null) {
            _hashCode += getE_fchconexion().hashCode();
        }
        if (getE_codigo() != null) {
            _hashCode += getE_codigo().hashCode();
        }
        if (getE_mensaje() != null) {
            _hashCode += getE_mensaje().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResponseBuscarUsuarioType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "ResponseBuscarUsuarioType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_codigousr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_codigousr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_run");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_run"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_nombres");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_nombres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_apellidos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_apellidos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_cuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_cuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idcompania");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_idcompania"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_compania");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_compania"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_idtipousuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_idtipousuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_tipousuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_tipousuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_aplicaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_aplicaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "Aplicaciones"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_fchconexion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_fchconexion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_mensaje");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService/types", "e_mensaje"));
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
