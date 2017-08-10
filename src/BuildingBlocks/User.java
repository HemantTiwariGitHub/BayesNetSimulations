package BuildingBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class User {

	private static final String TAG = "User:";
	Timer mTimer;
	ISystemAPI mSystem1, mSystem2;
	int mBaseProcessCount;
	Process[] mProcessList;
	Process sCurrentUserProcess;
	

	public User(ISystemAPI system1, ISystemAPI system2, int numOfBaseProcess, Process[] processList) {
		System.out.println(TAG + " ctor");
		mTimer = new Timer();
		mSystem1 = system1;
		mSystem2 = system2;
		mBaseProcessCount = numOfBaseProcess;
		mProcessList = processList;
	}

	public void startUserWork() {
		System.out.println(TAG + "startUserWork");
		mSystem1.bootSystem();
		mSystem2.bootSystem();
		mTimer.schedule(new UserWork(), 0);
	}
	
	private void chooseAndRun() {
		int val = new Random().nextInt(10);
		int nextPID = 2;
		if (val<5)
		{
			nextPID = 0;
		} else if ( val <7)
		{
			nextPID = 1;
		}
		
		
		System.out.println(TAG + "chooseAndRun" + nextPID);
		mSystem1.startProcessByUser(nextPID);
		mSystem2.startProcessByUser(nextPID);
		sCurrentUserProcess = mProcessList[nextPID];
		
		traverseAndStart();
	}


	class UserWork extends TimerTask {
		@Override
		public void run() {
			Random r = new Random();
	    	long l = Math.abs(r.nextLong());
	    	long delay = l%3000;
	    	
			System.out.println(TAG + "Next User Whim after : " + delay);
			Timer newTimer = new Timer();
	    	newTimer.schedule(new UserWork() , delay);
	    	
			chooseAndRun();
		}

	}

	private void traverseAndStart()
	{
		List<Process> mList = new ArrayList<Process>();
		System.out.println(TAG + "traverseAndStart");
		
		mList.add(sCurrentUserProcess);
		
		while (mList.isEmpty() == false)
		{
			Process newNode = mList.remove(0);
			Double[] UseProbability = newNode.getUseProbability();
			
			System.out.println(TAG + "Current Node : " + newNode.getPID() );
			for (int dependentProcessId = 0; dependentProcessId < UseProbability.length; dependentProcessId++)
			{
				if (UseProbability[dependentProcessId] != null && UseProbability[dependentProcessId] > 0.001)
				{
					System.out.println(TAG + "Dependent Node" +  dependentProcessId + " Use Probability ");
					Double newRandom  = Math.random();
					
					System.out.println(TAG + "Dependent Node" +  dependentProcessId + " Use Probability " +UseProbability[dependentProcessId] + " Random " +newRandom);
					if (UseProbability[dependentProcessId] > newRandom)
					{
						mSystem1.startProcessInternal(dependentProcessId);
						mSystem2.startProcessInternal(dependentProcessId);
						
							
					}
					
					mList.add(mProcessList[dependentProcessId]);	
					
				}

			}

		}

	}

}
