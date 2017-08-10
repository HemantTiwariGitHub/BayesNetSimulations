package BuildingBlocks;

import java.util.Map;

public interface ISystemAPI {
	
	Map<Integer, Process> getAliveProcessList();
	Process[] getAllProcessList();
	
	int getCurrentMemory();
	
	void startProcessByUser(int PID);
	void killProcess(int PID);
	
	int getMemoryThreshold();
	int getTotalMemory();
	
	Process getCurrentUserProcess();
	void bootSystem();
	Process getProcessFromPID(int pID);
	void startProcessMM(int pID);
	void updateLaunchRequests();
	void startProcessInternal(int PID);
	
	

}
