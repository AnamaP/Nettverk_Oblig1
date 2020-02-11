package Nettverk;

import java.io.*;
import java.net.*;


import static Nettverk.HjelpeMetoder.emailPrinter;
import static Nettverk.HjelpeMetoder.messageDecoder;


public class ClientTCP {

    public static void main(String[] args) throws IOException {
        //Klienten tar inn server sin IP for å koble seg til
        String hostName = "10.253.31.239";

        //samme portnummer som serveren
        int portNumber = 5555;
          if (args.length > 0)
            {
               hostName = args[0];
               if (args.length > 1)
               {
                 portNumber = Integer.parseInt(args[1]);
                 if (args.length > 2)
                 {
                   System.err.println("Usage: java TCP.ClientTCP [<host name>] [<port number>]");
                   System.exit(1);
                 }
               }
            }


        System.out.println("Connecting TCP client!");

        //kobler seg til wifi
        InetAddress address = InetAddress.getByName(hostName);

        try
        (
            // oppretter en TCP socket med gitt hostName og portNummer
            Socket clientSocket = new Socket(address, portNumber);

            //Stream som skriver til socket
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);

            // Stream reader fra socket
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));

            //Input leser fra konsollen
            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in))
        )
        {
            String userInput;

            // Kjører gjennom inntil userInput er null
            System.out.print("I (Client) [" + InetAddress.getLocalHost()  + ":" + clientSocket.getLocalPort() + "] > ");


            while ((userInput = stdIn.readLine()) != null && !userInput.isEmpty()) {

                //Det som skrives i konsollen blir sendt til server.
                out.println(userInput);


                //Leser fra socket og viser receivedText;
                //ReceivedText er meldingen som kommer fra server.
                String receivedText = in.readLine();

                //Hvis receivedText ikke er null (at det er noe innhold)
                if(receivedText != null) {

                    String outMessage = emailPrinter(receivedText);

                    System.out.println("Server [" + hostName + ":" + portNumber + "] > " + outMessage);
                    System.out.print("I (Client) [" + clientSocket.getLocalAddress().getHostAddress() + ":" +
                            clientSocket.getLocalPort() + "] > ");
                }
                //Sender serveren null, vil disse feilmeldingene slå ut.
                else {
                    System.out.println(messageDecoder(2));
                    System.out.println("Restart your client");

                    //avslutter klienten
                    System.exit(1);
                }

            }
        }
        //Dette skjer hvis den ikke klarer å koble opp
        catch (UnknownHostException e)
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
