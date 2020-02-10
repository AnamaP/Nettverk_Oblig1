package Nettverk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import static Nettverk.HjelpeMetoder.*;
import static Nettverk.HjelpeMetoder.messageDecoder;

/***
 * This class serves a client in a separate thread
 */
public class ClientService extends Thread {
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
