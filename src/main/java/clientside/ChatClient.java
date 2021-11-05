/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clientside;

import java.io.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author regularclip
 */
public class ChatClient {
    
    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("127.0.0.1", 6969);
       
        System.out.println("You have successfully contected!");
        System.out.println("Please enter a username:");
        Scanner keyboard = new Scanner(System.in);
        String username = keyboard.nextLine();
        System.out.println("Your username is: " + username + "\nType 'quit' in chat to terminate");
        Runnable litsener = new Litsener(socket);
        Runnable writer = new Writer(socket, username);
        
        new Thread(litsener).start();
        new Thread(writer).start();
    }      
                
}


class Litsener implements Runnable{
    private Socket socket;
    public Litsener(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            System.out.println("Awaiting messages!");
            while(true){
                String response = input.readUTF();
                System.out.println(response);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Litsener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

class Writer implements Runnable{
    private Socket socket;
    private String userId;
    public Writer(Socket socket, String userId){
        this.socket = socket;
        this.userId = userId;
    }
    
    @Override
    public void run() {
        try {
            DataOutputStream out = new DataOutputStream (socket.getOutputStream());
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please be nice in the chat!");
            while(true){
                String message = bf.readLine();
                if(message.equals("quit")){
                    System.out.println("Quitting...");
                    System.exit(0);
                }
                out.writeUTF(userId + ": " + message);
                out.flush();
            }
             
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}