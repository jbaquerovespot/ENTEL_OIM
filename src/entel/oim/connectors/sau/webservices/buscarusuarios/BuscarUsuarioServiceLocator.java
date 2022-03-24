/**
 * BuscarUsuarioServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.buscarusuarios;

public class BuscarUsuarioServiceLocator extends org.apache.axis.client.Service implements entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioService {

    public BuscarUsuarioServiceLocator() {
    }


    public BuscarUsuarioServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BuscarUsuarioServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BuscarUsuarioPort
    private java.lang.String BuscarUsuarioPort_address = "http://esbdesa.entel.cl:80/enterprise/securityapp/accesousuarios/ent_t_px_buscarusuariososbsauserviceps";

    public java.lang.String getBuscarUsuarioPortAddress() {
        return BuscarUsuarioPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BuscarUsuarioPortWSDDServiceName = "BuscarUsuarioPort";

    public java.lang.String getBuscarUsuarioPortWSDDServiceName() {
        return BuscarUsuarioPortWSDDServiceName;
    }

    public void setBuscarUsuarioPortWSDDServiceName(java.lang.String name) {
        BuscarUsuarioPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioPorType getBuscarUsuarioPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BuscarUsuarioPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBuscarUsuarioPort(endpoint);
    }

    public entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioPorType getBuscarUsuarioPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioBindingStub _stub = new entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioBindingStub(portAddress, this);
            _stub.setPortName(getBuscarUsuarioPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBuscarUsuarioPortEndpointAddress(java.lang.String address) {
        BuscarUsuarioPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioPorType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioBindingStub _stub = new entel.oim.connectors.sau.webservices.buscarusuarios.BuscarUsuarioBindingStub(new java.net.URL(BuscarUsuarioPort_address), this);
                _stub.setPortName(getBuscarUsuarioPortWSDDServiceName());
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
        if ("BuscarUsuarioPort".equals(inputPortName)) {
            return getBuscarUsuarioPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService", "BuscarUsuarioService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/buscarUsuariosOSBSAUService", "BuscarUsuarioPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BuscarUsuarioPort".equals(portName)) {
            setBuscarUsuarioPortEndpointAddress(address);
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
