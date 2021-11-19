package lab2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//http://localhost:8080/index.html
public class HttpServer {
    private static final int LINGER_TIME = 50000;
    private int portNo = 8080;
    private ExecutorService cachedThreadPool;
    private static int cookie = 0;
    private ArrayList<Game> games =  new ArrayList<Game>();


    public HttpServer(){
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    private void serve(){
        try {
            ServerSocket serverSocket = new ServerSocket(portNo);
            //loop for accepting connect request
            while(true){
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoLinger(true,LINGER_TIME);
                Thread handler = new Thread(new RequestHandler(clientSocket, games));
                cachedThreadPool.execute(handler);
                //handler.start();
            }
        } catch (IOException e) {
            System.out.println("HttpServer boost failure");
            e.printStackTrace();
        }
    }

    //update cookie
    public static void updateCookie(){
        cookie++;
    }

    //get the last cookie
    public  static int getCookie(){
        int returnCookie = cookie;
        updateCookie();

        return returnCookie;
    }
    public static void main(String[] args){
        HttpServer server = new HttpServer();
        server.serve();
    }
}
