package PerformanceObserver;

public interface IPerfMonitor {
	

	void updateLaunchTime(int pid, long launchTime); // at every process creation
	void updateMemory(long memory); //occupied memory at every clockTick
	void updateCumulativeLaunchTime(int clockTick, int numOfLaunches, long cumulativeLaunchTime, long mTrimCount, long mLaunchRequestCount);
	void setRecordFileName(String FileNameLaunchTime, String FileCumulativeLaunchTimesPerTick, String FileNameMemory);

}
