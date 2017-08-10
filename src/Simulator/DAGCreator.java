package Simulator;

public class DAGCreator {
	
	

	public static int[][] createDAGMatrix(int numProcess, int numRoot)
	{
		int DAG[][] = new int[numProcess][numProcess];
		
		for (int i = 0; i < numProcess; i++)
		{
			for (int j = i+1; j < numProcess; j++ )
			{
				if (j < numRoot)
				{
					continue;
				}
				
				int val = Math.random() > 0.6? 1 : 0;
				DAG[i][j] = val;
			}
		}
		
		for (int i = 0; i < numProcess; i++)
		{
			for (int j = 0; j < numProcess; j++ )
			{
			
				System.out.print(DAG[i][j] +" ");
			}
			System.out.println();
		}
		
		
		return DAG;
				
		
	}
	
	
	

}
