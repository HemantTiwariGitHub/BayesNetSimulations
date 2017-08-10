package Intelligent;
import norsys.netica.*;

public class BeliefNetBuilder {

	private int mDAG[][];

	public BeliefNetBuilder(int[][] DAG) {
		mDAG = DAG;
	}

	public void createNet() {
		try {
			Node.setConstructorClass("norsys.neticaEx.aliases.Node");
			Environ env = new Environ(null);
			Node[] node = new Node[mDAG.length];

			Net net = new Net();
			net.setName("SimulatedDAG");

			for (int i = 0; i < mDAG.length; i++) {
				node[i] = new Node(Utils.getNodeNameFromIndex(i), Utils.STATE_RUNNING +","+Utils.STATE_NOT_RUNNING , net);
			}

			for (int i = 0; i < mDAG.length; i++) {
				for (int j = i + 1; j < mDAG.length; j++) {
					if (mDAG[i][j] == 1) {
						node[j].addLink(node[i]);
					}
				}

			}

			Streamer stream = new Streamer(System.getProperty("user.dir")+"\\data\\"+ Utils.BELIEF_NET_FILENAME);
			net.write(stream);

			net.finalize(); // free resources immediately and safely; not
			env.finalize();
							// strictly necessary, but a good habit
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			new Utils().writeHeaderLineToFile();
		}
		
	}
}
