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

    public static URL urlReciever(String clientInput){
       URL url = null;

        try {
            url = new URL("https://" + clientInput);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URLConnection openConnection(URL url) throws IOException {
       URLConnection con =  url.openConnection();

       return con;
    }

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

        Pattern regex = Pattern.compile(
                "([A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6})",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

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

}
