/**
 * ResetPasswordServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.reiniciarpassword;

public class ResetPasswordServiceLocator extends org.apache.axis.client.Service implements entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordService {

    public ResetPasswordServiceLocator() {
    }


    public ResetPasswordServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ResetPasswordServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ResetPasswordPort
    private java.lang.String ResetPasswordPort_address = "http://esbdesa.entel.cl:80/enterprise/securityapp/accesousuarios/ent_t_px_reiniciarpasswordosbsauserviceps";

    public java.lang.String getResetPasswordPortAddress() {
        return ResetPasswordPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ResetPasswordPortWSDDServiceName = "ResetPasswordPort";

    public java.lang.String getResetPasswordPortWSDDServiceName() {
        return ResetPasswordPortWSDDServiceName;
    }

    public void setResetPasswordPortWSDDServiceName(java.lang.String name) {
        ResetPasswordPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordPortType getResetPasswordPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ResetPasswordPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getResetPasswordPort(endpoint);
    }

    public entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordPortType getResetPasswordPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordBindingStub _stub = new entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordBindingStub(portAddress, this);
            _stub.setPortName(getResetPasswordPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setResetPasswordPortEndpointAddress(java.lang.String address) {
        ResetPasswordPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordBindingStub _stub = new entel.oim.connectors.sau.webservices.reiniciarpassword.ResetPasswordBindingStub(new java.net.URL(ResetPasswordPort_address), this);
                _stub.setPortName(getResetPasswordPortWSDDServiceName());
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
        if ("ResetPasswordPort".equals(inputPortName)) {
            return getResetPasswordPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/reiniciarPasswordOSBSAUService", "ResetPasswordService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/reiniciarPasswordOSBSAUService", "ResetPasswordPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ResetPasswordPort".equals(portName)) {
            setResetPasswordPortEndpointAddress(address);
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
