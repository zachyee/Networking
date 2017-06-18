import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Zachary on 6/18/2017.
 *
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoClient.java
 */
public class EchoClient {
    public static void printException(Exception e, String message) {
        System.err.println(e.getStackTrace());
        System.err.println(message);
        System.exit(1);
    }

    public static void promptUser() {
        System.out.println("Enter a line to send to the server: ");
        System.out.print("Client: ");
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java EchoClient <host_name> <port_number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter socketOut = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            promptUser();
            while ((userInput = stdIn.readLine()) != null) {
                socketOut.println(userInput);
                System.out.println("Server: " + socketIn.readLine());
                promptUser();
            }
        }
        catch (UnknownHostException e) {
            printException(e, "IP address of host (" + hostName + ") could not be determined.");
        }
        catch (IOException e) {
            printException(e, "Could not create socket");
        }
    }
}