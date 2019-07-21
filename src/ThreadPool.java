import java.io.PrintWriter;

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
	final int v = 500; //5 seconds
	int t1;
	int t2;
	//and the main server thread

	public ThreadPool(MyMonitor jobQueue)
	{
		this.jobQueue = jobQueue;
		this.maxCapacity = 40;
		actualNumberThreads = 5;
		holders = new WorkerThread[5];
		for(int i = 0; i < holders.length; i++)
		{
			holders[i] = new WorkerThread();
			//holders[i].start();	//tHIS 
		}
	}
	
	public void runMe()
	{
		for(int i = 0; i < holders.length; i++)
		{
			while(jobQueue.size() > 0)
			{
				holders[0] = new WorkerThread();
				holders[0].start();
			}
		}
	}
	
//each Worker will grab a job in the jobQueue for
//processing if there are available jobs in the jobQueue.	
	public class WorkerThread extends Thread 
	{
		public boolean running = false;
		public MyMonitor.Job myJob;
		public WorkerThread()
		{
			this.myJob = jobQueue.getHead();
			jobQueue.dequeue();
		}
		@Override
		public void run()
		{
			//“ADD,4,5”, “SUB,10,9”, “MUL,2,3”, “DIV,4,2” and “KILL”.		
			//This is where we call string.split, have each command do math, etc. Pretty simple? I think?
			running = true;
			myJob.runJob();
		}
		
	}

//A test method to get threads started and running
	public void addToPool(MyMonitor.Job myJob)
	{
		int cur = -1;
		for(int i = 0; i < holders.length; i++)
		{
			if(!(holders[i].running))
			{
				cur = i;
				i = holders.length;
			}
		}
		holders[cur] = new WorkerThread();
		
	}
	
//start all available threads in the pool and Worker
//threads start to process jobs	
	public void startPool()
	{
		//if do .lengt, would it do all index, if so it may try to do null.start()?
		for(int i = 0; i < actualNumberThreads; i++)
		{
			holders[i].start();
		}
	}

//double the threads in pool according to threshold	
	public synchronized void increaseThreadsInPool()
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
				//temp[i] = new WorkerThread();
				
				//will it throw error?
				temp[i].start();
			}
		}
		this.holders = temp;
		actualNumberThreads = actualNumberThreads *2;
	}
	
//halve the threads in pool according to threshold	
	public synchronized void decreaseThreadsInPool()
	{
		WorkerThread[] temp = new WorkerThread[holders.length/2];
		for(int i = 0; i < temp.length; i++)
		{
			temp[i] = holders[i];
		}
		for(int i = temp.length; i < this.holders.length; i++)
		{
			holders[i].interrupt();//kill threads
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
	
	public synchronized int numberThreadsRunning()
	{
		return actualNumberThreads; 
	}
	
	public synchronized int maxCapacity()
	{
		return this.maxCapacity;
	}
	
	//Current test method
	@Override
	public String toString()
	{
		return holders[0].toString();
	}
}
