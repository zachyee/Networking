import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Zachary on 6/18/2017.
 *
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoServer.java
 */
public class EchoServer {
    public static void main(String[]args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port_number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = socketIn.readLine()) != null) {
                System.out.println("Client: " + inputLine);
                System.out.println("Echoing to client...");
                System.out.println("Server: " + inputLine);
                socketOut.println(inputLine);
            }
        }
        catch (IOException e) {
            System.err.println(e.getStackTrace());
            System.err.println("Exception while listening to port " + portNumber);
        }
    }
}
