package entel.oim.plugins.eventhandler.remedy.ws;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * This class is for unit testing purposes only, to be able to call remedy Web
 * Service outside of OIM
 * 
 * @author Oracle
 *
 */
public class SimpleSoapCallUnitTest {

	public static void main(String[] args) throws UnsupportedOperationException, SOAPException {
		String message = "";
		String rut = "11117836-1";
		String silla = "11117836";
		String nombreYApellidos = "RUTH VARAS JOFRE";
		String cargo = "ANALISTA SUPPLY CHAIN";
		String estamento = "Profesional";
		String empresaContratante = "ENTEL";
		String lugarDeTrabajo = "Andres Bello 2711";
		String fechaDeIngreso = "03-07-2018";
		String nombresYApellidosJefatura = "Vasquez Piña Jorge Fernando";
		String correoElectronico = "jpvasquez@entel.cl";
		String telefono = "+569999999999";
		String gerencia = "VP de Tecnología";
		String centroDeCosto = "20280";
		String type = "anexo";
		RemedyVO remedyVO = new RemedyVO();
		remedyVO.setRut(rut);
		remedyVO.setSilla(silla);
		remedyVO.setNombreYApellidos(nombreYApellidos);
		remedyVO.setCargo(cargo);
		remedyVO.setEstamento(estamento);
		remedyVO.setEmpresaContratante(empresaContratante);
		remedyVO.setLugarDeTrabajo(lugarDeTrabajo);
		remedyVO.setFechaDeIngreso(fechaDeIngreso);
		remedyVO.setNombresYApellidosJefatura(nombresYApellidosJefatura);
		remedyVO.setCorreoElectronico(correoElectronico);
		remedyVO.setTelefono(telefono);
		remedyVO.setGerencia(gerencia);
		remedyVO.setCentroDeCosto(centroDeCosto);
		remedyVO.setType(type);
		Properties props = new Properties();
		RemedyUtil remedyUtil = new RemedyUtil(props);
		SoapEnvelope envelope = new SoapEnvelope(remedyVO, remedyUtil);

		try {
			JAXBContext jaxbCtx = JAXBContext.newInstance(InputMapping1.class, ObjectFactory.class, SoapEnvelope.class);
			javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
			marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			StringWriter writer = new StringWriter();

			marshaller.marshal(envelope, writer);
			message = writer.toString();
			System.out.println(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		SOAPConnection conn = null;

		try {
			conn = SOAPConnectionFactory.newInstance().createConnection();
		} catch (UnsupportedOperationException | SOAPException e) {
			e.printStackTrace();
		}

		try {
			InputStream Soapmessage = new ByteArrayInputStream(message.getBytes("UTF-8"));
			SOAPMessage msg = MessageFactory.newInstance().createMessage(null, Soapmessage);
			SOAPMessage resp = conn.call(msg, remedyUtil.loadUrl());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			resp.writeTo(out);
			String strMsg = new String(out.toByteArray());
			System.out.println(strMsg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
