package PerformanceObserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;

public class PerformanceMonitor implements IPerfMonitor {
	
	String mFileNameLaunchTime, mFileNameMemory, mCumuFile;
	

	@Override
	public void setRecordFileName(String FileNameLaunchTime, String FileCumulativeLaunchTimesPerTick, String FileNameMemory) {
		// TODO Auto-generated method stub
		mFileNameLaunchTime = FileNameLaunchTime;
		mFileNameMemory = FileNameMemory;
		mCumuFile = FileCumulativeLaunchTimesPerTick;
	}

	@Override
	public void updateLaunchTime(int pid, long launchTime) {
		
		String str = "\n" + pid +"\t" + launchTime;
		writeStringToFile(mFileNameLaunchTime, str);		
	}

	@Override
	public void updateMemory(long memory) {
		// TODO Auto-generated method stub
		String str = "\n" + memory ;
		writeStringToFile(mFileNameMemory, str);	
		
	}
	
	
	private static void writeStringToFile(String filename, String str)
	{
		Writer output = null;
		File file = new File(System.getProperty("user.dir")+"\\data\\"+ filename);
		try {
			output = new BufferedWriter(new FileWriter(file, true));
			output.write(str);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void updateCumulativeLaunchTime(int clockTick, int numOfLaunches, long cumulativeLaunchTime, long mTrimCount,
			long mLaunchRequestCount) {
		// TODO Auto-generated method stub
		try{
			double hitRatio = (double)(mLaunchRequestCount-numOfLaunches)/mLaunchRequestCount;
			double avgLaunchTime = cumulativeLaunchTime/mLaunchRequestCount;
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(3);
		
		String str = "\n" + clockTick  +"\t" + mTrimCount+"\t" + mLaunchRequestCount +"\t" + numOfLaunches + "\t"+df.format(hitRatio) +"\t" + cumulativeLaunchTime+ "\t"+avgLaunchTime;
		writeStringToFile(mCumuFile, str);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}



}
