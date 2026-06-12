/**Project by Devesh Parekh
 * The purpose of this class is to set up the threads needed to send and 
 * receive messages from client and server via a DatagramSocket */

import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread implements Runnable {
    private DatagramSocket socket; // DatagramSocket is used to receive and send packets
    private String senderName; 

    //  The constructor below will receive the socket from the Client/Server
    public ServerThread(DatagramSocket socket, String senderName) { 
        this.socket = socket;
        this.senderName = senderName;
    }

    public void run() {
        // It loops continuously, listening for messages
        try {
            while (true) {
                // Create a buffer and packet to receive data
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                
                // waits for a message
                socket.receive(receivePacket);
                
                // converts message to a String and print it
                String message = new String(
                    receivePacket.getData(), 0, receivePacket.getLength());
                
                // Use sendername parameter will be passed from server/client classes so it outputs "Server said:" or "Client said:"
                System.out.println("\n" + senderName + " said: " + message);
            }
        } catch (SocketException e) {
            // prints message when socket is closed
            System.out.println("Socket closed.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}