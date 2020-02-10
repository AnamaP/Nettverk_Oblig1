package Nettverk;
import java.net.*;
import java.io.*;


public class ServerTCPMultiClient {

    public static void main(String[] args) throws IOException
    {
        int portNumber = 5555;

        if (args.length > 0)
        {
            if (args.length == 1)
                portNumber = Integer.parseInt(args[0]);
            else
            {
                System.err.println("Usage: java ServerMultiClients [<port number>]");
                System.exit(1);
            }
        }
        System.out.println("Connecting Multi-client TCP server.");
        InetAddress inetAddress = InetAddress.getLocalHost(); // kobler opp til selve nettet så klient og server finner hverandre
        System.out.println("Server opened at: "+inetAddress.getHostAddress() + ": " + portNumber);


         try (
                // Oppretter en server socket med det gitte port nummeret
                ServerSocket serverSocket =
                        new ServerSocket(portNumber);

          )
        {
            // Lytter kontinuerlig etter klienter
            while (true) {

                // Oppretter og starter en ny ClientServer tråd for hver tilkoblede klient
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
