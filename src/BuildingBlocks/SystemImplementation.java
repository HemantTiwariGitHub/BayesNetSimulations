package BuildingBlocks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import BuildingBlocks.User.UserWork;
import Intelligent.BeliefLearner;
import Intelligent.IntelligentMemoryManager;
import Intelligent.Utils;
import LRU.LRUMemoryManager;
import PerformanceObserver.IPerfMonitor;
import PerformanceObserver.PerformanceMonitor;

public class SystemImplementation implements ISystemAPI {



	private   String TAG = "SystemImplementation:";
	private static final boolean FULL_DEBUG = false;
	private Process [] mAllProcessList;
	private Map<Integer,Process> mAliveProcessList;
	
	private int mTotalMemory;
	private int mMemoryThreshold;
	private int mCurrentMemory;
	private Process mCurrentUserProcess;
	
	private IMemoryManager mMemoryManager;
	
	private int mClockTickCount;
	private boolean isIntelligent;
	private IPerfMonitor mPerformanceMonitor; 
	
	private static final String MEM_FILENAME = "MEM";
	private static final String LAUNCH_FILENAME = "LAUNCH";
	private static final String CUMU_FILENAME = "CUMU";
	
	int mNumOfLaunches;
	long mCumuLaunchTime;
	long mTrimMemoryCount;
	long mLaunchRequestCount;
	

	public SystemImplementation(Process[] AllProcessList, int mTotalMemory,
			int mMemoryThreshold, int mCurrentMemory, boolean isIntelligentMemoryManager) {
		super();
		mPerformanceMonitor = new PerformanceMonitor();
		if (isIntelligentMemoryManager)
		{
			TAG = "IntelligentSystem: ";
			isIntelligent = true;
			mPerformanceMonitor.setRecordFileName(LAUNCH_FILENAME + "_I.txt", CUMU_FILENAME + "_I.txt", MEM_FILENAME + "_I.txt");
		}else
		{
			TAG = "LRUSystem: ";
			isIntelligent = false;
			mPerformanceMonitor.setRecordFileName(LAUNCH_FILENAME + ".txt", CUMU_FILENAME + ".txt", MEM_FILENAME + ".txt");
		}
		
		System.out.println(TAG + " ctor");
		this.mAllProcessList = AllProcessList;
		this.mAliveProcessList = new HashMap<Integer, Process>();
		this.mTotalMemory = mTotalMemory;
		this.mMemoryThreshold = mMemoryThreshold;
		this.mCurrentMemory = mCurrentMemory;
		if (!isIntelligentMemoryManager)
		{
			mMemoryManager = new LRUMemoryManager(this);
		}else {
			
			mMemoryManager = new IntelligentMemoryManager(this);
		}
		
		mNumOfLaunches = 0;
		mCumuLaunchTime = 0;
	}
	
	public void updateLaunchRequests()
	{
		mLaunchRequestCount++;
	}
	

	@Override
	public void startProcessByUser(int PID) {
		

		
		System.out.println(TAG + "Process" + PID +" Requested by User");
		mMemoryManager.startProcess(PID);
		mCurrentUserProcess = getProcessFromPID(PID);
	}
	
	


	@Override
	public void killProcess(int PID) {
		
		
		
		Process killedProcess = getProcessFromPID(PID);
		
		mAllProcessList[getProcessIndexPID(PID)].setStatus(Process.NOT_RUNNING);
		
		mAliveProcessList.remove(PID);

		mCurrentMemory -= killedProcess.getMemory();
		System.out.println(TAG + "Process" + PID +" Killed");
		
		
		
	}
	
	@Override
	public void bootSystem() {
		mClockTickCount = 0;
		System.out.println(TAG + "System Boot");
		Timer newTimer = new Timer();
    	newTimer.schedule(new KlockWork() , 0);
	}
	

	@Override
	public Map<Integer, Process> getAliveProcessList() {
		
		return mAliveProcessList;
	}
	
	@Override
	public void startProcessMM(int PID) {
		
		Process currentProcess = getProcessFromPID(PID);
		
		mAliveProcessList.put(PID, currentProcess);
		//mCurrentUserProcess = currentProcess;
		mCurrentMemory += currentProcess.getMemory();
		
		mAllProcessList[getProcessIndexPID(PID)].setStatus(Process.RUNNING);
		System.out.println(TAG + "Process" + PID +" Started");
		
		
		//Inform to perf mon
		mPerformanceMonitor.updateLaunchTime(currentProcess.getPID(), currentProcess.getLaunchTime());
		mNumOfLaunches++;
		mCumuLaunchTime += currentProcess.getLaunchTime();
		
	
		
		
	}



	
	
