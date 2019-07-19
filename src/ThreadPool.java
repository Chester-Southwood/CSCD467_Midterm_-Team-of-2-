/*
 * Your ThreadPool class will contain a pool of Worker threads.
 * 
 * When you first start your ThreadPool, you should have 5 threads running by default and ready to process jobs. 
 * The maximum capacity of the thread holder of the pool is 40, 
 * the maximum capacity of jobQueue is 50. 
 * 
 * All other variables, V, T1, T2 should be programmed as parameters so that you can easily change them for testing.
 */
		
public class ThreadPool 
{
	int maxCapacity; //maximum number of threads in the pool
	int actualNumberThreads;
	WorkerThread holders[]; //stores the worker thread references
	boolean stopped; //used to receive a stop signal from main thread
	MyMonitor jobQueue; //shared by all WorkerThread in the pool and ThreadManager
	int v;
	int t1;
	int t2;
	//and the main server thread

	public ThreadPool()
	{
		this.maxCapacity = 40;
		actualNumberThreads = 5;
		holders = new WorkerThread[5];
		for(int i = 0; i < holders.length; i++)
		{
			holders[i] = new WorkerThread();
			holders[i].start();
		}
			
	}
	
//each Worker will grab a job in the jobQueue for
//processing if there are available jobs in the jobQueue.	
	public static class WorkerThread extends Thread 
	{
		
	}

//start all available threads in the pool and Worker
//threads start to process jobs	
	public void startPool()
	{
		for(int i = 0; i < holders.length; i++)
		{
			holders[i].start();
		}
	}

//double the threads in pool according to threshold	
	public void increaseThreadsInPool()
	{
		WorkerThread[] temp = new WorkerThread[holders.length * 2];
		for(int i = 0; i < temp.length; i++)
		{
			if(i < holders.length)
			{
				temp[i] = holders[i];
			}
			else
			{ 
				temp[i] = new WorkerThread();
				temp[i].start();
			}
		}
		this.holders = temp;
		actualNumberThreads = actualNumberThreads *2;
	}
	
//halve the threads in pool according to threshold	
	public void decreaseThreadsInPool()
	{
		WorkerThread[] temp = new WorkerThread[holders.length/2];
		for(int i = 0; i < temp.length; i++)
		{
			temp[i] = holders[i];
		}
		this.holders = temp;
		actualNumberThreads = actualNumberThreads/2;
	}
	
//terminate all threads in the pool gracefully
//all threads in pool terminate when a command KILL is sent through the client to the server.
	public void stopPool()
	{
		for(int i = 0; i < holders.length; i++)
		{
			holders[i].interrupt();
		}
	}
	
	public int numberThreadsRunning()
	{
		return actualNumberThreads; 
	}
	
	public int maxCapacity()
	{
		return this.maxCapacity;
	}
}
