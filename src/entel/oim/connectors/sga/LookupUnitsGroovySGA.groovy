package entel.oim.connectors.sga


import java.util.List;

import entel.oim.connectors.sga.webservices.WS_listarPlataformasSGAStub.DetallePlataformasType
import entel.oim.connectors.sga.webservices.WS_listarPlataformasSGAStub.ListarPlataformasSGAResponseType


class LookupUnitsGroovySGA {

	public LookupUnitsGroovy() {
		// TODO Auto-generated constructor stub
		
	}
	
	static void main(String[] args) {
		
		
	
		entel.oim.connectors.sga.webservices.WS_listarPlataformasSGAStub.ListarPlataformasSGAResponseType response = entel.oim.connectors.sga.OperationsSGA.lookupPlatforms(
			"http",
			"esbdesa.entel.cl",
			"80",
			"crm/crmloyalty/customerintermanag/crm_t_px_listarplataformassgaps?wsdl",
			"uproiam6456",
			"entel123");
	
		System.out.println("Error code: " + response.getCodigo());
		System.out.println("Error description: " + response.getDescripcion());
		
			DetallePlataformasType[] detPlatformArray = response.getPlataformas().getListadoPlataformas();
		
				for (int i=0; i < detPlatformArray.length ; i++) {
					
					DetallePlataformasType plat = detPlatformArray[i];
					System.out.println(plat.getNombrePlataforma());
					
					
				}
		
			
	
	
				
		/*List<String> unitList= bch.oim.groovy.utilities.Cibergestion.getUnits("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"OIMUSER1",
			"BANCO2017",
			"7_1_23"
			);
			for (String item : unitList) {
				System.out.println(item)
		}
		*/
		
		/*
		List<String> profileList= bch.oim.groovy.CibergestionPrueba.Utilities.getProfiles("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"BERTY",
			"BANCO2017"
			);
			for (String item : profileList) {
				System.out.println(item)
		}
		*/
		
		/*
		
		
		List<String> profileList = [ "None" ]
		List<String> unitList = [ ]
		boolean exito = bch.oim.groovy.utilities.CibergestionPrueba.createUser("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"OIMUSER1",
			"BANCO2017",
			"OIMUSER2",
			"BANCO2017",
			"AADIAZ",
			"Ariel Alejandro Diaz Mella",
			"Human",
			"Password",
			null,
			profileList,
			unitList
			);
		
		*/
		
		/*
		boolean exito2 = bch.oim.groovy.CibergestionPrueba.Utilities.deleteUser("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"BERTY",
			"BANCO2017",
			"DFERREIRA");
		
		*/
			
		/*
		boolean exito2 = bch.oim.groovy.utilities.CibergestionPrueba.setStatusUser( "https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"OIMUSER1",
			"BANCO2017",
			"DFERREIRA2",
			"false");
		
		
		
		
		
		boolean exito2 = bch.oim.groovy.utilities.CibergestionPrueba.resetPasswordUser( "https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"OIMUSER1",
			"BANCO2017",
			"AAPINTO");
		
		
		
		
		
		boolean exito = bch.oim.groovy.CibergestionPrueba.Utilities.addRoleUser("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"BERTY",
			"BANCO2017",
			"SUSANA",
			"BANCO2017",
			"DFERREIRA",
			"BCHI_MESA_CONSULTA");
		
		
		boolean exito = bch.oim.groovy.CibergestionPrueba.Utilities.addUnitUser("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"BERTY",
			"BANCO2017",
			"SUSANA",
			"BANCO2017",
			"DFERREIRA",
			"LBTR");
		
		
		
		boolean exito = bch.oim.groovy.CibergestionPrueba.Utilities.removeUnitUser("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"BERTY",
			"BANCO2017",
			"SUSANA",
			"BANCO2017",
			"DFERREIRA",
			"CAMBIOS");
		
		
		boolean exito = bch.oim.groovy.CibergestionPrueba.Utilities.removeRoleUser("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"BERTY",
			"BANCO2017",
			"SUSANA",
			"BANCO2017",
			"DFERREIRA",
			"BCHI_MESA_CONSULTA");
		
		
		
		List<String[]> userList = bch.oim.groovy.utilities.CibergestionPrueba.getUsers("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"OIMUSER1",
			"BANCO2017",
			"OIMUSER2",
			"BANCO2017");
		
		for (String[] user : userList) {
				System.out.println(user[0]);
				System.out.println(user[1]);
				System.out.println(user[2]);
				System.out.println(user[3]);
				System.out.println(user[4]);
				System.out.println(user[5]);
				System.out.println(user[6]);
				System.out.println(user[7]);
				System.out.println(user[8]);
				System.out.println(user[9]);
				System.out.println("--------------");
				
		}
			
		
		
		
		String[] user = bch.oim.groovy.utilities.CibergestionPrueba.getUser("https",
			"152.139.146.43",
			"2443",
			"swp/group/saaqa",
			"OIMUSER1",
			"BANCO2017",
			"OIMUSER2",
			"BANCO2017",
			"YPARRAGUEZ");
		
			  System.out.println(user[0]);
				System.out.println(user[1]);
				System.out.println(user[2]);
				System.out.println(user[3]);
				System.out.println(user[4]);
				System.out.println(user[5]);
				System.out.println(user[6]);
				System.out.println(user[7]);
				System.out.println(user[8]);
				System.out.println(user[9]);
				System.out.println("--------------");
		
			
		
		
		
		
		boolean exito = bch.oim.groovy.utilities.Cibergestion.updateProfile(
		
			"http",
			"152.139.73.143",
			"8080",
			"",
			"ciber",
			"chile1\$",
			"36900", //UserId
			"chk_Contabilizacion",
			"TRUE");
		
		
		*/
		
			
	/*
	 *
	 *
	 * boolean exito = bch.oim.groovy.utilities.Cibergestion.updateProfiles(
		
			"http",
			"152.139.73.143",
			"8080",
			"",
			"ciber",
			"chile1\$",
			"36900", //UserId
			"52;148;3030",
			"TRUE");
			
		
		
		def branches = [:];
		
	branches = bch.oim.groovy.utilities.Cibergestion.getProfiles(
			"http",
			"152.139.73.143",
			"8080",
			"",
			"ciber",
			"Banco2017"); // item -> Uno  a Uno
		
		if (branches.size() == 0){
			println "Errorrrr"
		}
		
		branches.each{ codeId,codeDesc -> println "${codeId}:${codeDesc}"};
		
		
			*/
		
		 /*
		
		 boolean exito = bch.oim.groovy.utilities.Cibergestion.updateUser(
		 
				"http",
				"152.139.73.143",
				"8080",
				"",
				"ciber",
				"Banco2017",
				"36900", //UserId
				"null",
				"null",
				"null",
				"null",
				"null",
				"null",
				"null",
				"null",
				"null",
				"null",
				"null",
				"TRUE"); // Enabled
		
			println "${exito}"
			
		
		
		String id = bch.oim.groovy.utilities.Cibergestion.createUser(
			
				   "http",
				   "152.139.73.143",
				   "8080",
				   "",
				   "ciber",
				   "chile1\$",
				   "dferreira5",
				   "Daniel",
				   "Jesus",
				   "Ferreira",
				   "Andrade",
				   "25078127k",
				   "daniel.ferreira@oracle.com",
				   "Banco2017",
				   "null",
				   "null",
				   "null",
				   "null",
				   "TRUE"); // Enabled
		
		 println "${id}"
		 
		 
	
		
		bch.oim.groovy.utilities.Cibergestion.getUsers(
			
				   "http",
				   "152.139.73.143",
				   "8080",
				   "",
				   "ciber",
				   "Banco2017"); // Enabled
		 
		
			   
			bch.oim.groovy.utilities.Cibergestion2.getUser(
				   
						  "http",
						  "152.139.73.143",
						  "8080",
						  "",
						  "ciber",
						  "Banco2017",
						  "39609"); // Enabled
					  
					  bch.oim.groovy.utilities.Cibergestion2.getUsers(
						  
								 "http",
								 "152.139.73.143",
								 "8080",
								 "",
								 "ciber",
								 "Banco2017"); // Enabled
				 
		  
					  
					  
					  String id = bch.oim.groovy.utilities.Cibergestion.createUser(
						  
								 "http",
								 "152.139.73.143",
								 "8080",
								 "",
								 "ciber",
								 "Banco2017",
								 "dferreira7",
								 "Daniel",
								 "Jesus",
								 "Ferreira",
								 "Andrade",
								 "162811819",
								 "daniel.ferreira@oracle.com",
								 "Banco2017",
								 "null",
								 "null",
								 "null",
								 "null",
								 "TRUE"); // Enabled
					  
					   println "${id}"
   */
		}

}
