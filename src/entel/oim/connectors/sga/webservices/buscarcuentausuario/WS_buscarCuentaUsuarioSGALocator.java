/**
 * WS_buscarCuentaUsuarioSGALocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.buscarcuentausuario;

public class WS_buscarCuentaUsuarioSGALocator extends org.apache.axis.client.Service implements entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGA {

    public WS_buscarCuentaUsuarioSGALocator() {
    }


    public WS_buscarCuentaUsuarioSGALocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WS_buscarCuentaUsuarioSGALocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WS_buscarCuentaUsuarioSGAPort
    private java.lang.String WS_buscarCuentaUsuarioSGAPort_address = "http://esbdesa.entel.cl:80/crm/crmloyalty/customerintermanag/crm_t_px_buscarcuentausuariosgaps";

    public java.lang.String getWS_buscarCuentaUsuarioSGAPortAddress() {
        return WS_buscarCuentaUsuarioSGAPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WS_buscarCuentaUsuarioSGAPortWSDDServiceName = "WS_buscarCuentaUsuarioSGAPort";

    public java.lang.String getWS_buscarCuentaUsuarioSGAPortWSDDServiceName() {
        return WS_buscarCuentaUsuarioSGAPortWSDDServiceName;
    }

    public void setWS_buscarCuentaUsuarioSGAPortWSDDServiceName(java.lang.String name) {
        WS_buscarCuentaUsuarioSGAPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGAPortType getWS_buscarCuentaUsuarioSGAPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WS_buscarCuentaUsuarioSGAPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWS_buscarCuentaUsuarioSGAPort(endpoint);
    }

    public entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGAPortType getWS_buscarCuentaUsuarioSGAPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGABindingStub _stub = new entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGABindingStub(portAddress, this);
            _stub.setPortName(getWS_buscarCuentaUsuarioSGAPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWS_buscarCuentaUsuarioSGAPortEndpointAddress(java.lang.String address) {
        WS_buscarCuentaUsuarioSGAPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGAPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGABindingStub _stub = new entel.oim.connectors.sga.webservices.buscarcuentausuario.WS_buscarCuentaUsuarioSGABindingStub(new java.net.URL(WS_buscarCuentaUsuarioSGAPort_address), this);
                _stub.setPortName(getWS_buscarCuentaUsuarioSGAPortWSDDServiceName());
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
        if ("WS_buscarCuentaUsuarioSGAPort".equals(inputPortName)) {
            return getWS_buscarCuentaUsuarioSGAPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/buscarCuentaUsuarioSGA/", "WS_buscarCuentaUsuarioSGA");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/buscarCuentaUsuarioSGA/", "WS_buscarCuentaUsuarioSGAPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WS_buscarCuentaUsuarioSGAPort".equals(portName)) {
            setWS_buscarCuentaUsuarioSGAPortEndpointAddress(address);
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
