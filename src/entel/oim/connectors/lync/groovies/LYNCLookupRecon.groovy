package entel.oim.connectors.lync.groovies;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.*;


// Initiation
trace.info("[Lookup Lync] Lookup Recon timing:"+ timing);
trace.info("[Lookup Lync] Attributes to Get:"+ ATTRS_TO_GET);


if (timing.equals("executeQuery:REGISTRARPOOL")) {
	
	trace.info("[Lookup Lync] Getting usertypes.");
    String codekey =  ATTRS_TO_GET[0];
	String decodekey = ATTRS_TO_GET[0];

		
  util.addAttribute(codekey,"poollync2013.Entel.Entelcorp.com");
  util.addAttribute(decodekey,"poollync2013.Entel.Entelcorp.com");
  if (!util.build()) return;
  
    
  trace.info("[Lookup Lync] REGISTRARPOOL were loaded sucessfully.");
    
}


	
 