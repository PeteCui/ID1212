package Client;

import Client.ClientNetController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class CmdParser implements Runnable{
    private ClientNetController controller;
    private boolean receivingCmds;

    public CmdParser(ClientNetController controller) {
        this.controller = controller;
        this.receivingCmds = true;
    }

    @Override
    public void run() {
        System.out.println("Enter text to send: ");
        //local variable for parsing
        BufferedReader indata = new BufferedReader(new InputStreamReader(System.in));
        String msg = "";
        String NewName = "Anonymous";
        //loop
        while(receivingCmds) {
            try {
                msg = indata.readLine();
                //System.out.println(controller.connected);
                if (msg.equals("-quit")) {
                    receivingCmds = false;
                    /* spawn a thread for sending notification */
                    if (controller.connected) {
                        //controller.sendNotification(msg);
                        controller.disconnect();
                    }
                } else if (msg.equals("-name")) {
                    System.out.println("Enter name: ");
                    NewName = indata.readLine();
                    controller.setClientName(NewName);
                } else if (msg.equals("-connect")) {
                    controller.connect();
                } else {
                    /* spawn a thread for sending message */
                    controller.sendMsg(msg);
                }
            } catch (IOException e) {
                System.out.println("Parser failure");
            }
        }
    }

}
