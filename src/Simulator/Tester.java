package Simulator;

import Intelligent.BeliefLearner;
import Intelligent.BeliefNetBuilder;
import LRU.LRUMemoryManager;
import BuildingBlocks.ISystemAPI;
import BuildingBlocks.Process;
import BuildingBlocks.SystemImplementation;
import BuildingBlocks.User;

public class Tester {

	//memory in MB
	private static final int TOTAL_SYS_MEM = 4000;
	private static final int THRESHOLD_SYS_MEM = 2500;
	private static final int CURR_SYS_MEM = 500;
	
	private static final int TOTAL_PROCESS = 15;
	private static final int BASE_PROCESS = 3;
	
	public static void main(String args[])
	{
		RunSimulation();
		
		//RunLRUSimulation();
		
		
		//RunLearningProcess();
		
		
		//RunIntelligentSimulation();
		
	}
	/*
	static void RunLRUSimulation()
	{
		int mDAG[][] = DAGCreator.createDAGMatrix(TOTAL_PROCESS, BASE_PROCESS);
		
		ProcessListCreator p = new ProcessListCreator(mDAG);
		
		Process[] pList = p.createProcessList();
		
		BeliefNetBuilder mBeliefNetBuilder = new BeliefNetBuilder(mDAG);
		mBeliefNetBuilder.createNet();
		
		ISystemAPI mSystem = new SystemImplementation(pList, TOTAL_SYS_MEM, THRESHOLD_SYS_MEM, CURR_SYS_MEM, false);
		mSystem.bootSystem();
		
		User mUser = new User(mSystem, BASE_PROCESS);
		mUser.startUserWork();
	}
	
	static void RunIntelligentSimulation()
	{
		int mDAG[][] = DAGCreator.createDAGMatrix(TOTAL_PROCESS, BASE_PROCESS);
		
		ProcessListCreator p = new ProcessListCreator(mDAG);
		
		Process[] pList = p.createProcessList();
		
		BeliefNetBuilder mBeliefNetBuilder = new BeliefNetBuilder(mDAG);
		mBeliefNetBuilder.createNet();
		
		ISystemAPI mSystem = new SystemImplementation(pList, TOTAL_SYS_MEM, THRESHOLD_SYS_MEM, CURR_SYS_MEM, true);
		mSystem.bootSystem();
		
		User mUser = new User(mSystem, BASE_PROCESS);
		mUser.startUserWork();
	}
	
	static void RunLearningProcess()
	{
		BeliefLearner bL =  new BeliefLearner();
		bL.learn();
	}
	*/
	static void RunSimulation()
	{
		int mDAG[][] = DAGCreator.createDAGMatrix(TOTAL_PROCESS, BASE_PROCESS);
		
		ProcessListCreator p = new ProcessListCreator(mDAG);
		
		Process[] pList = p.createProcessList();
		
		
		BeliefNetBuilder mBeliefNetBuilder = new BeliefNetBuilder(mDAG);
		mBeliefNetBuilder.createNet();
		
		
		ISystemAPI mLRUSystem = new SystemImplementation(pList, TOTAL_SYS_MEM, THRESHOLD_SYS_MEM, CURR_SYS_MEM, false);
		ISystemAPI mIntelligentSystem = new SystemImplementation(pList, TOTAL_SYS_MEM, THRESHOLD_SYS_MEM, CURR_SYS_MEM, true);

		User mUser = new User(mLRUSystem, mIntelligentSystem, BASE_PROCESS, pList);
		mUser.startUserWork();
		
	}
	
	
}
