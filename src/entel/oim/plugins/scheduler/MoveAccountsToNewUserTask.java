package entel.oim.plugins.scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE;
import oracle.iam.scheduler.vo.TaskSupport;

public class MoveAccountsToNewUserTask extends TaskSupport {
	
	private final static String className = MoveAccountsToNewUserTask.class.getName();
	private static Logger logger = Logger.getLogger(className);

	private final UserManager umgr = Platform.getService(UserManager.class);
    private final ProvisioningService provsvr = Platform.getService(ProvisioningService.class);
    private final tcUserOperationsIntf userOperIntf = Platform.getService(tcUserOperationsIntf.class);;
	
    @Override
    public void execute(HashMap hm) throws Exception {
		
		logger.entering(className, "execute", hm);
	
		logger.finer("Loading parameters");
		String oiuKey = (String) hm.get("OIU Key");
		String newUserKey = (String) hm.get("USR Key");

		logger.fine("Getting details of the account to move");
		Account account = provsvr.getAccountDetails(Long.parseLong(oiuKey));
		
		logger.fine("Getting details of the account to move");
		Set<String> resAttrs = new HashSet<String>();
		User user = umgr.getDetails(newUserKey, resAttrs, false);
		
		
		if (account == null || user == null) {
			logger.severe("The user " +newUserKey+ " and the account "+oiuKey+" must exists");
		
		} else {
			
			if (account.getAccountType().getId().equalsIgnoreCase(ACCOUNT_TYPE.ServiceAccount.getId())) {
				logger.fine("It is a Service Account");
				logger.fine("Moving account to the new user:" +newUserKey);
				userOperIntf.moveServiceAccount(Long.parseLong(oiuKey), Long.parseLong(newUserKey));
				
			} else {
			
				logger.fine("Changing account to ServiceAccount temporaly");
				userOperIntf.changeToServiceAccount(Long.parseLong(oiuKey));
				
				logger.fine("Moving account to the new user:" +newUserKey);
				userOperIntf.moveServiceAccount(Long.parseLong(oiuKey), Long.parseLong(newUserKey));
			
				logger.fine("Restoring account type");
				userOperIntf.changeFromServiceAccount(Long.parseLong(oiuKey));
			}
		}
		
		logger.exiting(className, "execute");

	}
	
	
	@Override
    public HashMap getAttributes() {
		return null;
    }

    @Override
    public void setAttributes() {

    }
	
}
