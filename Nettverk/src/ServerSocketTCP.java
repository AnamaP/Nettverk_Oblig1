import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTCP {

    public static void main(String[] args) throws IOException {
        int portNumber = 5555;

        if(args.length > 0 ){
            if(args.length == 1){
                portNumber = Integer.parseInt(args[0]);
            }
            else {
                System.err.println("feilmelding");
                System.exit(1);
            }
        }

        try (

            //Oppretter server socket
            ServerSocket serverSocket = new ServerSocket(portNumber);

            //Oppretter kontakten med server socketen.
            Socket connectSocket = serverSocket.accept();

            // Skriver til connection socket
            PrintWriter out = new PrintWriter(connectSocket.getOutputStream(), true);

            //Henter ut nettsiden sin URL og legger det inn som et objekt.
            //InputStreamReader konverterer fra bytes til bokstaver
            BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
        ) {
            // klassen representerer en IP adresse.
            InetAddress clientAdr = connectSocket.getInetAddress();

            //henter porten til klienten
            int clientPort = connectSocket.getPort();
            String receivedURL;

            while ((receivedURL = in.readLine())!=null){

                //if (receivedURL.matches())
                System.out.println("Client [" + clientAdr.getHostAddress() + " : " + clientPort + "] > "
                                    + receivedURL);
            }
        }


    }
}
