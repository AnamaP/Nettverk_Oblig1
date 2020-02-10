package Nettverk;

import java.io.*;
import java.net.*;


import static Nettverk.HjelpeMetoder.emailPrinter;
import static Nettverk.HjelpeMetoder.messageDecoder;


public class TestClientTCP {

    public static void main(String[] args) throws IOException
    {

        String hostName = "127.0.0.1";
        int portNumber = 5555;
        if (args.length > 0)
        {
            hostName = args[0];
            if (args.length > 1)
            {
                portNumber = Integer.parseInt(args[1]);
                if (args.length > 2)
                {
                    System.err.println("Usage: java TCP.TestClientTCP [<host name>] [<port number>]");
                    System.exit(1);
                }
            }
        }


        System.out.println("Connecting test TCP client!");

        InetAddress address = InetAddress.getByName(hostName);

        try
                (
                        // create TCP socket for the given hostName, remote port PortNumber
                        Socket clientSocket = new Socket(address, portNumber);

                        // Stream writer to the socket
                        PrintWriter out =
                                new PrintWriter(clientSocket.getOutputStream(), true);

                        // Stream reader from the socket
                        BufferedReader in =
                                new BufferedReader(
                                        new InputStreamReader(clientSocket.getInputStream()));

                        // Keyboard input reader
                        BufferedReader stdIn =
                                new BufferedReader(
                                        new InputStreamReader(System.in))
                )
        {
            String userInput;

            // Loop until null input string
            System.out.print("I (Client) [" + InetAddress.getLocalHost()  + ":" + clientSocket.getLocalPort() + "] > ");


            while ((userInput = stdIn.readLine()) != null && !userInput.isEmpty()) {
                // write keyboard input to the socket
                out.println(userInput);

                // read from the socket and display
                String receivedText = in.readLine();

                if(receivedText != null) {

                    String outMessage = emailPrinter(receivedText);

                    System.out.println("Server [" + hostName + ":" + portNumber + "] > " + outMessage);
                    System.out.print("I (Client) [" + clientSocket.getLocalAddress().getHostAddress() + ":" +
                            clientSocket.getLocalPort() + "] > ");
                }
                else {
                    System.out.println(messageDecoder(2));
                    System.out.println("Restart your client");
                    System.exit(1);
                }

            }
        } catch (UnknownHostException e)
        {
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}