package BuildingBlocks;

public interface IMemoryManager {

	void startProcess(int PID);
	void killProcess (int PID);
	void trimMemory();
}
