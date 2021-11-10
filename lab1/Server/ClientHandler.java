package Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable{
    public Socket clientSocket;
    private ServerController controller;
    private BufferedReader fromClient;
    private PrintStream toClient;
    public volatile boolean connected;

    public ClientHandler(Socket clientSocket , ServerController controller) {
        this.clientSocket = clientSocket;
        this.controller = controller;
        try {
            this.fromClient = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
            this.toClient = new PrintStream( clientSocket.getOutputStream());
            this.connected = true;
        } catch (IOException e) {
            System.out.println("client socket initialize failure");
        }
    }

    public PrintStream getToClient(){
        return toClient;
    }

    @Override
    public void run() {
        while(connected){
            try {
                String msg = fromClient.readLine();
                //It means client quit! When he quits the buffer flushed!
                if(msg == null){
                    System.out.println(clientSocket.getInetAddress() + " quit!");
                    controller.disconnect(this);
                    break;
                }else{
                    controller.broadcast(msg);
                }
            } catch (Exception e) {
                System.out.println("One client lose connection");
                controller.disconnect(this);
                break;
            }
        }
    }

}
