/**
 * WS_listarPlataformasSGALocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sga.webservices.listarplataformas;

public class WS_listarPlataformasSGALocator extends org.apache.axis.client.Service implements entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGA {

    public WS_listarPlataformasSGALocator() {
    }


    public WS_listarPlataformasSGALocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WS_listarPlataformasSGALocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WS_listarPlataformasSGAPort
    private java.lang.String WS_listarPlataformasSGAPort_address = "http://esbdesa.entel.cl:80/crm/crmloyalty/customerintermanag/crm_t_px_listarplataformassgaps";

    public java.lang.String getWS_listarPlataformasSGAPortAddress() {
        return WS_listarPlataformasSGAPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WS_listarPlataformasSGAPortWSDDServiceName = "WS_listarPlataformasSGAPort";

    public java.lang.String getWS_listarPlataformasSGAPortWSDDServiceName() {
        return WS_listarPlataformasSGAPortWSDDServiceName;
    }

    public void setWS_listarPlataformasSGAPortWSDDServiceName(java.lang.String name) {
        WS_listarPlataformasSGAPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGAPortType getWS_listarPlataformasSGAPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WS_listarPlataformasSGAPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWS_listarPlataformasSGAPort(endpoint);
    }

    public entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGAPortType getWS_listarPlataformasSGAPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGABindingStub _stub = new entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGABindingStub(portAddress, this);
            _stub.setPortName(getWS_listarPlataformasSGAPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWS_listarPlataformasSGAPortEndpointAddress(java.lang.String address) {
        WS_listarPlataformasSGAPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGAPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGABindingStub _stub = new entel.oim.connectors.sga.webservices.listarplataformas.WS_listarPlataformasSGABindingStub(new java.net.URL(WS_listarPlataformasSGAPort_address), this);
                _stub.setPortName(getWS_listarPlataformasSGAPortWSDDServiceName());
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
        if ("WS_listarPlataformasSGAPort".equals(inputPortName)) {
            return getWS_listarPlataformasSGAPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarPlataformasSGA/", "WS_listarPlataformasSGA");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/CRM/CRMLoyalty/T/listarPlataformasSGA/", "WS_listarPlataformasSGAPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WS_listarPlataformasSGAPort".equals(portName)) {
            setWS_listarPlataformasSGAPortEndpointAddress(address);
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
