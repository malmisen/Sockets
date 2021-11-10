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
       
        System.out.println("You have successfully connected!");
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
    private BufferedReader input;
    public Litsener(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            System.out.println("Awaiting messages!");
            while(!socket.isClosed()){
                String response = input.readLine();
                if(response != null){
                    if(response.equals("quit")){  //server says its okay to quit
                        socket.close();
                    }
                    System.out.println(response);
                } else {
                    System.out.println("Server is closed, please try again later");
                    socket.close();
                }
            }
            System.out.println("Litsener thread quitting...");
            System.exit(0);
        } catch (IOException e) {
           System.out.println("Woops! Something went wrong. \nClient disconnected from server");
           System.out.println("Type 'quit' to terminate");
        }
    }
    
}

class Writer implements Runnable{
    private Socket socket;
    private String userId;
    private PrintWriter out;
    private BufferedReader bf;
    
    public Writer(Socket socket, String userId){
        this.socket = socket;
        this.userId = userId;
    }
    
    @Override
    public void run() {
        try {
            bf = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("Please be nice in the chat!");
            while(!socket.isClosed()){
                String message = bf.readLine();
                if(message != null){
                    
                    if(message.equals("quit")){
                        out.write("quit" + "\r\n");  //We want to quit
                        out.flush();
                        Thread.sleep(1000);   //Wait for listener to get an OKAY from server before we stop this thread (this is dirty tho)
                        out.close();
                        socket.close();
                    }
                    out.write(userId + ": " + message + "\r\n");
                    out.flush();
                }
            }
            System.out.println("Writer thread quitting...");
            
        } catch (IOException ex) {
            System.out.println("Error occured here!");
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
}