/**
 * WS_actualizarGruposUsuarioSGALocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.actualizargruposusuario;

public class WS_actualizarGruposUsuarioSGALocator extends org.apache.axis.client.Service implements entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGA {

    public WS_actualizarGruposUsuarioSGALocator() {
    }


    public WS_actualizarGruposUsuarioSGALocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WS_actualizarGruposUsuarioSGALocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WS_actualizarGruposUsuarioSGAPort
    private java.lang.String WS_actualizarGruposUsuarioSGAPort_address = "http://esbdesa.entel.cl:80/crm/crmloyalty/customerintermanag/crm_t_px_actualizargruposusuariosgaps";

    public java.lang.String getWS_actualizarGruposUsuarioSGAPortAddress() {
        return WS_actualizarGruposUsuarioSGAPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WS_actualizarGruposUsuarioSGAPortWSDDServiceName = "WS_actualizarGruposUsuarioSGAPort";

    public java.lang.String getWS_actualizarGruposUsuarioSGAPortWSDDServiceName() {
        return WS_actualizarGruposUsuarioSGAPortWSDDServiceName;
    }

    public void setWS_actualizarGruposUsuarioSGAPortWSDDServiceName(java.lang.String name) {
        WS_actualizarGruposUsuarioSGAPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGAPortType getWS_actualizarGruposUsuarioSGAPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WS_actualizarGruposUsuarioSGAPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWS_actualizarGruposUsuarioSGAPort(endpoint);
    }

    public entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGAPortType getWS_actualizarGruposUsuarioSGAPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGABindingStub _stub = new entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGABindingStub(portAddress, this);
            _stub.setPortName(getWS_actualizarGruposUsuarioSGAPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWS_actualizarGruposUsuarioSGAPortEndpointAddress(java.lang.String address) {
        WS_actualizarGruposUsuarioSGAPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGAPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGABindingStub _stub = new entel.oim.connectors.sga.webservices.actualizargruposusuario.WS_actualizarGruposUsuarioSGABindingStub(new java.net.URL(WS_actualizarGruposUsuarioSGAPort_address), this);
                _stub.setPortName(getWS_actualizarGruposUsuarioSGAPortWSDDServiceName());
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
        if ("WS_actualizarGruposUsuarioSGAPort".equals(inputPortName)) {
            return getWS_actualizarGruposUsuarioSGAPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/actualizarGruposUsuarioSGA/", "WS_actualizarGruposUsuarioSGA");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/actualizarGruposUsuarioSGA/", "WS_actualizarGruposUsuarioSGAPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WS_actualizarGruposUsuarioSGAPort".equals(portName)) {
            setWS_actualizarGruposUsuarioSGAPortEndpointAddress(address);
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
