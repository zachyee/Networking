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
        int connectionNumber;

        public ConnectionHandler(Socket clientSocket, int connectionNumber) {
            this.clientSocket = clientSocket;
            this.connectionNumber = connectionNumber;
        }

        public void run() {
            try {
                PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = socketIn.readLine()) != null) {
                    System.out.println("Client " + connectionNumber + ": " + inputLine);
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

    private static final int maxConnections = 10;
    private static int numberConnections = 0;
    private static final Object lock = new Object();

    private static void incrementConnectionNumber() {
        synchronized (lock) {
            numberConnections = (numberConnections + 1) % maxConnections;
        }
    }

    private static int getConnectionNumber() {
        synchronized (lock) {
            return numberConnections;
        }
    }

    public static void main(String[]args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java MultiEchoServer <port_number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(portNumber);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                incrementConnectionNumber();
                ConnectionHandler connectionHandler = new MultiEchoServer().new ConnectionHandler(clientSocket,
                        getConnectionNumber());
                Thread clientThread = new Thread(connectionHandler);
                clientThread.start();
            }
        }
        finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
