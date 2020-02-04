package TCP; /**
 * Socket programming example: TCP Server
 * DATA410 Networking and Cloud Computing, Spring 2020
 * Raju Shrestha, OsloMet
 **/

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EchoUcaseServerTCP
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
                URL url = new URL("https://" + receivedText);
                System.out.println(url);

                System.out.println("Client [" + clientAddr.getHostAddress() +  ":" + clientPort +"] > " + receivedText);

                // input stream som kommer fra URL, henter ut all informasjon fra siden i html format.
                InputStream in1 = url.openStream();

                //Leser innholdet fra input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(in1));

                // leser hver linje
                String outText = reader.readLine();

                URLConnection urlcon=url.openConnection();
                InputStream stream=urlcon.getInputStream();
                int i;

                while((i=stream.read())!=-1){
                    List<String> containedEmails = new ArrayList<>();

                    Pattern regex = Pattern.compile("((http?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",
                            //"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                                                Pattern.CASE_INSENSITIVE);

                    out.println(outText);

                    Matcher emailMatcher = regex.matcher(outText);
                    while(emailMatcher.find()){
                        containedEmails.add(outText.substring(emailMatcher.start(0),emailMatcher.end(0)));
                    }
                    for(String email : containedEmails){
                        System.out.println("Emails:" + email);
                    }

                    //System.out.println(containedEmails);

                    System.out.print((char)i);
                }


                // en test for å sjekke om den finner ordet velkommen - fungerer, men var bare en test

                    // printer ut innholdet fra siden



                System.out.println("Protocol: "+url.getProtocol());
                System.out.println("Host Name: "+url.getHost());
                System.out.println("Port Number: "+url.getPort());
                System.out.println("Default Port Number: "+url.getDefaultPort());
                System.out.println("Query String: "+url.getQuery());
                System.out.println("Path: "+url.getPath());
                System.out.println("File: "+url.getFile());

                System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" +
                            portNumber + "] > " + outText);




            }

            System.out.println("I am done, Bye!");
        } catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
