package Nettverk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static Nettverk.HjelpeMetoder.*;
import static Nettverk.HjelpeMetoder.messageDecoder;

/***
 * Denne klassen tjener klienten i en separat tråd
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
                // Oppretter server socket med det gitte port nummeret
                PrintWriter out =
                        new PrintWriter(connectSocket.getOutputStream(), true);
                // Stream reader fra tilkobling socket
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connectSocket.getInputStream()));
        ) {

            String receivedText;
            // leser fra tilkoblings socket
            boolean connected = true;

            while (connected) {

                try {

                    receivedText = in.readLine();
                    if(receivedText != null) {
                        //Åpner en kobling mellom server og webside
                        openConnection(urlReciever(receivedText));

                        System.out.println("Client [" + clientAddr.getHostAddress() + ":" + clientPort + "] > " + receivedText);

                        // Oppretter en ArrayListe som tar i mot alle emails og bruker hjelpemetoden for å legge de inn
                        ArrayList<String> containedEmails = emailExtractor(receivedText);


                        if (containedEmails.isEmpty()) {
                            //Fant ingen emails --> gi beskjed til klienten.
                            out.println(messageDecoder(1));
                        } else {
                            //skriver emails til client
                            out.println(containedEmails.toString());
                        }

                        System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() +
                                ":" + serverPort + "] > " + containedEmails.toString());
                    }
                    else {
                        System.out.println("Client with IP:"  +connectSocket.getLocalAddress().getHostAddress()+ " - Disconnected!");
                        connected = false;
                    }
                }
                catch (IOException e){
                    System.out.println("Blalbla");
                }

                }


                // lukker leser, skriver og socket forbindelsen
                connectSocket.close();

        }
        catch (IOException e) {
            System.out.println("Exception occurred when trying to communicate with the client "
                    + clientAddr.getHostAddress());
            System.out.println(e.getMessage());
        }
    }

}
