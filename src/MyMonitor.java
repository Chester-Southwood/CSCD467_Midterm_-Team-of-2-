import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * the job will be processed by one of
 * the running threads in the pool, which returns the result 4 + 5 = 9 to the corresponding
 * client that made the original request “ADD,4,5”.
 */ 

public class MyMonitor 
{
	private class Job 
	{
		private String serviceRequested;
		private PrintWriter pw;
		private Socket client;
		
		public Job(String serviceRequested, Socket client) throws IOException
		{
			this.setServiceRequested(serviceRequested);
			this.setClient(client);
			this.setPrintWriter(new PrintWriter(this.client.getOutputStream(), true)); //printwriter that appends
		}
		
		public String getServiceRequested() 
		{
			return serviceRequested;
		}

		public void doMath(String serviceRequested)
		{
			//This is where we call string.split, have each command do math, etc. Pretty simple? I think?
		}
		
		
		public void setServiceRequested(String serviceRequested) 
		{
			this.serviceRequested = serviceRequested;
		}

		public PrintWriter getPrintWriter() 
		{
			return pw;
		}

		public void setPrintWriter(PrintWriter pw) 
		{
			this.pw = pw;
		}

		public Socket getClient() 
		{
			return client;
		}

		public void setClient(Socket client) 
		{
			this.client = client;
		}
		
		public void printToClient(String msg)
		{
			this.pw.println(msg);
		}
		
		public void terminateJob()
		{
			try 
			{
				this.client.close(); //close socket connection
			} 
			catch (IOException e) 
			{
				e.printStackTrace(); //shouldn't happen
			}
		}
		
		@Override
		public String toString()
		{
			return this.action.toString();
		}
	}
	
	//took queue class from previous assignment with modifications. Still needs work on it for functionality of this project.
	//Smart!
	
	private class Node 
    {
	   private Object data;
	   private Node next;
	   public Node(Object newData) 
       {
		   this.data = newData;
		   this.next = null;
	   }
	}  
	
	private final int maxSize;
	private Node head, tail;
	private int size;
	
	public MyMonitor()
	{
		this.maxSize = 50;
		this.size = 0;
	}
	
	public synchronized int getLineCount()
	{
		return this.lineCount;
	}
	
	public synchronized void setLineCount(int value)
	{
		this.lineCount += value;
	}
	
	public synchronized int getRegexCount()
	{
		return this.regexCount;
	}
	
	public synchronized void incrementRegexCount(int value)
	{
		this.regexCount += value;
	}
	
	public synchronized boolean getIsDoneSearching()
	{
		return this.isDoneSearching;
	}
	
	public synchronized void setIsDoneSearching(boolean value)
	{
		this.isDoneSearching = value;
	}

	public synchronized boolean isEmpty() 
	{
		return this.size() == 0;
	}
	
	public synchronized boolean isFull()
	{
		return this.size() >= this.maxSize;
	}
	 
	public synchronized int size() 
	{
		return this.size;
	}
	 
	public synchronized void enqueue(Object arg0) 
	{
		while(this.isFull())
		{
			try 
			{
				this.wait();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		Node node = new Node(arg0);
		
		if (this.isEmpty())
		{
			head = node;
		}
		else
		{
			tail.next = node;  
		}   
		
		tail = node;          
		size++;
		
		notifyAll();
	}
	 
	public synchronized Object dequeue() 
	{
		while(this.isEmpty())
		{
	         try 
	         {
	             wait();
	         }
	         catch (InterruptedException e ) 
	         {
	             e.printStackTrace();//should stop thread?
	         }		
	    }
		
		Object data = head.data;
		Node oldHead = head;

		head = head.next;
		oldHead.next = null;
		
		this.size--;
		
		if(this.isEmpty())
		{
			tail = null;
		}
		
		notify();
		
		return data;
	}
	
	@Override
	public String toString()
	{
		Node tempNode = this.head;
		String toStr = "";
		
		while(tempNode != null)
		{
			toStr.concat(" " + (String)tempNode.data);
			tempNode = tempNode.next;
		}
		
		return toStr;
	}
}
