/**
 * The purpose of this class is to allow a client to send and receive messages from server using udp protocol
 * given that the user gives the correct IP and port number */

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	public static void main(String[] args) {
	Scanner input = new Scanner(System.in);
	
		try {
	DatagramSocket  soc = new DatagramSocket(); // Client has to create their own socket as well for sending/recieving
	System.out.println("Client has started receiving ...");
	
	// code below asks client to input server IP and port number
	
	System.out.println("Please input server IP and port number:");
	String serverIP = input.nextLine();
	int serverPort = input.nextInt();
	input.nextLine();
	
	InetAddress serverAddress = InetAddress.getByName(serverIP); // https://docs.oracle.com/javase/10/docs/api/java/net/InetAddress.html
	
	ServerThread receiver = new ServerThread(soc, "Server");
	
	Thread thread = new Thread(receiver);
	thread.start();
	
	System.out.println("Please type a message to send to server, press q to quit:");
	while (true) {
	//loop to ask user to input a message to send to server continuously
		
		String typeMessage  = input.nextLine();
		
		if (typeMessage.equals("q")){
			break;
		}
	
	// converts string to byte array
		byte[] Buffer = typeMessage.getBytes();
	
	 //datagram packet for sending packets of length to the specified port number.
		DatagramPacket transmitPacket = new DatagramPacket(Buffer, Buffer.length, serverAddress, serverPort); // https://docs.oracle.com/javase/8/docs/api/java/net/DatagramPacket.html
			soc.send(transmitPacket); // sends the transmitPacket to the server using the DatagramSocket
	}
		} //exception handling
		catch(SocketException e) {
			System.out.println("Error, Port 50000 is already in use by another application");
			e.printStackTrace();

		}catch (UnknownHostException e) {
			System.out.println("Error, Could not find local IP address.");
            e.printStackTrace();
        }
		catch (IOException e) { // prevents io error for soc.send
            e.printStackTrace();
        }
		finally { // closes scanner
            input.close();
		}
	}
	
}
