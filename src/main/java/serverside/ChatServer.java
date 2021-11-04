/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
       
       ArrayList<Socket> connections = new ArrayList<>();
       
       //Accept incoming connections
       while(true){
           
           System.out.println("Server is now accepting incoming connections...");
           Socket connectionSocket = welcomeSocket.accept();
         
           connections.add(connectionSocket);
           System.out.println("Client connected!");
  
           System.out.println("Starting new thread to serve client...");
           Runnable ch = new ConnectionH(connectionSocket, connections);
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

    public ConnectionH(Socket connectionSocket, ArrayList<Socket> connections){
        con = connectionSocket;
        cons = connections;
     
    }
    @Override
    public void run() {
        int threadId = (int)(Math.random()*100);
        System.out.println("Thread " + threadId + " is now running");
     
        try {
            DataInputStream input = new DataInputStream(con.getInputStream());
            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            
            String clientMessage = "";
            while(true){
                clientMessage = input.readUTF();
                if(clientMessage.equals("quit")) break;
                

                for(Socket connection: cons){
                    //Check if the socket being served matches the socket in the list of sockets. If so, do not reply to that client
                    if(!con.equals(connection)){
                        output = new DataOutputStream(connection.getOutputStream());
                        output.writeUTF(clientMessage);
                    }
                }
            }
            
            input.close();
    
            
        } catch (IOException ex) {
            
        }
    }
    
}