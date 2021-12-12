package lab4extra;

import lab2.Game;
import lab2.RequestHandler;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//https://localhost:8080/index.html
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
            // load certificate
            String keystoreFilename = "C:\\Users\\pppp\\Desktop\\ID1212-NetProgramming\\Certificate\\zhanboRSA.keystore";
            char[] storepass = "zhanboRSA".toCharArray();
            char[] keypass = "zhanboRSA".toCharArray();
            String alias = "alias";
            FileInputStream fIn = new FileInputStream(keystoreFilename);
            //Returns a keystore object of the specified type. Here is the "JKS" keystore object.
            KeyStore keystore = KeyStore.getInstance("JKS", "SUN");
            //Loads this KeyStore from the given input stream
            keystore.load(fIn, storepass);
            // set up the key manager factory
            //KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keystore, keypass);
            kmf.init(keystore, keypass);
            // set up the trust manager factory
            //TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            tmf.init(keystore);
            // create ssl context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // set up the HTTPS context and parameters
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            //sslContext.init(kmf.getKeyManagers(), null, null);
            //set up the server socket factory
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket)ssf.createServerSocket(portNo);
            //ServerSocket serverSocket = new ServerSocket(portNo);

            //loop for accepting connect request
            while(true){
                //Socket clientSocket = serverSocket.accept();
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                clientSocket.setSoLinger(true,LINGER_TIME);
                Thread handler = new Thread(new SSLRequestHandler(clientSocket, games));
                cachedThreadPool.execute(handler);
                //handler.start();
            }
        } catch (IOException | KeyStoreException e) {
            System.out.println("HttpServer boost failure");
        } catch (CertificateException e) {
            System.out.println("haha zhanbo?!");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("haha zhanbo?!");
        } catch (UnrecoverableKeyException e) {
            System.out.println("haha zhanbo?!");
        } catch (KeyManagementException e) {
            System.out.println("haha zhanbo?!");
        } catch (NoSuchProviderException e) {
            System.out.println("haha zhanbo?!");
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
