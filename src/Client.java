import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client extends Thread
	{		
	  private BufferedReader in;
      private PrintWriter out;
      Socket socket;
      //test cases for commands
      String[] commands = {"asdf", "capitalize me", "I hope this works", "A+ Material here :D", "ADD,4,5", "MUL,6,8", "DIV,50,5", "SUB,53425,2345", "ADD,455,4355", "MUL,6346,84365", "DIV,543650,7655", "SUB,765425,6575"}; 
      
      public void run()
      {
    	  Random random = new Random();
    	  String response = "";
    	  int messagesToSend = random.nextInt(4);
    	  try
    	  {
    		  connectToServer();
    		  String welcome = in.readLine(); 
    		  System.out.print(welcome + "\n");
    		  if(!welcome.equals("Server busy try again later."))
    		  {
				  //System.out.println("Going to send " + messagesToSend + " messages");
	    		  for(int x = 0; x < messagesToSend; x++)
	    		  {
	    			  out.println(commands[random.nextInt(commands.length)]);
	    			  
	    			  try 
	    			  {
	                      response = in.readLine();
	                      if (response == null || response.isEmpty())
	                      {
	                          System.out.println("client to terminate.");
	                      }
	    			  }  
	    			  catch (IOException ex) 
	    			  {
                           response = "Error: " + ex;
                           System.out.println("" + "Error: " + ex + "\n");
	    			  }
	    			  finally
	    			  {
	    				  System.out.println(response);
	    			  }
	    		  }
	    		  out.println(".");
    		  }
    	  }
    	  catch(IOException e)
    	  {
    		  //Do nothing
    	  }
      }
	
      public void connectToServer() throws IOException 
      {

            // Get the server address from a dialog box.
            String serverAddress = "localhost";

            // Make connection and initialize streams
            socket = new Socket(serverAddress, 9898);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Consume the initial welcoming messages from the server
      }		
      
      public void sendCommand(String command) throws IOException
      {
    	  this.connectToServer();
    	  this.out.println(command);
    	  System.out.println(in.readLine());
      }
	}