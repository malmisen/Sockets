/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverside;

import java.io.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author regularclip
 */
public class ConnectionHandler implements Runnable {
    
    private Socket con;
    private  int threadId;
    
    
    public ConnectionHandler(Socket connectionSocket) {
        con = connectionSocket;
        threadId = (int) (Math.random() * 100);       
    }

    @Override
    public void run() {
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
