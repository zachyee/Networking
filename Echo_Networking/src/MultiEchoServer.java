import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Zachary on 6/18/2017.
 *
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoServer.java
 */
public class MultiEchoServer {
    private class ConnectionHandler implements Runnable {
        Socket clientSocket;

        public ConnectionHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
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
                System.err.println("Exception while listening to port " + clientSocket.getPort());
            }
        }
    }

    public static void main(String[]args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java MultiEchoServer <port_number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(portNumber);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ConnectionHandler connectionHandler = new MultiEchoServer().new ConnectionHandler(clientSocket);
            Thread clientThread = new Thread(connectionHandler);
            clientThread.start();
        }
    }
}
