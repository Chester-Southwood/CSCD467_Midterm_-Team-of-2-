/*
 * Modify the client and server program so that it can accept and support five types of commands, 
 * including “ADD,4,5”, “SUB,10,9”, “MUL,2,3”, “DIV,4,2” and “KILL”.
 * there is no space between operands and operators in the command string.
 * 
 * When a message is sent to the server, 
 * the server main thread has to create a job  
 * and put it into the jobQueue (MyMonitor class) used by the ThreadPool object.   
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
  * A simple Swing-based client for the capitalization server.
  * It has a main frame window with a text field for entering
  * strings and a textarea to see the results of capitalizing
  * them.
  */
public class CapitalizeClient {

      private BufferedReader in;
      private PrintWriter out;
      private JFrame frame = new JFrame("Capitalize Client");
      private JTextField dataField = new JTextField(40);
      private JTextArea messageArea = new JTextArea(8, 60);

      /**
        * Constructs the client by laying out the GUI and registering a
        * listener with the textfield so that pressing Enter in the
        * listener sends the textfield contents to the server.
        */
      public CapitalizeClient() 
      {

            // Layout GUI
            messageArea.setEditable(false);
            frame.getContentPane().add(dataField, "North");
            frame.getContentPane().add(new JScrollPane(messageArea), "Center");

            // Add Listeners
            //This is where we'll need to create a job, insert it into jobQueue, that's us
            dataField.addActionListener(new ActionListener() 
            {
                  /**
                    * Responds to pressing the enter key in the textfield
                    * by sending the contents of the text field to the
                    * server and displaying the response from the server
                    * in the text area.   If the response is "." we exit
                    * the whole application, which closes all sockets,
                    * streams and windows.
                    */
                  public void actionPerformed(ActionEvent e) 
                  {
                        out.println(dataField.getText());
                        String response;
                        try 
                        {
                              response = in.readLine(); //We need to send "response" to the server.
                              /*if (response == null || response.equals("")) 
                              {
                                  System.out.println("client to terminate.");
                                  System.exit(0);
                              }*/
                        } 
                        catch (IOException ex) 
                        {
                            response = "Error: " + ex;
                            System.out.println("" + response + "\n");
                        }
                        messageArea.append(response + "\n");
                        dataField.selectAll();
                  }
            });
      }

      /**
        * Implements the connection logic by prompting the end user for
        * the server's IP address, connecting, setting up streams, and
        * consuming the welcome messages from the server.   The Capitalizer
        * protocol says that the server sends three lines of text to the
        * client immediately after establishing a connection.
        */
      public void connectToServer() throws IOException 
      {

            // Get the server address from a dialog box.
            String serverAddress = JOptionPane.showInputDialog(
                  frame,
                  "Enter IP Address of the Server:",
                  "Welcome to the Capitalization Program",
                  JOptionPane.QUESTION_MESSAGE);

            // Make connection and initialize streams
            Socket socket = new Socket(serverAddress, 9898);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Consume the initial welcoming messages from the server
            for (int i = 0; i < 3; i++) 
            {
                  messageArea.append(in.readLine() + "\n");
            }
            
            //chester line
            //socket.close();
      }

      /**
        * Runs the client application.
        */
      public static void main(String[] args) throws Exception 
      {
            CapitalizeClient client = new CapitalizeClient();
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.pack();
            client.frame.setVisible(true);
            client.connectToServer();
      }
}