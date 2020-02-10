package Nettverk; /**
 * Socket programming example: TCP Multi-client Server
 * DATA2410 Networking and Cloud Computing, Spring 2020
 * Raju Shrestha, OsloMet
 **/
import java.net.*;
import java.io.*;
import java.util.ArrayList;


import static Nettverk.HjelpeMetoder.*;


public class ServerTCPMultiClient
{
    public static void main(String[] args) throws IOException
    {
        int portNumber = 5555; // Default port to use

        if (args.length > 0)
        {
            if (args.length == 1)
                portNumber = Integer.parseInt(args[0]);
            else
            {
                System.err.println("Usage: java ServerMutiClients [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Connected to Multi-client TCP server.");
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("Server opened at: "+inetAddress.getHostAddress() + ": " + portNumber);
         try (
                // Create server socket with the given port number
                ServerSocket serverSocket =
                        new ServerSocket(portNumber);


          )
        {
            // continuously listening for clients
            while (true)
            {
                // create and start a new ClientServer thread for each connected client
                ClientService clientserver = new ClientService(serverSocket.accept());
                clientserver.start();
                System.out.println("New Client connected: " + clientserver.clientAddr.getLocalHost()+ ":" +
                        clientserver.clientPort + " ");

            }
        } catch (IOException e) {

            System.out.println("Exception occurred when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }

}
