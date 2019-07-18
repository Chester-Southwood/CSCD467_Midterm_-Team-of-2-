public class ParallelClient 
{
	public static void main(String[] args) throws Exception
	{

		Client[] clients = new Client[500];
		for(int x = 0; x < clients.length; x++)
		{
			clients[x] = new Client();
			clients[x].start();
			
		}
		for(int x = 0; x < clients.length; x++)
		{
			clients[x].join();
		}

		System.out.println("Parallel Tests are done!!");
	}
}