package Intelligent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import BuildingBlocks.Process;

import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.NodeList;
import norsys.netica.Streamer;

public class Utils {
	
	private static final String IDNUM_Header = "IDnum";
	public static final String BELIEF_NET_FILENAME = "SimulatedDAG.dne";
	public static final String CASE_FILENAME = "ProcessSnapshots.cas";
	public static final String LEARNED_BELIEF_NET_FILENAME = "Learned_SimulatedDAG.dne";
	
	public static final String STATE_RUNNING = "R";
	public static final String STATE_NOT_RUNNING= "N";
	
	public void writeHeaderLineToFile()
	{
		 System.out.println( "" );
		 Environ env = null;
		    try {
				 env = new Environ (null);
			} catch (NeticaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		    try {	
			Net      net      = new Net (new Streamer (System.getProperty("user.dir")+"\\data\\"+BELIEF_NET_FILENAME));
			NodeList nodes    = net.getNodes();

			StringBuffer strBuf = new StringBuffer();
			strBuf.append(IDNUM_Header);
			
			
			for (int n = 0;  n < nodes.size();  n++) {
			    Node node = (Node) nodes.get (n);
			    strBuf.append("\t" + node.getName());
			}
			
			writeStringToFile(strBuf.toString());
	
			net.finalize();  
			env.finalize();
		    }
		    catch (Exception e) {
			e.printStackTrace();
		    }
	}
	
	public static void writeEvidenceToFile(Process[] list, int clockTick)
	{
		
		String str = createEntry(list, clockTick);
		
		writeStringToFile(str);
	
	}
	
	private static String createEntry(Process[] list, int  clockTick)
	{
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("\n"+clockTick);
	
		for(Process p : list)
		{
			String status =  p.getStatus() == Process.RUNNING ? Utils.STATE_RUNNING : Utils.STATE_NOT_RUNNING;
			strBuf.append("\t" + status);
		}
		
		return strBuf.toString();
	}
	
	private static void writeStringToFile(String str)
	{
		Writer output = null;
		File file = new File(System.getProperty("user.dir")+"\\data\\"+CASE_FILENAME);
		try {
			output = new BufferedWriter(new FileWriter(file, true));
			output.write(str);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String getNodeNameFromIndex(int i)
	{
		return "Node"+i;
	}

}
