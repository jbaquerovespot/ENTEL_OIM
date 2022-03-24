/**
 * ValidaExisteUsrServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.validarlogin;

public class ValidaExisteUsrServiceLocator extends org.apache.axis.client.Service implements entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrService {

    public ValidaExisteUsrServiceLocator() {
    }


    public ValidaExisteUsrServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ValidaExisteUsrServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ValidaExisteUsrPort
    private java.lang.String ValidaExisteUsrPort_address = "http://esbdesa.entel.cl:80/enterprise/securityapp/accesousuarios/ent_t_px_validarloginosbsauserviceps";

    public java.lang.String getValidaExisteUsrPortAddress() {
        return ValidaExisteUsrPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ValidaExisteUsrPortWSDDServiceName = "ValidaExisteUsrPort";

    public java.lang.String getValidaExisteUsrPortWSDDServiceName() {
        return ValidaExisteUsrPortWSDDServiceName;
    }

    public void setValidaExisteUsrPortWSDDServiceName(java.lang.String name) {
        ValidaExisteUsrPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrPortType getValidaExisteUsrPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ValidaExisteUsrPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getValidaExisteUsrPort(endpoint);
    }

    public entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrPortType getValidaExisteUsrPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrBindingStub _stub = new entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrBindingStub(portAddress, this);
            _stub.setPortName(getValidaExisteUsrPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setValidaExisteUsrPortEndpointAddress(java.lang.String address) {
        ValidaExisteUsrPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrBindingStub _stub = new entel.oim.connectors.sau.webservices.validarlogin.ValidaExisteUsrBindingStub(new java.net.URL(ValidaExisteUsrPort_address), this);
                _stub.setPortName(getValidaExisteUsrPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ValidaExisteUsrPort".equals(inputPortName)) {
            return getValidaExisteUsrPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/validarLoginOSBSAUService", "ValidaExisteUsrService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/validarLoginOSBSAUService", "ValidaExisteUsrPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ValidaExisteUsrPort".equals(portName)) {
            setValidaExisteUsrPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
