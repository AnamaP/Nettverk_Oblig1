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
                System.err.println("Usage: java EchoUcaseServerMutiClients [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi, I am the EchoUCase Multi-client TCP server.");
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


    /***
     * This class serves a client in a separate thread
     */
    public static class ClientService extends Thread
    {
        Socket connectSocket;
        InetAddress clientAddr;
        int serverPort, clientPort;

        public ClientService(Socket connectSocket)
        {
            this.connectSocket = connectSocket;
            clientAddr = connectSocket.getInetAddress();
            clientPort = connectSocket.getPort();
            serverPort = connectSocket.getLocalPort();
        }

        public void run()
        {
            try (
                    // Create server socket with the given port number
                    PrintWriter out =
                            new PrintWriter(connectSocket.getOutputStream(), true);
                    // Stream reader from the connection socket
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connectSocket.getInputStream()));
            ) {

                String receivedText;
                // read from the connection socket
                while (((receivedText = in.readLine()) != null))
                {
                    //Åpner en kobling mellom server og webside
                    openConnection(urlReciever(receivedText));

                    System.out.println("Client [" + clientAddr.getHostAddress() +  ":" + clientPort +"] > " + receivedText);

                    // Oppretter en ArrayListe som tar i mot alle emails og bruker hjelpemetoden for å legge de inn
                    ArrayList<String> containedEmails  = emailExtractor(receivedText);


                    if(containedEmails.isEmpty()){
                        //Fant ingen emails --> gi beskjed til klienten.
                        out.println(messageDecoder(1));
                    }
                    else{
                        //skriver emails til client
                        out.println(containedEmails.toString());
                    }


                    System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() +
                            ":" + serverPort +"] > " + containedEmails.toString());
                }


                // close the connection socket
                connectSocket.close();

            }
            catch (IOException e) {
                System.out.println("Exception occurred when trying to communicate with the client "
                        + clientAddr.getHostAddress());
                System.out.println(e.getMessage());
            }
        }

    }
}
