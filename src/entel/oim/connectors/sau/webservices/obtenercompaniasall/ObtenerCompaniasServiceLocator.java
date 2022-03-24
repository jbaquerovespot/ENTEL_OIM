/**
 * ObtenerCompaniasServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package entel.oim.connectors.sau.webservices.obtenercompaniasall;

public class ObtenerCompaniasServiceLocator extends org.apache.axis.client.Service implements entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasService {

    public ObtenerCompaniasServiceLocator() {
    }


    public ObtenerCompaniasServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ObtenerCompaniasServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ObtenerCompaniasPort
    private java.lang.String ObtenerCompaniasPort_address = "http://esbdesa.entel.cl:80/enterprise/securityapp/accesousuarios/ent_t_px_obtenercompaniasallosbserviceps";

    public java.lang.String getObtenerCompaniasPortAddress() {
        return ObtenerCompaniasPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ObtenerCompaniasPortWSDDServiceName = "ObtenerCompaniasPort";

    public java.lang.String getObtenerCompaniasPortWSDDServiceName() {
        return ObtenerCompaniasPortWSDDServiceName;
    }

    public void setObtenerCompaniasPortWSDDServiceName(java.lang.String name) {
        ObtenerCompaniasPortWSDDServiceName = name;
    }

    public entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasPortType getObtenerCompaniasPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ObtenerCompaniasPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getObtenerCompaniasPort(endpoint);
    }

    public entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasPortType getObtenerCompaniasPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasBindingStub _stub = new entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasBindingStub(portAddress, this);
            _stub.setPortName(getObtenerCompaniasPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setObtenerCompaniasPortEndpointAddress(java.lang.String address) {
        ObtenerCompaniasPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasBindingStub _stub = new entel.oim.connectors.sau.webservices.obtenercompaniasall.ObtenerCompaniasBindingStub(new java.net.URL(ObtenerCompaniasPort_address), this);
                _stub.setPortName(getObtenerCompaniasPortWSDDServiceName());
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
        if ("ObtenerCompaniasPort".equals(inputPortName)) {
            return getObtenerCompaniasPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerCompaniasAllOSBService", "ObtenerCompaniasService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.esa.com/Enterprise/SecurityApp/T/obtenerCompaniasAllOSBService", "ObtenerCompaniasPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ObtenerCompaniasPort".equals(portName)) {
            setObtenerCompaniasPortEndpointAddress(address);
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
