/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverside;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author regularclip
 */
class ConnectionPool {
    
    private ArrayList<Socket> connections;
    
    public ConnectionPool(){
        connections = new ArrayList<Socket>();
    }
    public ArrayList<Socket> getConnectionList(){
        return connections;
    }
    
    public void addConnection(Socket connection){
        connections.add(connection);
    }
    
    
    
}
