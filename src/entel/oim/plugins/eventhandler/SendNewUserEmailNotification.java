package entel.oim.plugins.eventhandler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.kernel.spi.PostProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;

/**
 * Contains generics adapters share by all connectors
 * 
 * @author Oracle
 *
 */

public class SendNewUserEmailNotification implements PostProcessHandler {

	private final static String className = SendNewUserEmailNotification.class.getName();
	private static Logger logger = Logger.getLogger(className);

	@Override
	public void initialize(HashMap<String, String> parameters) {}

	@Override
	public void compensate(long processId, long eventId, AbstractGenericOrchestration orchestration) {}

	@Override
	public boolean cancel(long processId, long eventId, AbstractGenericOrchestration orchestration) {
		return false;
	}

	@Override
	public EventResult execute(long processId, long eventId, Orchestration orchestration) {
	
		System.out.println("EVENT HANDLER.");

		
		//logger.entering(className, "EventResult");
		
		/*logger.info(String.format("Start execute() with ProcessId: %s and EventId %s", processId, eventId));
		System.out.println(String.format("Start execute() with ProcessId: %s and EventId %s", processId, eventId));
		*/
		HashMap<String, Serializable> parameters = orchestration.getParameters(); // contains only the new values
		HashMap<String, Serializable> interParameters = orchestration.getInterEventData(); // contains old and new values of user
		/*
		logger.info(String.format("Inter Parameters: %s ", interParameters));
		logger.info(String.format("New Parameters: %s ", parameters));
		
		System.out.println(String.format("Inter Parameters: %s ", interParameters));
		System.out.println(String.format("New Parameters: %s ", parameters));
		*/
		/*
		 * Operation is create user?
		 */	
		if (orchestration.getOperation().equals("CREATE")) {

			//logger.finer("Operation: CREATE");
			System.out.println("Operation: CREATE");
			
			String userKey = orchestration.getTarget().getEntityId();
			String middleName = getParamaterValue(parameters, "Middle Name");

			/*
			logger.info("userKey: " + userKey);
			logger.info("middleName: " + middleName);
			*/
			System.out.println("userKey: " + userKey + "middleName: " + middleName); 
			
		} else {

			//logger.finer("Operation: NOT CREATE - " + orchestration.getOperation());
			System.out.println("Operation: NOT CREATE - " + orchestration.getOperation());
					
		}

		//logger.exiting(className, "EventResult");
				
		System.out.println("EVENT HANDLER ENDED.");
				
		return new EventResult();
	}
	
	@Override
	public BulkEventResult execute(long processId, long eventId, BulkOrchestration orchestration) {

		logger.entering(className, "BulkEventResult");
		logger.exiting(className, "BulkEventResult");

		return new BulkEventResult();
	}

	private String getParamaterValue(HashMap<String, Serializable> parameters, String key) {
		if (parameters.containsKey(key)) {
			String value = (parameters.get(key) instanceof ContextAware)
					? (String) ((ContextAware) parameters.get(key)).getObjectValue()
					: (String) parameters.get(key);
			return value;
		} else {
			return null;
		}
	}

}
