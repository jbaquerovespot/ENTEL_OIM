package entel.oim.plugins.eventhandler.remedy.ws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;


public class RemedyUtil {
	private static  Properties props = new Properties();
    
    private boolean debug=false;
	public RemedyUtil(Properties props) {
		try {
			if (debug)
			{
			String filename = System.getenv("Entel.PropertiesFileLoc");
			FileInputStream fileInputStream = new FileInputStream(filename);

			props.load(new InputStreamReader(fileInputStream, Charset.forName("UTF-8")));
			}
			{
				this.props=props;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  String loadNotes(RemedyVO remedyVO) {
		String notes = "";
		notes = props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas1") + "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas2") + "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas3") + remedyVO.getRut()+ "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas4") + remedyVO.getSilla()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas26") + remedyVO.getRut()+ "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas5") + remedyVO.getNombreYApellidos()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas6") + remedyVO.getEmpresaContratante()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas7") +remedyVO.getCargo()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas8") + remedyVO.getEstamento()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas9") + remedyVO.getLugarDeTrabajo()+ "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas10") + remedyVO.getFechaDeIngreso()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas12") + "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas13") + remedyVO.getNombresYApellidosJefatura()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas14") + remedyVO.getCorreoElectronico()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas15") + remedyVO.getTelefono()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas16") + remedyVO.getGerencia()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas17") + remedyVO.getCentroDeCosto()+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas18") + "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas19") + "\n";
		String strPuesto="SI";
		String strAnexo="SI";
		String strEquipo="SI";
		String strVendomatica="SI";
		/*if (remedyVO.getCargo().indexOf("EJECUTIVO ATENCION COMERCIAL") >= 0 ||
				remedyVO.getCargo().indexOf("CAJERO COMERCIAL") >= 0 ||
						remedyVO.getCargo().indexOf("ANFITRION") >= 0 
						//|| 						remedyVO.getState().indexof("Metropolitana")< 0
						)
		{
			strPuesto="NO";
			strAnexo="NO";
			strEquipo="NO";
			strVendomatica="NO";
		}
		if (remedyVO.getCargo().indexOf("PRACTICANTE") >= 0)
		{
			strAnexo="NO";
			
		}
		*/
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas20") + strPuesto+"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas21") + strVendomatica+"\n";
		String torniqueteCostanera="";
		if((remedyVO.getLugarDeTrabajo().indexOf("2711") >= 0) && (  remedyVO.getLugarDeTrabajo().indexOf("Andres") >= 0 ||  remedyVO.getLugarDeTrabajo().indexOf("Andrés") >= 0)&& (remedyVO.getLugarDeTrabajo().indexOf("Bello") >= 0 ))
		{
			torniqueteCostanera="SI";	
		}
		else
		{
			torniqueteCostanera="NO";
		}
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas22") + torniqueteCostanera+ "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas23") + strAnexo +"\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas24") + "\n";
		notes = notes + props.getProperty("entel.oim.plugins.eventhandler.remedy."+remedyVO.getType()+".Notas25");
		return notes;
	}

	public String loadSubmitter(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".submitter");
	}

	public String loadSummary(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".Resumen");
	}

	public String loadTipoOrdenDeTrabajo(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".TipoDeOrdenDeTrabajo");
	}

	public String loadSupportOrganization(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".SupportOrganization");
	}

	public String loadUrl() {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy.url");
	}

	public String loadPriority(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".Prioridad");
	}

	public String loadUsername() {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy.username");
	}
	public String loadFirstName() {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy.FirstName");
	}
	public String loadLastName() {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy.LastName");
	}
	public String loadCustomerCompany() {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy.CustomerCompany");
	}
	public String loadPassword() {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy.password");
	}

	public String loadEmpresa(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".Empresa");
	}

	public String loadAction(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".z1D_Action");
	}

	public String loadEmpresaSupportOrganization(String service) {
			return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".EmpresaSupportOrganization");
	}

	public String loadNombreGrupoSoporte(String service) {
			return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".NombreDelGrupoSoporte");
	}

	public String loadFuenteReportada(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".FuenteReportada");
	}

	public String loadTipoRequerimiento(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".TipoRequerimiento");
	}

	public String loadUAPManagerGrupoSoporteEmpresa(String service) {
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".UsuarioAsignadoPeticion_ManagerGrupoSoporteEmpresa");
	}

	public String loadUAPManagerGrupoSoporte(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".UsuarioAsignadoPeticion_ManagerGrupoSoporte");
	}

	public String loadUAMNombreGrupoSoporte(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".UsuarioAsignadoPeticion_NombreGrupoSoporte");
	}

	public String loadCatOperacionalNivel1(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".categorizacionoperacional.Nivel1");
	}
	
	public String loadCatOperacionalNivel2(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".categorizacionoperacional.Nivel2");
	}
	public String loadCatOperacionalNivel3(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".categorizacionoperacional.Nivel3");
	}
	
	public String loadCatProductoNivel1(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".categorizacionproducto.Nivel1");
	}
	
	public String loadCatProductoNivel2(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".categorizacionproducto.Nivel2");
	}
	
	public String loadCatProductoNivel3(String service) {
		// TODO Auto-generated method stub
		return props.getProperty("entel.oim.plugins.eventhandler.remedy."+service+".categorizacionproducto.Nivel3");
	}
	public String loadXmlUnitTest(RemedyVO remedyVO) {
		// Simple soap connection
		String SoapHeader = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:CreacionWO\">\r\n" + 
				"   <soapenv:Header>\r\n" + 
				"      <urn:AuthenticationInfo>\r\n" + 
				"         <urn:userName>"+loadUsername()+"</urn:userName>\r\n" + 
				"         <urn:password>"+loadPassword()+"</urn:password>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:authentication>?</urn:authentication>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:locale>?</urn:locale>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:timeZone>?</urn:timeZone>\r\n" + 
				"      </urn:AuthenticationInfo>\r\n" + 
				"   </soapenv:Header>" ;
		
		String SoapBody = "<soapenv:Body>\r\n" + 
				"      <urn:CreacionWO>\r\n" + 
				"         <urn:Submitter>"+loadSubmitter(remedyVO.getType())+"</urn:Submitter>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Assigned_To></urn:Assigned_To>\r\n" + 
				"         <urn:Status></urn:Status>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Summary>"+loadSummary(remedyVO.getType())+"</urn:Summary>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				
				
"         <urn:Categorization_Tier_1>"+loadCatOperacionalNivel1(remedyVO.getType())+"</urn:Categorization_Tier_1>\r\n" + 
"         <!--Optional:-->\r\n" + 
"         <urn:Categorization_Tier_2>"+loadCatOperacionalNivel2(remedyVO.getType())+"</urn:Categorization_Tier_2>\r\n" + 
"         <!--Optional:-->\r\n" + 
"         <urn:Categorization_Tier_3>"+loadCatOperacionalNivel3(remedyVO.getType())+"</urn:Categorization_Tier_3>\r\n" + 
				"         <urn:Detailed_Description>"+loadNotes(remedyVO)+"</urn:Detailed_Description>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Priority>"+loadPriority(remedyVO.getType())+"</urn:Priority>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Work_Order_Type>"+loadTipoOrdenDeTrabajo(remedyVO.getType())+"</urn:Work_Order_Type>\r\n" + 
				"         <!--Optional:-->\r\n" + 
			
"         <urn:Product_Cat_Tier_1_2_>"+loadCatProductoNivel1(remedyVO.getType())+"</urn:Product_Cat_Tier_1_2_>\r\n" + 
"         <!--Optional:-->\r\n" + 
"         <urn:Product_Cat_Tier_2__2_>"+loadCatProductoNivel2(remedyVO.getType())+"</urn:Product_Cat_Tier_2__2_>\r\n" + 
"         <!--Optional:-->\r\n" + 
"         <urn:Product_Cat_Tier_3__2_>"+loadCatProductoNivel3(remedyVO.getType())+"</urn:Product_Cat_Tier_3__2_>\r\n" +
			 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Product_Name__2_></urn:Product_Name__2_>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Product_Model_Version__2_></urn:Product_Model_Version__2_>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Support_Organization>"+loadUAPManagerGrupoSoporte(remedyVO.getType())+"</urn:Support_Organization>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Support_Company>"+loadUAPManagerGrupoSoporteEmpresa(remedyVO.getType())+"</urn:Support_Company>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Support_Group_Name>"+loadUAMNombreGrupoSoporte(remedyVO.getType())+"</urn:Support_Group_Name>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Customer_First_Name>"+loadFirstName()+"</urn:Customer_First_Name>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Customer_Last_Name>"+loadLastName()+"</urn:Customer_Last_Name>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Customer_Company>"+loadCustomerCompany()+"</urn:Customer_Company>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:Fuente_Reportada>"+loadFuenteReportada(remedyVO.getType())+"</urn:Fuente_Reportada>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:TipoRequerimiento>"+loadTipoRequerimiento(remedyVO.getType())+"</urn:TipoRequerimiento>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:RequestManagerCompany>"+loadEmpresaSupportOrganization(remedyVO.getType())+"</urn:RequestManagerCompany>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:ManagerSupportOrganization>"+loadSupportOrganization(remedyVO.getType())+"</urn:ManagerSupportOrganization>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:ManagerSupportGroupName>"+loadNombreGrupoSoporte(remedyVO.getType())+"</urn:ManagerSupportGroupName>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:z1D_Action>"+loadAction(remedyVO.getType())+"</urn:z1D_Action>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:LocationCompany>"+loadEmpresa(remedyVO.getType())+"</urn:LocationCompany>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <urn:AddRequestFor></urn:AddRequestFor>\r\n" + 
				"	</urn:CreacionWO>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		String message = SoapHeader + SoapBody;
  return message;
		
	}
}
