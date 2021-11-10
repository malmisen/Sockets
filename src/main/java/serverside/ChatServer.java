/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverside;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * 
 * @author regularclip
 */
public class ChatServer {
    
    public static void main(String[] args) throws IOException{
       ServerSocket welcomeSocket = new ServerSocket(6969);
       System.out.println("The server is online...");
       
       //ConnectionPool pool = new ConnectionPool();
       
       ArrayList<Socket> connections = new ArrayList<Socket>();
       int threadId = 0;
      
       //Accept incoming connections
       while(true){           
           System.out.println("Server is now accepting incoming connections...");
           Socket connectionSocket = welcomeSocket.accept();
         
           connections.add(connectionSocket);
           //pool.addConnection(connectionSocket);
           System.out.println("Client connected!");
           System.out.println("Starting new thread to serve client...");
           threadId++;
           Runnable ch = new ConnectionH(connectionSocket, threadId, connections);
           new Thread(ch).start();
       }           
   }   
}

/*
*
*/
class ConnectionH implements Runnable{
    private Socket con;
    private ArrayList<Socket> cons;
    private final int threadId;
    private String clientMessage;
    //private ConnectionPool pool;

    public ConnectionH(Socket connectionSocket, int threadId, ArrayList<Socket> connections){
        con = connectionSocket;
        cons = connections;
        this.threadId = threadId;
        clientMessage = "";
    }
    
    @Override
    public void run() {
        System.out.println("Thread " + threadId + " is now running");
     
        try {
  
            BufferedReader input  = new BufferedReader(new InputStreamReader(con.getInputStream()));
            PrintWriter writer;
            String temp = "";
         
            while(!con.isClosed()){
                temp = input.readLine();
                if(temp != null){
                    System.out.println("Thread: " + threadId + " msg: " + temp);
                    
                    if(temp.equals("quit")){  //client wants to quit
                        writer = new PrintWriter(con.getOutputStream());
                        writer.write("quit" + "\r\n");  //tell client its okay to quit
                        writer.flush();
                        writer.close();
                        
                        //remove this socket from the list of connected clients
                        for(Socket connection: cons){
                            if(con.equals(connection)){
                                cons.remove(connection);
                                con.close();
                                break;   //break out of this for loop
                            }
                        }
                    } else {            
                        for(Socket connection: cons){           //broadcast to all clients
                            if(!con.equals(connection)){        //dont write to ourselves.
                                System.out.println("Thread "+ threadId + " About to broadcast");
                                writer = new PrintWriter(connection.getOutputStream());
                                writer.write(temp + "\r\n");
                                writer.flush();
                            }
                        }
                    }
                }
            }
            System.out.println("Thread: " + threadId + " is now done serving its client");
        } catch (IOException ex) {
            System.out.println("Error on serverside, threadId: " + threadId);
            System.out.println("Thread: " + threadId + " client message is: " + clientMessage);
            
        }
    }    
}