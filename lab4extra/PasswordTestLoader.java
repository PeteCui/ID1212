package lab4extra;

import jdk.jshell.execution.LoaderDelegate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class PasswordTestLoader {

    public static void main(String args[]){
        try {
            // load certificate
            String keystoreFilename = "C:\\Users\\pppp\\Desktop\\ID1212-NetProgramming\\Certificate\\zhanboRSA.keystore";
            char[] storepass = "zhanboRSA".toCharArray();
            char[] keypass = "zhanboRSA".toCharArray();
            String alias = "zhanborsa";
            FileInputStream fIn = new FileInputStream(keystoreFilename);
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(fIn, storepass);
            // display certificate
            Certificate cert = keystore.getCertificate(alias);
            System.out.println(cert);
            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keystore, keypass);
            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keystore);

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
