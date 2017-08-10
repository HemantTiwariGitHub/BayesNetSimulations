package LRU;

import java.util.Comparator;
import java.util.PriorityQueue;

import BuildingBlocks.IMemoryManager;
import BuildingBlocks.ISystemAPI;
import BuildingBlocks.Process;

public class LRUMemoryManager implements IMemoryManager {
	private static final String TAG = "LRUMemoryManager:";

	ISystemAPI mSystem;

	public class LRUDetails {
		public LRUDetails(int pID, long lastUsedTime) {
			super();
			PID = pID;
			this.lastUsedTime = lastUsedTime;
		}

		int PID;
		long lastUsedTime;
	}

	PriorityQueue<LRUDetails> mAccessTimeList;

	public LRUMemoryManager(ISystemAPI System) {

		mSystem = System;
		mAccessTimeList = new PriorityQueue<LRUDetails>(mSystem.getAllProcessList().length, new ProcessLRUComparator());

	}

	@Override
	public void startProcess(int PID) {

		Process newProcess = mSystem.getProcessFromPID(PID);
		int currentMemory = mSystem.getCurrentMemory();
		int totalMemory = mSystem.getTotalMemory();

		mSystem.updateLaunchRequests();
		if (mSystem.getAliveProcessList().get(PID) != null) {
			System.out.println(TAG + "Process" + PID + " Already Running");
			for (LRUDetails det : mAccessTimeList) {
				if (det.PID == PID) {
					mAccessTimeList.remove(det);

					LRUDetails newLRUItem = new LRUDetails(PID, System.currentTimeMillis());
					mAccessTimeList.add(newLRUItem);

					break;
				}
			}

			return;
		}

		if (currentMemory + newProcess.getMemory() < totalMemory) {

			startProcessInternal(PID);

		} else {

			cleanMemory(newProcess);

		}

	}

	void cleanMemory(Process newProcess) {
		System.out.println(TAG + "cleanMemory");
		int killingPID = getProcessToKill();
		killProcess(killingPID);

		if (mSystem.getCurrentMemory() + newProcess.getMemory() < mSystem.getTotalMemory()) {

			startProcessInternal(newProcess.getPID());

		} else {

			cleanMemory(newProcess);

		}

	}

	private void startProcessInternal(int PID) {
		mSystem.startProcessMM(PID);
		LRUDetails newLRUItem = new LRUDetails(PID, System.currentTimeMillis());
		mAccessTimeList.add(newLRUItem);
	}

	@Override
	public void killProcess(int PID) {

		mSystem.killProcess(PID);

	}

	@Override
	public void trimMemory() {
		System.out.println(TAG + "trimMemory");
		int killingPID = getProcessToKill();
		killProcess(killingPID);
		if (mSystem.getCurrentMemory() > mSystem.getMemoryThreshold())
		{
			trimMemory();
		}
		
	}

	public class ProcessLRUComparator implements Comparator<LRUDetails> {
		@Override
		public int compare(LRUDetails Process1, LRUDetails Process2) {

			if (Process1.lastUsedTime < Process2.lastUsedTime) {
				return -1;
			}
			if (Process1.lastUsedTime > Process2.lastUsedTime) {
				return 1;
			}
			return 0;
		}
	}
	
	private int getProcessToKill()
	{
		//dont kill TopProcess
		int pid = mAccessTimeList.remove().PID;
		System.out.println(TAG + "Chosen PID " + pid + " Currnt Top PID " + mSystem.getCurrentUserProcess().getPID());
		if (pid == mSystem.getCurrentUserProcess().getPID())
		{
			System.out.println(TAG + " This is Current Top " + pid);
			LRUDetails newLRUItem = new LRUDetails(pid, System.currentTimeMillis());
			mAccessTimeList.add(newLRUItem);
			pid = mAccessTimeList.remove().PID;
			System.out.println(TAG + "New Chosen PID " + pid);
			
		}
		
		
		return pid;
	}

}
