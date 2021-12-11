import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class IMAP {

    private boolean work = true;

    public static void main(String[] args){
        IMAP imap = new IMAP();
        imap.boot();
    }

    private void boot(){
        //configuration
        SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
        HttpsURLConnection.setDefaultSSLSocketFactory(sf);

        PrintWriter writer = null;
        BufferedReader reader = null;

        try {
            SSLSocket socket = (SSLSocket)sf.createSocket(User.imap_host,993);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new cmdParser(writer)).start();

            String msg = "";
            while(work && (msg = reader.readLine()) != null){
                System.out.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class cmdParser implements Runnable{
        //local variable for parsing
        private BufferedReader indata;
        private PrintWriter writer;

        public cmdParser(PrintWriter writer){
            this.writer = writer;
            this.indata = new BufferedReader(new InputStreamReader(System.in));
        }

        public void run(){
            System.out.println("Enter command");
            while(work){
                try {
                    String msg = indata.readLine();
                    if(msg.equals("logout")){
                        work = false;
                        logout(writer);
                    }else if(msg.equals("login")){
                        login(writer);
                    }else if(msg.equals("list")){
                        list(writer);
                    }else if(msg.equals("fetch")){
                        fetch(writer);
                    }
                    System.out.println("Enter command");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void login(PrintWriter writer){
        //login
        writer.println("a001 login " + User.username + " " + User.password);
        writer.flush();
    }

    public void list(PrintWriter writer){
        //list
        //The first argument (known as the “reference name”) indicates under what folder hierarchy you’d like to limit the list to
        //The second argument (known as the “mailbox name”) can contain wildcards to match names under the provided hierarchy.
        writer.println("a002 list \"\" \"*\"");
        writer.flush();
    }

    public void fetch(PrintWriter writer){
        writer.println("a003 select inbox");
        writer.flush();

        writer.println("a004 fetch 1 (FLAGS BODY[HEADER.FIELDS (DATE FROM TO)])");
//        writer.println("a004 fetch 1 (BODY[HEADER.FIELDS (DATE FROM TO)])");
//        writer.println("a004 fetch 1 FLAGS");
        writer.flush();

    }

    public void logout(PrintWriter writer){
        writer.println("a005 logout");
        writer.flush();
    }

}
