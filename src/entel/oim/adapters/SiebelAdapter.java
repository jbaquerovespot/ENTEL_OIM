package entel.oim.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.configservice.api.Constants;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.Platform;
import oracle.iam.provisioning.api.ProvisioningService;

public class SiebelAdapter {

	private final static String className = SiebelAdapter.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private final tcLookupOperationsIntf lookupOprInf = Platform.getService(tcLookupOperationsIntf.class);

	public String toGetJobPositionSiebel(String cargo, String division, String lookupName) {

		logger.entering(className, "toGetJobPositionSiebel", cargo + "-" + division + " || " + lookupName);
		// Return variable
		// String jobPositionSiebel = "SiebelITResource~AGENTE RECLAMOS BACK";
		String jobPositionSiebel = "";
		String cargoSiebel = "";
		String divisionSiebel = "";

		if (cargo != null && division != null && lookupName != null && !cargo.isEmpty() && !division.isEmpty()
				&& !lookupName.isEmpty()) {
			try {
				logger.finer("Getting Cargos Siebel");
				tcResultSet cargoSet = lookupOprInf.getLookupValues("Lookup.Siebel.Dictonary.Cargo");

				for (int i = 0; i < cargoSet.getRowCount(); i++) {
					cargoSet.goToRow(i);

					String codeCargo = cargoSet.getStringValue(Constants.TableColumns.LKV_ENCODED.toString());

					if (codeCargo != null && !codeCargo.isEmpty()) {
						if (codeCargo.equalsIgnoreCase(cargo)) {
							cargoSiebel = cargoSet.getStringValue(Constants.TableColumns.LKV_DECODED.toString());
							logger.finer("Cargo Siebel found: " + cargoSiebel);
							break;
						}
					}

				}

				if (!cargoSiebel.isEmpty()) {

					logger.finer("Getting Division Siebel");
					tcResultSet divisionSet = lookupOprInf.getLookupValues("Lookup.Siebel.Dictonary.DivisionTiendaPDV");

					for (int i = 0; i < divisionSet.getRowCount(); i++) {
						divisionSet.goToRow(i);

						String codeDivision = divisionSet.getStringValue(Constants.TableColumns.LKV_ENCODED.toString());

						if (codeDivision != null && !codeDivision.isEmpty()) {
							if (codeDivision.equalsIgnoreCase(division)) {
								divisionSiebel = divisionSet
										.getStringValue(Constants.TableColumns.LKV_DECODED.toString());
								logger.finer("Division Siebel found: " + divisionSiebel);
								break;
							}
						}

					}

					if (divisionSiebel.isEmpty()) {

						logger.info("Division Siebel not found.");

					} else {

						tcResultSet lookupSiebel = lookupOprInf.getLookupValues(lookupName);

						for (int i = 0; i < lookupSiebel.getRowCount(); i++) {
							lookupSiebel.goToRow(i);

							String decode = lookupSiebel.getStringValue(Constants.TableColumns.LKV_DECODED.toString());
							if (decode != null && !decode.isEmpty()) {
								if (decode.contains(cargoSiebel) && decode.contains(divisionSiebel)) {
									jobPositionSiebel = lookupSiebel
											.getStringValue(Constants.TableColumns.LKV_ENCODED.toString());
									logger.finer("Siebel Primary Position found: " + decode);
									break;
								}
							}
						}
					}

				} else {
					logger.info("Cargo Siebel not found.");
				}

			} catch (tcAPIException | tcInvalidLookupException | tcColumnNotFoundException e) {
				e.printStackTrace();
			}
		}
		logger.exiting(className, "toGetJobPositionSiebel", jobPositionSiebel);

		return jobPositionSiebel;

	}

}
