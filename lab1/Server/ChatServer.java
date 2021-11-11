package lab1.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ChatServer {
    /* Server Connection Controller */
    private ServerController controller;

    public static void main(String[] args){
        /* instantiate a server */
        ChatServer server = new ChatServer();
        /* boost server */
        server.boost();
    }

    public ChatServer(){
        this.controller = new ServerController();
    }


    /**
     * loop to listen and accept connection request from client
     * Assign every client a thread
     */
    public void boost() {
        try {
            ServerSocket ss = new ServerSocket(controller.getPort());
            while (true){
                Socket clientSocket = ss.accept();
                /* spawn a thread for handling incoming message from client*/
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Server boost failure");
        }

    }

    /**
     * spawn a thread for handling a client socket
     * @param clientSocket new clientSocket accepted
     */
    private void startHandler (Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, controller.getLingerTime());
        clientSocket.setSoTimeout(controller.getTimeoutHalfHour());
        System.out.println("New client join!");
        ClientHandler handler = new ClientHandler(clientSocket, controller);
        //only one thread can manipulate the client list at same time
        controller.addClient(handler);
        controller.getCachedThreadPool().execute(handler);

    }
}
