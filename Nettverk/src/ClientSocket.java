/* Shall provide following functionalities:

- TCP server provides an email extraction service to the client.
    - oWhen run, it waits for a client’s query.
    
- TCP client gets an input query (web page/URL) from the user and sends it to the server.

- TCP  server  receives  the  query  and  respond  to  the  client,  depending  on  whether
  it  finds  email addresses on the web page or not.
   - The server looks for the email addresses on the web page and if it finds, extracts theemail addresses, and returns to the client.
   - The first character in the response message should be one of the three code numbers 0, 1, or 2,
     depending on the three different  possible  scenarios  such  as  the  web  page  exists and contains emails,
     the web page exists but contains no email, or any problem with the web page (possibly invalid URL,
     page doesn’t exist, ...) respectively.

- The   client   decodes   the   response   message   from   the   server   and   print   information
  depending on the return code as follows:
     - Code 0: print the emails one per line.
     - Code 1: print ‘!!!No email address found on the page!!!’.
     - Code 2: print ‘!!!Server couldn’t find the web page!!!
*/

public class ClientSocket {
    //test
}
