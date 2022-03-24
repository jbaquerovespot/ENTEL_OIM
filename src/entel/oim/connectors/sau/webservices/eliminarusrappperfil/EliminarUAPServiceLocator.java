/**
 * EliminarUAPServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.eliminarusrappperfil;

public class EliminarUAPServiceLocator extends org.apache.axis.client.Service implements entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPService {

    public EliminarUAPServiceLocator() {
    }


    public EliminarUAPServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EliminarUAPServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EliminarUAPPort
    private java.lang.String EliminarUAPPort_address = "http://esbdesa.entel.cl:80/enterprise/securityapp/accesousuarios/ent_t_px_eliminarusrappperfilosbserviceps";

    public java.lang.String getEliminarUAPPortAddress() {
        return EliminarUAPPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EliminarUAPPortWSDDServiceName = "EliminarUAPPort";

    public java.lang.String getEliminarUAPPortWSDDServiceName() {
        return EliminarUAPPortWSDDServiceName;
    }

    public void setEliminarUAPPortWSDDServiceName(java.lang.String name) {
        EliminarUAPPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPPortType getEliminarUAPPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EliminarUAPPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEliminarUAPPort(endpoint);
    }

    public entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPPortType getEliminarUAPPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPBindingStub _stub = new entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPBindingStub(portAddress, this);
            _stub.setPortName(getEliminarUAPPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEliminarUAPPortEndpointAddress(java.lang.String address) {
        EliminarUAPPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPBindingStub _stub = new entel.oim.connectors.sau.webservices.eliminarusrappperfil.EliminarUAPBindingStub(new java.net.URL(EliminarUAPPort_address), this);
                _stub.setPortName(getEliminarUAPPortWSDDServiceName());
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
        if ("EliminarUAPPort".equals(inputPortName)) {
            return getEliminarUAPPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/eliminarUsrAppPerfilOSBService", "EliminarUAPService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/eliminarUsrAppPerfilOSBService", "EliminarUAPPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EliminarUAPPort".equals(portName)) {
            setEliminarUAPPortEndpointAddress(address);
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
