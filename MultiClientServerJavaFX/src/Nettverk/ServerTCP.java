package Nettverk;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

import static Nettverk.ClientTCP.message;
import static Nettverk.HjelpeMetoder.*;

public class ServerTCP {


    public static void main(String[] args) throws IOException
    {
        int portNumber = 5555; // Default port to use

        if (args.length > 0)
        {
            if (args.length == 1)
                portNumber = Integer.parseInt(args[0]);
            else
            {
                System.err.println("Usage: java TCP.EchoUcaseServerTCP [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi, I am EchoUCase TCP server");

        // try() with resource makes sure that all the resources are automatically
        // closed whether there is any exception or not!!!
        try (
                // Create server socket with the given port number
                ServerSocket serverSocket =
                        new ServerSocket(portNumber);
                // create connection socket, server begins listening
                // for incoming TCP requests
                Socket connectSocket = serverSocket.accept();

                // Stream writer to the connection socket
                PrintWriter out =
                        new PrintWriter(connectSocket.getOutputStream(), true);

                // Stream reader from the connection socket
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connectSocket.getInputStream()));
        )
        {
            InetAddress clientAddr = connectSocket.getInetAddress();
            int clientPort = connectSocket.getPort();

            String receivedText;


            // read from the connection socket
            while ((receivedText = in.readLine())!=null)
            {

                //Åpner en kobling mellom server og webside
                openConnection(urlReciever(receivedText));

                System.out.println("Client [" + clientAddr.getHostAddress() +  ":" + clientPort +"] > " + receivedText);


                //Oppretter en ArrayListe som tar imot alle emails.
                ArrayList<String> containedEmails  = emailExtractor(receivedText);


                if(containedEmails.isEmpty()){
                    //out.println(1);
                    out.println(message(1));
                }
                else{
                    //skriver emails til client
                    out.println(message(0)+","+containedEmails.toString());
                }

                System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" +
                            portNumber + "] > " + containedEmails.toString());

            }

            System.out.println("I am done, Bye!");
        }

        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
