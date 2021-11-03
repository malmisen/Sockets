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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author regularclip
 */
public class ChatServer implements Runnable {
    
    private static final ArrayList<Socket> connections = new ArrayList<>();
    
    public static void main(String[] args) throws IOException{
       ServerSocket welcomeSocket = new ServerSocket(6969);
       System.out.println("The server is online...");
       
       
       //Keeps track of connected clients
       //ConnectionPool connectionPool = new ConnectionPool();
       
       
       //Accept incoming connections
       while(true){
           
           System.out.println("Server is now accepting incoming connections...");
           Socket connectionSocket = welcomeSocket.accept();
           connections.add(connectionSocket);
           System.out.println("Client connected!");
           
           System.out.println("Starting new thread to serve client...");
           Runnable connectionHandler = new ConnectionHandler(connectionSocket);
           new Thread(connectionHandler).start();
       }
               
     
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
               // System.out.println("Client with threadId: "+ threadId + "says: " + clientMessage);
                for(Socket connection: connections){
                    output = new DataOutputStream(connection.getOutputStream());
                    output.writeUTF(clientMessage);
                }
            }
            
            input.close();
    
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
