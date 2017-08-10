package Intelligent;

import java.util.Arrays;
import java.util.Map;

import BuildingBlocks.ISystemAPI;
import BuildingBlocks.Locker;
import BuildingBlocks.Process;
import norsys.netica.*;

public class BeliefNetInference {

	private static final String TAG = "BeliefNetInference:";
	ISystemAPI mSystem;

	BeliefNetInference(ISystemAPI sys) {
		mSystem = sys;
	}

	static String getBeliefFilePath()
	{
		return System.getProperty("user.dir")+"\\data\\"+ Utils.LEARNED_BELIEF_NET_FILENAME;
	}
	
	
	public int getProcessToKill() {
		
		Process currentProcess = mSystem.getCurrentUserProcess();
		double []pDistribution ;
		Locker locker = Locker.getLocker();
		synchronized(locker) {
		pDistribution = getProbabilityDistribution(currentProcess.getPID());
		}
		
		int pid  =  getHighestRewardProcessId(pDistribution);
		
		return pid;
		
	}
	
	
	private double[] getProbabilityDistribution(int CurrentNodeIndex)
	{
		double []pDistribution =  null;;
		try {
			Environ env = new Environ(null);
			

			//Read the learned beliefs 
			Net net = new Net(new Streamer(System.getProperty("user.dir")+"\\data\\"+ Utils.LEARNED_BELIEF_NET_FILENAME));
			net.compile();
			
	
			Node curNode = net.getNode(Utils.getNodeNameFromIndex(CurrentNodeIndex));
			curNode.finding().enterState(Utils.STATE_RUNNING);
			
			NodeList nodes    = net.getNodes();
			int      numNodes = nodes.size();
			pDistribution = new double[numNodes];
			
			for (int n = 0;  n < numNodes;  n++) {
			    Node node = (Node) nodes.get (n);
			    pDistribution[n] = node.getBelief(Utils.STATE_NOT_RUNNING);
			   			 
			}
			
			net.finalize(); // not strictly necessary, but a good habit
			env.finalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pDistribution;
	}
	
	private int getHighestRewardProcessId(double[] pDistribution)
	{
		Map<Integer, Process> aliveProcessList = mSystem.getAliveProcessList();
		
		System.out.println(TAG + "getHighestRewardProcessId pDist" + pDistribution.toString());
		
		long MaxReward = Long.MIN_VALUE;
		int processId = 0;
		for (Map.Entry<Integer, Process> entry : aliveProcessList.entrySet())
		{
		    Process p = entry.getValue();
		    
		    double probabilityOfNoInvocation = pDistribution[p.getPID()];
		    System.out.println(TAG + "InvocationMatrix"+Arrays.toString(pDistribution));
		    int memoryUsed = p.getMemory();
		    int launchTime = p.getLaunchTime();
		    
		    long score  = getScore(probabilityOfNoInvocation, memoryUsed, launchTime);
		    
		    System.out.println(TAG + " pid " + p.getPID() + " score " + score);
		    
		    if (score > MaxReward)
		    {
		    	MaxReward = score;
		    	processId = p.getPID();
		    }
		    
		}
		
		return processId;
	}
	
	private static final int CONST_MEM = 1;
	private static final int CONST_LTIME = 10;
	
	private long getScore(double prob, int mem, int lTime)
	{
		return (long) (prob*100);
	}

}
