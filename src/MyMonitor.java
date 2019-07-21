import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * the job will be processed by one of
 * the running threads in the pool, which returns the result 4 + 5 = 9 to the corresponding
 * client that made the original request “ADD,4,5”.
 */ 

public class MyMonitor 
{
	class Job 
	{
		private String serviceRequested;
		private PrintWriter pw;
		private Socket client;
		private Job next;
			
		public Job(String serviceRequested) throws IOException
		{
			this.setServiceRequested(serviceRequested);
		}
		
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

		public void runJob()
		{
			int result;
			if(serviceRequested.equals("KILL"))
			{
				result = 1; //change this to kill the threads - may have to do more with this
			}
			String[] operands = serviceRequested.split(",");
			int num1 = Integer.parseInt(operands[1]);
			int num2 = Integer.parseInt(operands[2]);
			if(operands[0].equals("ADD"))
			{
				result = num1 + num2;
			}
			else if(operands[0].equals("SUB"))
			{
				result = num1 - num2;
			}
			else if(operands[0].equals("MUL"))
			{
				result = num1 * num2;
			}
			else //if(operands[0].equals("DIV"))
			{
				result = num1 / num2;
			}
			out.println(result);
			out.println(result);
			printLog(operands[0], num1, num2, result);
			
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
		
		//# 4 from directions, print all actions performed on server onto standard out.
		public void printLog(String command, int value1, int value2, int result)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			Thread currentThread = Thread.currentThread();
			
			System.out.println("Worker Thread #" + currentThread.getId() + " processed service"
					+ "request " + command + ", " + value1 + ", " + value2 + " at TIME: " + dateFormat.format(cal));
		}
		
		@Override
		public String toString()
		{
			//return this.action.toString();
			return"";
		}
	}
	
	//took queue class from previous assignment with modifications. Still needs work on it for functionality of this project.
	//Smart!
	
	private final int maxSize;
	private Job head, tail;
	private int size;
	PrintWriter out;
	
	public MyMonitor(PrintWriter out)
	{
		this.out = out;
		this.maxSize = 50;
		this.size = 0;
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
	 
	public synchronized void enqueue(String arg0) throws IOException 
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
		
		Job node = new Job(arg0);
		
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
	
	public synchronized Job getHead()
	{
		return head; 
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
		
		Object data = head.serviceRequested;
		Job oldHead = head;

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
		Job tempNode = this.head;
		String toStr = "";
		
		while(tempNode != null)
		{
			toStr.concat(" " + (String)tempNode.serviceRequested);
			tempNode = tempNode.next;
		}
		
		return toStr;
	}
}
