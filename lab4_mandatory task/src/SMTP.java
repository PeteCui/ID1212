import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

public class SMTP {

    public static void main(String args[]) {
        SMTP smtp = new SMTP();
        smtp.boot();
    }

    public void boot(){
        //configuration
        Socket socket = null;

        try {
            socket = new Socket(User.smtp_host, 587);


            cmdParser parse = new cmdParser(socket);
            new Thread(parse).start();
            Listener_1 listener = new Listener_1(socket);
            new Thread(listener).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class cmdParser implements Runnable{

        private BufferedReader indata;
        private Socket socket;
        private PrintWriter writer;
        PrintWriter sslWriter = null;
        BufferedReader sslReader = null;
        private boolean phase_1 = true;
        private boolean phase_2 = true;

        public cmdParser(Socket socket){
            try {
                this.socket = socket;
                this.indata = new BufferedReader(new InputStreamReader(System.in));
                this.writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            System.out.println("Enter command");
            while(phase_1){
                try {
                    String msg = indata.readLine();
                    if(msg.equals("ehlo")){
                        System.out.println("C: EHLO smtp.kth.se");
                        writer.println("EHLO smtp.kth.se");
                        writer.flush();
                    }else if(msg.equals("tls")) {
                        System.out.println("C: STARTTLS");
                        writer.println("STARTTLS");
                        writer.flush();
                        phase_1 = false;
                    }
                    System.out.println("Enter command");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket sslSocket = null;
                sslSocket = (SSLSocket) ssf.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);

                sslWriter = new PrintWriter(sslSocket.getOutputStream(), true);
                sslReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread(new Listener_2(sslReader)).start();

            while(phase_2){
                try {
                    String msg = indata.readLine();
                    if(msg.equals("ehlo")){
                        System.out.println("C: EHLO smtp.kth.se");
                        sslWriter.println("EHLO smtp.kth.se");
                        writer.flush();
                    }else if(msg.equals("login")) {
                        System.out.println("C: AUTH LOGIN");
                        sslWriter.println("AUTH LOGIN");
                        String username = Base64.getEncoder().encodeToString(User.username.getBytes());
                        sslWriter.println(username);
                        System.out.println("username: " + username);
                        sslWriter.flush();
                        String password = Base64.getEncoder().encodeToString(User.password.getBytes());
                        sslWriter.println(password);
                        System.out.println("password: " + password);
                        sslWriter.flush();
                    }else if(msg.equals("write")){
                        System.out.println("C: MAIL FROM:<zhanbo@kth.se>");
                        sslWriter.println("MAIL FROM:<zhanbo@kth.se>");
                        writer.flush();

                        System.out.println("C: RCPT TO:<zhanbo@kth.se>");
                        sslWriter.println("RCPT TO:<zhanbo@kth.se>");
                        writer.flush();

                        System.out.println("C: DATA");
                        sslWriter.println("DATA");
                        writer.flush();

                        //If no delay is set, the server will report an error
                        Thread.sleep(2000);
                        sslWriter.println("Test Message\\r\\n");
                        System.out.println("C: Test Message\\r\\n");
                        writer.flush();

                        Thread.sleep(2000);
                        sslWriter.println(".");
                        System.out.println("C: .");
                        writer.flush();

                        Thread.sleep(2000);
                        System.out.println("Finish!");

                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public class Listener_1 implements Runnable{

        private Socket socket;
        private BufferedReader reader = null;
        private boolean phase_1 = true;
        private boolean phase_2 = true;

        public Listener_1(Socket socket){
            this.socket = socket;
        }

        public void run(){

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg = "";
            try {
                //phase 1
                while (true) {
                    if ((msg = reader.readLine()) != null)
                        System.out.println(msg);
                    if (msg.contains("220 2.0.0"))
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Listener_2 implements Runnable{

        private BufferedReader reader = null;
        private boolean phase_1 = true;
        private boolean phase_2 = true;

        public Listener_2(BufferedReader reader){
            this.reader = reader;
        }

        public void run(){


            String msg = "";
            try {
                //phase 1
                while (true) {
                    if ((msg = reader.readLine()) != null)
                        System.out.println(msg);
                    if (msg.contains("250 2.0.0 Ok"))
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}