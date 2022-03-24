/**
 * WS_listarGruposSGALocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.listargrupos;

public class WS_listarGruposSGALocator extends org.apache.axis.client.Service implements entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGA {

    public WS_listarGruposSGALocator() {
    }


    public WS_listarGruposSGALocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WS_listarGruposSGALocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WS_listarGruposSGAPort
    private java.lang.String WS_listarGruposSGAPort_address = "http://esbdesa.entel.cl:80/crm/crmloyalty/customerintermanag/crm_t_px_listargrupossgaps";

    public java.lang.String getWS_listarGruposSGAPortAddress() {
        return WS_listarGruposSGAPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WS_listarGruposSGAPortWSDDServiceName = "WS_listarGruposSGAPort";

    public java.lang.String getWS_listarGruposSGAPortWSDDServiceName() {
        return WS_listarGruposSGAPortWSDDServiceName;
    }

    public void setWS_listarGruposSGAPortWSDDServiceName(java.lang.String name) {
        WS_listarGruposSGAPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGAPortType getWS_listarGruposSGAPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WS_listarGruposSGAPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWS_listarGruposSGAPort(endpoint);
    }

    public entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGAPortType getWS_listarGruposSGAPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGABindingStub _stub = new entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGABindingStub(portAddress, this);
            _stub.setPortName(getWS_listarGruposSGAPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWS_listarGruposSGAPortEndpointAddress(java.lang.String address) {
        WS_listarGruposSGAPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGAPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGABindingStub _stub = new entel.oim.connectors.sga.webservices.listargrupos.WS_listarGruposSGABindingStub(new java.net.URL(WS_listarGruposSGAPort_address), this);
                _stub.setPortName(getWS_listarGruposSGAPortWSDDServiceName());
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
        if ("WS_listarGruposSGAPort".equals(inputPortName)) {
            return getWS_listarGruposSGAPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarGruposSGA/", "WS_listarGruposSGA");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarGruposSGA/", "WS_listarGruposSGAPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WS_listarGruposSGAPort".equals(portName)) {
            setWS_listarGruposSGAPortEndpointAddress(address);
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
