/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clientside;

import java.io.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;



/**
 *
 * @author regularclip
 */
public class ChatClient {
    
    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("localhost", 6969);
        DataOutputStream out = new DataOutputStream (socket.getOutputStream());
        DataInputStream input = new DataInputStream(socket.getInputStream());
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Your client is connected, please type!");
        
         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        while(true){
           String message = in.readLine();
           out.writeUTF(message);
           out.flush();
           if(message.equals("quit")) break;
           String response = input.readUTF();
           System.out.println("server response: " + response);
        }
        
        
        
    }      
                
    
}
