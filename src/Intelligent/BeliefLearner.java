package Intelligent;

import BuildingBlocks.ISystemAPI;
import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.NodeList;
import norsys.netica.Streamer;

public class BeliefLearner {
	private static final String TAG = "BeliefLearner:";

	
	
	public static void learn() {
	    System.out.println( TAG + "Learning Started" );
	    Environ env = null;
	    try {
			env = new Environ (null);
		} catch (NeticaException e1) {

			e1.printStackTrace();
		}

	    try {	
		Net      net      = new Net (new Streamer (System.getProperty("user.dir")+"\\data\\" + Utils.BELIEF_NET_FILENAME));
		NodeList nodes    = net.getNodes();
		int      numNodes = nodes.size();

		for (int n = 0;  n < numNodes;  n++) {
		    Node node = (Node) nodes.get (n);
		    //node.deleteTables();
		}
		


		Streamer caseFile = new Streamer (System.getProperty("user.dir")+"\\data\\"+Utils.CASE_FILENAME);
		net.reviseCPTsByCaseFile (caseFile, nodes, 1.0);

		net.write (new Streamer (System.getProperty("user.dir")+"\\data\\"+Utils.LEARNED_BELIEF_NET_FILENAME));
		
		net.finalize();   // not strictly necessary, but a good habit
		env.finalize();
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	    System.out.println( TAG + "Learning Complete" );
	  }
}
