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
		private String action;
		private PrintWriter pw;
		private Socket client;
		
		public Job(String action, Socket client) throws IOException
		{
			this.setAction(action);
			this.setClient(client);
			this.setPrintWriter(new PrintWriter(this.client.getOutputStream(), true)); //printwriter that appends
		}
		
		public String getAction() 
		{
			return action;
		}

		public void setAction(String action) 
		{
			this.action = action;
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
				this.client.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		@Override
		public String toString()
		{
			return this.action.toString();
		}
	}

}
