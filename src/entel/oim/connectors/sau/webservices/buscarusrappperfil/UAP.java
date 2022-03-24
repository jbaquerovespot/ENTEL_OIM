/**
 * UAP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.buscarusrappperfil;

public class UAP  implements java.io.Serializable {
    private java.lang.String e_nomapli;

    private java.lang.String e_nomperfil;

    private java.lang.String e_nomestado;

    public UAP() {
    }

    public UAP(
           java.lang.String e_nomapli,
           java.lang.String e_nomperfil,
           java.lang.String e_nomestado) {
           this.e_nomapli = e_nomapli;
           this.e_nomperfil = e_nomperfil;
           this.e_nomestado = e_nomestado;
    }


    /**
     * Gets the e_nomapli value for this UAP.
     * 
     * @return e_nomapli
     */
    public java.lang.String getE_nomapli() {
        return e_nomapli;
    }


    /**
     * Sets the e_nomapli value for this UAP.
     * 
     * @param e_nomapli
     */
    public void setE_nomapli(java.lang.String e_nomapli) {
        this.e_nomapli = e_nomapli;
    }


    /**
     * Gets the e_nomperfil value for this UAP.
     * 
     * @return e_nomperfil
     */
    public java.lang.String getE_nomperfil() {
        return e_nomperfil;
    }


    /**
     * Sets the e_nomperfil value for this UAP.
     * 
     * @param e_nomperfil
     */
    public void setE_nomperfil(java.lang.String e_nomperfil) {
        this.e_nomperfil = e_nomperfil;
    }


    /**
     * Gets the e_nomestado value for this UAP.
     * 
     * @return e_nomestado
     */
    public java.lang.String getE_nomestado() {
        return e_nomestado;
    }


    /**
     * Sets the e_nomestado value for this UAP.
     * 
     * @param e_nomestado
     */
    public void setE_nomestado(java.lang.String e_nomestado) {
        this.e_nomestado = e_nomestado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UAP)) return false;
        UAP other = (UAP) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.e_nomapli==null && other.getE_nomapli()==null) || 
             (this.e_nomapli!=null &&
              this.e_nomapli.equals(other.getE_nomapli()))) &&
            ((this.e_nomperfil==null && other.getE_nomperfil()==null) || 
             (this.e_nomperfil!=null &&
              this.e_nomperfil.equals(other.getE_nomperfil()))) &&
            ((this.e_nomestado==null && other.getE_nomestado()==null) || 
             (this.e_nomestado!=null &&
              this.e_nomestado.equals(other.getE_nomestado())));
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
        if (getE_nomapli() != null) {
            _hashCode += getE_nomapli().hashCode();
        }
        if (getE_nomperfil() != null) {
            _hashCode += getE_nomperfil().hashCode();
        }
        if (getE_nomestado() != null) {
            _hashCode += getE_nomestado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UAP.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsrAppPerfilOSBService/types", "UAP"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_nomapli");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsrAppPerfilOSBService/types", "e_nomapli"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_nomperfil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsrAppPerfilOSBService/types", "e_nomperfil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_nomestado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsrAppPerfilOSBService/types", "e_nomestado"));
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
