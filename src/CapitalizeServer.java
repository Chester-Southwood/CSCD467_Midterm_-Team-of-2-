import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server program which accepts requests from clients to
 * capitalize strings.  When clients connect, a new thread is
 * started to handle an interactive dialog in which the client
 * sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform
 * dependent.  If you ran it from a console window with the "java"
 * interpreter, Ctrl+C generally will shut it down.
 */
public class CapitalizeServer {

    /**
     * Application method to run the server runs in an infinite loop
     * listening on port 9898.  When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening.  The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages.  It is certainly not necessary to do this.
     */
    public static void main(String[] args) throws Exception 
    {
        System.out.println("The capitalization server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try 
        {
            while (true) 
            {
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        } 
        finally 
        {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class Capitalizer extends Thread 
    {
        private Socket socket;
        private int clientNumber;

        public Capitalizer(Socket socket, int clientNumber) 
        {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() 
        {
            try 
            {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            	MyMonitor jobQueue = new MyMonitor(out); //The JobQueue that handles the commands. -Anthony
            	ThreadPool myPool = new ThreadPool(jobQueue);
            	ThreadManager myManager = new ThreadManager(jobQueue, myPool);            	
                Thread managerThread = new Thread(myManager);
                managerThread.start();
            	// Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("Enter a line with only a period to quit\n");

                // Get messages from the client, line by line; return them
                /* 
                 * When a message is sent to the server, 
                 * the server main thread has to create a job  
                 * and put it into the jobQueue (MyMonitor class) used by the ThreadPool object.   
                 */


                while (true) 
                { 
                	//reassign new instances to reader and writer for new thread
                	in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);
                	
                    String input = in.readLine();
                    if (input == null || input.equals(".") || input.toLowerCase().contentEquals("kill"))
                    {
                    	myPool.stopPool();
                    	break;
                    }
                    else if(jobQueue.isFull())
                    {
                    	in.close();
                    	out.close();
                    }
                    else
                    {
                    	jobQueue.enqueue(input); //We create the first job and add it to the queue.
                        //so now the first job needs to go from the queue, to the pool, and then run.
                    }

                    //So runMe runs the first item in the thread,
                    //Each thread has one job. When it runs, it tells its job to follow the commanded input.
                    //out.println(myPool);
                }
            } 
            catch (IOException e) 
            {
                log("Error handling client# " + clientNumber + ": " + e);
            } 
            finally 
            {
                try 
                {
                    socket.close();
                } 
                catch (IOException e) 
                {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) 
        {
            System.out.println(message);
        }
    }
}
