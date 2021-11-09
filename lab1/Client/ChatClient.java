package Client;

public class ChatClient {
    /* Connection Controller */
    private ClientNetController controller;

    public static void main(String[] args) {
        /* instantiate a client */
        ChatClient client = new ChatClient();
        /* boost client */
        client.boost();
    }

    public ChatClient(){
        this.controller = new ClientNetController();
    }

    public void boost(){
        /* welcome */
        System.out.println("Hej! " + controller.getClientName());
        /* init connection */
        controller.connect();
        /* spawn a thread for handling user input */
        new Thread(new CmdParser(controller)).start();
    }

}
