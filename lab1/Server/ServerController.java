package lab1.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    /* Server Name */
    private String servertName = "Server";
    /* Port */
    private static final int port = 8080;
    /* Thread Pool */
    private ExecutorService cachedThreadPool;
    private static final int LINGER_TIME = 5000;//5s
    private static final int TIMEOUT_HALF_HOUR = 1800000;

    private final ArrayList<ClientHandler> clients;

    public ServerController() {
        /* create a thread pool */
        this.cachedThreadPool = Executors.newCachedThreadPool();
        this.clients = new ArrayList<>();
    }


    public int getPort() {
        return port;
    }

    public int getLingerTime() {
        return LINGER_TIME;
    }

    public int getTimeoutHalfHour(){
        return TIMEOUT_HALF_HOUR;
    }

    public ExecutorService getCachedThreadPool(){
        return cachedThreadPool;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public synchronized void addClient(ClientHandler handler){
        clients.add(handler);
    }

    public synchronized void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }

    public void disconnect(ClientHandler handler){
        try {
            removeClient(handler);
            handler.clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String msg, int port){
        synchronized (clients){
            //clients.forEach((client)-> System.out.println(client.getToClient()));
            clients.forEach((client)-> {
                if(client.clientSocket.getPort() != port){
                    client.getToClient().println(msg);
                }
            });
        }
    }


}
