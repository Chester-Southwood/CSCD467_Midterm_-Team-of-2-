/* To do: "KILL", "V seconds"
 * 
 * ThreadManager will be constantly polling the status of jobQueue and the status of
 * ThreadPool every V seconds in order to decide whether to increase or decrease or maintain
 * the current number of the threads in the pool, according to the criteria: 
 * Number of Jobs in Queue:		Nj<=T1(10)	T1(10)< Nj <= T2(20)	T2(20)<Nj<Capacity(50)
 * Number of Threads:			5			10						20
 * 
 * The ThreadManager terminates when a command “KILL” is sent through the client to the server.
 */

public class ThreadManager implements Runnable
{
	public MyMonitor jobQueue;
	public ThreadPool myPool; //
	private int v = 500; //Amount of seconds to decide the number of threads
	int t; //Number of threads
	public ThreadManager(MyMonitor jobQueue, ThreadPool myPool)
	{
		this.jobQueue = jobQueue;
		this.myPool = myPool;
	}
	
	@Override
	public void run() 
	{
		while(myPool.isRunning()) //not termianted
		{
			changePool();
			try
			{
				Thread.sleep(v);
			}
			catch(InterruptedException e)
			{
				return; //termiante
			}
		}
	}	
	
	public void changePool() //This will run every v seconds. What is V supposed to be? I have no clue.   
	{
//Increasing		
		if(myPool.numberThreadsRunning() < 5 && jobQueue.size() > 10) //Running 5 but more than 10 waiting in the queue.
		{
			myPool.increaseThreadsInPool(); 	//Grow from 5 to 10
		}
		else if(myPool.numberThreadsRunning() < 10 && jobQueue.size() > 20)
		{
			myPool.increaseThreadsInPool();		//Grow from 10 to 20
		}
//Decreasing		
		else if(myPool.numberThreadsRunning() >= 20 && jobQueue.size() <= 20) //Running 20 but less than 20 waiting in the queue
		{
			myPool.decreaseThreadsInPool();		//Shrink from 20 to 10
		}
		else if(myPool.numberThreadsRunning() >= 10 && jobQueue.size() <= 10) //Running 10 but less than 10 waiting in the queue
		{
			myPool.decreaseThreadsInPool();		//Shrink from 10 to 5
		}
	}
}
