package Nettverk;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerTCP {

    public static String message(String errorCode){
        String code = "";
        switch (errorCode) {
            case "Code 0": {
                code += "print the emails one per line.";
                System.out.println(code);
                break;
            }

            case "Code 1": {
                code += "!!No email address found on the page!!!";
                System.out.println(code);
                break;
            }
        }

        return code;
    }
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

                System.out.println("Client [" + clientAddr.getHostAddress() +  ":" + clientPort +"] > " + receivedText);


                URLConnection urlcon=url.openConnection();
                InputStream stream=urlcon.getInputStream();
                int i;

                String pageContent = "";

                //Legger alle tegn på nettsiden inn i en String
                while((i=stream.read())!=-1){
                    try {
                        pageContent += (char)i;
                    }
                    catch(Exception e){
                        System.out.println("DET SKJEDDE NOE FEIL");
                    }
                }
                //leter etter emails som matcher regex'n under, i Stringen som blir lagd over
                ArrayList<String> containedEmails = new ArrayList<>();

                Pattern regex = Pattern.compile(
                        "([A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6})",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

                Matcher emailMatcher = regex.matcher(pageContent);

                //Legger emails inn i en liste, dersom de ikke er der fra før
                while(emailMatcher.find()){
                    String enEmail = pageContent.substring(emailMatcher.start(0),emailMatcher.end(0));
                        if(!containedEmails.contains(enEmail)){
                            containedEmails.add(enEmail);
                        }

                    }


                if(containedEmails.isEmpty()){
                    String code = "Code 1";
                    out.println(message(code));

                }
                else{
                    //skriver emails til client
                    String code = "Code 0";
                    out.println(message(code) + containedEmails.toString());

                }






                /*System.out.println("Protocol: "+url.getProtocol());
                System.out.println("Host Name: "+url.getHost());
                System.out.println("Port Number: "+url.getPort());
                System.out.println("Default Port Number: "+url.getDefaultPort());
                System.out.println("Query String: "+url.getQuery());
                System.out.println("Path: "+url.getPath());
                System.out.println("File: "+url.getFile());
                 */

                System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" +
                            portNumber + "] > " + containedEmails.toString());




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
