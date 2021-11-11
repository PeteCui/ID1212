package lab1.Client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientNetController {
    /* Client Name */
    private String clientName = "Anonymous";
    /* IP address*/
    private static String host = "localhost";
    /* Port */
    private static final int port = 8080;

    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private PrintStream toServer;
    private BufferedReader fromServer;
    public volatile boolean connected;


    /**
     * Initiate the connection between client and server
     */
    public void connect(){
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
            socket.setSoTimeout(TIMEOUT_HALF_HOUR);
            connected = true;
            boolean autoFlush = true;
            /* client -> server */
            toServer = new PrintStream(socket.getOutputStream(), autoFlush);
            /* server -> client */
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Server connected!");
            /* spawn a thread for listening incoming message */
            if(connected){
                startListen();
            }else{
                System.out.println("Try to connect to the server");
            }
        } catch (IOException e) {
            System.out.println("Connection failure");
        }

    }

    /**
     * Close the connection between client and server
     */
    public void disconnect(){
        try {
            socket.close();
            connected = false;
            System.out.println("Disconnect safely");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start a thread for listening incoming message
     */
    public void sendMsg(String msg) {
        Sender sender = new Sender(toServer, clientName+" : " + msg);
        new Thread(sender).start();
    }

    /**
     * Start a thread for listening incoming message
     */
    public void startListen() {
        Listener listener = new Listener(fromServer);
        new Thread(listener).start();
    }

    /**
     * get client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * set client name
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
        System.out.println("Name is changed to " + this.clientName);
    }

    /**
     * listen to incoming message and print to console
     */
    private class Listener implements Runnable {
        private BufferedReader fromServer;
        private String msg = "";

        public Listener(BufferedReader fromServer) {
            this.fromServer = fromServer;
        }

        @Override
        public void run() {
            try {
                while (connected && (msg = fromServer.readLine()) != null) {
                    System.out.println(msg);
                }
                System.out.println("Listener thread terminated");
            } catch (IOException e) {
                System.out.println("Listener thread terminated");
            }
        }

    }

    /**
     * send message from console to server
     */
    public class Sender implements Runnable{
        private PrintStream toServer;
        private String msg;

        public Sender(PrintStream toServer, String msg) {
            this.toServer = toServer;
            this.msg = msg;
        }

        @Override
        public void run() {
            if(connected){
                toServer.println(msg);
            }else{
                System.out.println("Please reconnect to server!");
            }
        }
    }


}
