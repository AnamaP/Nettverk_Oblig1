package Nettverk;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HjelpeMetoder {

    public static String messageDecoder(int errorCode)throws IOException{
        String code = "";
        switch (errorCode) {
            case 0: {
                emailExtractor(code); // kaller metoden som formaterer e-posten
                break;
            }

            case 1: {
                code += "!!No email address found on the page!!!,";
                break;
            }

            case 2: {
                code+= "!!Server couldn’t find the web page!!!,";
                break;
            }
        }

        return code;
    }

    //Metode som tar inn input fra klienten og gjør den om til en URL.
    public static URL urlReciever(String clientInput){
       URL url = null;

        try {
            url = new URL("https://" + clientInput);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //Åpner en kobling til nettsiden.
    public static URLConnection openConnection(URL url) throws IOException {
       URLConnection con =  url.openConnection();

       return con;
    }

    //Tar informasjon som er hentet fra URL og gjør den om til en stream, slik at den kan leses av
    //emailExtractor.
    public static InputStream inputStream (URLConnection con) throws IOException {
        InputStream stream = con.getInputStream();
        return stream;

    }

    public static ArrayList<String> emailExtractor(String input) throws IOException {

        InputStream stream = inputStream(openConnection(urlReciever(input)));

        String content = "";
        int i;

        //Legger alle tegn på nettsiden inn i en String
        while((i=stream.read())!=-1){
            try {
                content += (char)i;
            }
            catch(Exception e){
                System.out.println("DET SKJEDDE NOE FEIL");
            }
        }

        ArrayList<String> containedEmails = new ArrayList<>();

        // Regex for å fine email.
        Pattern regex = Pattern.compile(
                "([A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6})",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

        //Sjekker om vi finner noen match.
        Matcher emailMatcher = regex.matcher(content);

        //Legger emails inn i en liste, dersom de ikke er der fra før
        while(emailMatcher.find()){
            String enEmail = content.substring(emailMatcher.start(0),emailMatcher.end(0));
            if(!containedEmails.contains(enEmail)){
                containedEmails.add(enEmail);
            }
        }

        return containedEmails;

    }

    //Metode som formaterer måten emails skal bli printet ut på.
    public static String emailPrinter(String receivedText) throws IOException {
        String emails = "";

        if(receivedText != null) {
            String [] split = receivedText.split(",");

            for (int i = 0; i < split.length; i++) {

                if (i == split.length - 1) {
                    emails += split[i];
                } else {
                    emails += split[i] + " \n";
                }
            }
        }
        else {
            emails += messageDecoder(2);
        }
        return emails;
    }

}
