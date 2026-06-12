/** Project By Devesh Parekh
 * The purpose of this class is to implement
 * a udp-based server chat that listens on port 50000
 * and waits to receive messages from clients and send message back to client*/

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);
        try {
            DatagramSocket socket = new DatagramSocket(50000); // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/net/DatagramSocket.html
            System.out.println("Server has opened communications on port 50000.");
           
            String ipAddress = InetAddress.getLocalHost().getHostAddress(); // returns hostt's ip address || https://docs.oracle.com/javase/8/docs/api/java/net/InetAddress.html
            System.out.println("Server IP Address: " + ipAddress); //       
            
            // waits for client message to get their address 
            System.out.println("Waiting for a client to connect...");
            byte[] buff = new byte[1024]; // buffer temporarily stores data to help prevent packet loss
            DatagramPacket initialPacket = new DatagramPacket(buff, buff.length); //(buffer,length of byte) Constructs a DatagramPacket for receiving packets with length, will get passed to serverthread
            
            socket.receive(initialPacket); 
            
            // store the client's info 
            InetAddress clientAddress = initialPacket.getAddress(); // inet address gets ip address
            int clientPort = initialPacket.getPort(); // gets port number
            
            String message = new String(initialPacket.getData(), 0, initialPacket.getLength());
            System.out.println("Received first message from " + clientAddress + ": " + clientPort); // gets client port and ip address when they send their first message
            System.out.println("Client said: " + message);
            
            //  passes "Client" parameter to server thread, so the thread prints "Client said:"
            ServerThread receiver = new ServerThread(socket, "Client"); // calls server thread classs, handles incoming messages
            Thread thread = new Thread(receiver);
            thread.start();

            System.out.println("Chat session started. Type 'q' to quit.");
           
            // loop to keep accepting inputs until the letter q is typed in to end loop
            while (true) {
                String typeMessage = input.nextLine();
                
                if (typeMessage.equals("q")){
                    break;
                }
                
                byte[] sendBuffer = typeMessage.getBytes();
                
                // Send packet to the client's address and port
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                socket.send(sendPacket);
            }
            
            System.out.println("Server is now shutting down.");
            socket.close(); // closes socket
          
            //exception handling
        } catch (SocketException e) {
            System.out.println("Error, Port 50000 is already in use");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.out.println("Error, Could not find local IP address.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally { // closes scanner
            input.close();
		}
        
    }
}