	public Process getProcessFromPID(int PID)
	{
		for (int i =0; i < mAllProcessList.length; i++)
		{
			if (mAllProcessList[i].getPID() == PID)
			{
				return mAllProcessList[i];
			}
		}
		return null;
	}
	
	
	public int getProcessIndexPID(int PID)
	{
		for (int i =0; i < mAllProcessList.length; i++)
		{
			if (mAllProcessList[i].getPID() == PID)
			{
				return i;
			}
		}
		return -1;
	}

	
	public Process[] getAllProcessList() {
		return mAllProcessList;
	}

	public void setAllProcessList(Process[] mAllProcessList) {
		this.mAllProcessList = mAllProcessList;
	}


	public void setAliveProcessList(Map<Integer, Process> mAliveProcessList) {
		this.mAliveProcessList = mAliveProcessList;
	}

	@Override
	public int getTotalMemory() {
		return mTotalMemory;
	}

	public void setTotalMemory(int mTotalMemory) {
		this.mTotalMemory = mTotalMemory;
	}

	@Override
	public int getMemoryThreshold() {
		return mMemoryThreshold;
	}

	public void setMemoryThreshold(int mMemoryThreshold) {
		this.mMemoryThreshold = mMemoryThreshold;
	}

	@Override
	public int getCurrentMemory() {
		return mCurrentMemory;
	}

	public void setCurrentMemory(int mCurrentMemory) {
		this.mCurrentMemory = mCurrentMemory;
	}

	@Override
	public Process getCurrentUserProcess() {
		return mCurrentUserProcess;
	}

	public void setCurrentUserProcess(Process mCurrentUserProcess) {
		this.mCurrentUserProcess = mCurrentUserProcess;
	}


	class KlockWork extends TimerTask {
		@Override
		public void run() {

	    	
			//System.out.println(TAG + "schedule : " );
			Timer newTimer = new Timer();
	    	newTimer.schedule(new KlockWork() , 1000);
	    	
	    	clockTick();
		}

	}
	
	@Override
	public void startProcessInternal(int PID)
	{
		mMemoryManager.startProcess(PID);
	}

	
	private void clockTick()
	{
		// find current PID, and start some dependent tasks as per probability
		System.out.println(TAG + " System Tick");
		
		if (mCurrentUserProcess == null) {
			System.out.println(TAG + " No User Process");
			
		} else {
			int PID = mCurrentUserProcess.getPID();
			System.out.println(TAG + " Current User Process : " + PID);
		
		}
		
		if (mCurrentMemory > mMemoryThreshold) {
			mMemoryManager.trimMemory();
			mTrimMemoryCount++;
		}

		if (true == isIntelligent)
		{
			Utils.writeEvidenceToFile(mAllProcessList, mClockTickCount);
		}
		System.out.println(TAG +this.toString());
		mClockTickCount++;
		
		//if IntelligentSystem, at every 100 clock Tick Learn
		
		if (isIntelligent && mClockTickCount%25 == 0)
		{
			Locker locker = Locker.getLocker();
			synchronized(locker) {
			RunLearningProcess();
			}
		}
		
		
		//Inform to perf mon
		mPerformanceMonitor.updateMemory(mCurrentMemory);
		mPerformanceMonitor.updateCumulativeLaunchTime(mClockTickCount, mNumOfLaunches, mCumuLaunchTime, mTrimMemoryCount, mLaunchRequestCount);

	}
	
	void RunLearningProcess()
	{
		BeliefLearner bL =  new BeliefLearner();
		bL.learn();
	}


	
	private boolean isRunning(int PID)
	{
		return (mAliveProcessList.get(PID) != null);
	}
	
	@Override
	public String toString() {
		if(FULL_DEBUG) {
		return "SystemStub [mAllProcessList=" + Arrays.toString(mAllProcessList) + ", mAliveProcessList="
				+ mAliveProcessList + ", mTotalMemory=" + mTotalMemory + ", mMemoryThreshold=" + mMemoryThreshold
				+ ", mCurrentMemory=" + mCurrentMemory + ", mCurrentUserProcess=" + mCurrentUserProcess
				+ ", mMemoryManager=" + mMemoryManager + "]";
		} else {
			
			return " mCurrentMemory=" + mCurrentMemory + ", mCurrentUserProcess=" + mCurrentUserProcess;
			
		}
	}
	







}
