package Simulator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import BuildingBlocks.Process;

public class ProcessListCreator {
	
	private int mDAG[][];
	private static final int MAXMEM = 1000;
	private static final int MAXLAUNCH = 2000;
	private static final int DEPENDENCY = 7;
	
	private Process[]mProcessList;
	
	public ProcessListCreator(int[][] mDAG) {
		super();
		this.mDAG = mDAG;
	}

	public int[][] getDAG() {
		return mDAG;
	}

	public void setDAG(int[][] mDAG) {
		this.mDAG = mDAG;
	}

	
	public Process[] createProcessList()
	{
		mProcessList = new Process[mDAG.length];
		Random r = new Random();
		for (int i = 0; i < mDAG.length; i++) {
			Double memGauss = r.nextGaussian();
			Double launchGauss = r.nextGaussian();

			int memory = Math.abs((int) (memGauss * MAXMEM));
			int launchTime = Math.abs( (int) (launchGauss * MAXLAUNCH));

			Double useProbability[] = new Double[mDAG.length];
			for (int j = i+1; j < mDAG.length; j++)
			{
				if (mDAG[i][j] == 1)
				{
					Random ran = new Random();
					useProbability[j] =  ran.nextInt(DEPENDENCY)/(double)10;
				}
			}
			
			
			
			
			//System.out.println(Arrays.toString(useProbability));
			mProcessList[i] = new Process(i, memory, launchTime, Process.NOT_RUNNING, useProbability);
			

		}

		printList(mProcessList);
		
		return mProcessList;

	}
	
	private static void printList(Process[] mProcessList)
	{
		for (Process P : mProcessList)
		{
			System.out.println(P.toString());
		}
	}
	
	@Override
	public String toString() {
		return "ProcessListCreator [mDAG=" + Arrays.toString(mDAG) + "]";
	}
	
	
	

}
