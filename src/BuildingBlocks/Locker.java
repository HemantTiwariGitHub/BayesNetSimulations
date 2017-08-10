package BuildingBlocks;

public class Locker {

	static Locker mLocker = null;
	private Locker()
	{
		
	}
	
	public synchronized static Locker getLocker(){
		
		if (mLocker == null)
		{
			mLocker = new Locker();
		}
		return mLocker;
	}
}
