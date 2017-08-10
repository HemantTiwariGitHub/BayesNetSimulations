package BuildingBlocks;
import java.util.Arrays;

public class Process {


	public static final int RUNNING = 101;
	public static final int NOT_RUNNING = 102;
	
	private int mPID;

	private int mMemory;
	private int mLaunchTime;
	private int mStatus;
	Double[] mUseProbability;
	
	public Process(int mPID, int mMemory, int mLaunchTime, int mStatus, Double []useProbability) {
		this.mPID = mPID;
		this.mMemory = mMemory;
		this.mLaunchTime = mLaunchTime;
		this.mStatus = mStatus;
		mUseProbability = useProbability;
		//System.out.println(Arrays.toString(mUseProbability));
	}
	public int getPID() {
		return mPID;
	}
	public void setPID(int mPID) {
		this.mPID = mPID;
	}
	public int getMemory() {
		return mMemory;
	}
	public void setMemory(int mMemory) {
		this.mMemory = mMemory;
	}
	public int getLaunchTime() {
		return mLaunchTime;
	}
	public void setLaunchTime(int mLaunchTime) {
		this.mLaunchTime = mLaunchTime;
	}
	public int getStatus() {
		return mStatus;
	}
	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}
	
	public Double[] getUseProbability() {
		return mUseProbability;
	}
	public void setUseProbability(Double[] mUseProbability) {
		this.mUseProbability = mUseProbability;
	}

	@Override
	public String toString() {
		return "Process [mPID=" + mPID + ", mMemory=" + mMemory + ", mLaunchTime=" + mLaunchTime + ", mStatus="
				+ mStatus + ", mUseProbability=" + Arrays.toString(mUseProbability) + "]";
	}
	
}
