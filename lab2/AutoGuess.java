package lab2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.Duration;

public class AutoGuess {
    HttpClient client;
    public String myCookie;
    public int sum;
    public int min;
    public int max;
    public int finish;

    public AutoGuess(){
        this.sum = 0;
        this.min = 0;
        this.max = 100;
        this.finish= 0;
        //construct the client
        client = HttpClient.newHttpClient();

    }

    public void getCookie() throws IOException, InterruptedException {
        //construct get request
        HttpRequest firstRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/index.html"))
                .build();
        //send get request to get index.html
        HttpResponse<String> response = null;
        response = client.send(firstRequest, HttpResponse.BodyHandlers.ofString());
        //retrieve the cookie
        this.myCookie = response.headers().allValues("set-cookie").get(0);

    }

    private void startGame(int Guess, int count) throws IOException, InterruptedException {
        //construct post request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/index.html"))
                .timeout(Duration.ofSeconds(1))
                .header("set-cookie", this.myCookie)
                .POST(HttpRequest.BodyPublishers.ofString("number=" + Guess))
                .build();
        //send post request to guess
        HttpResponse<String> postResponse = null;
        try {
            postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            //I do not know why this way can reconnect the link
            HttpClient newClient = HttpClient.newHttpClient();
            postResponse = newClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        }

        //parse
        String[] split1 = postResponse.body().split("</h1>");
        if(split1[0].endsWith("lower")){
            this.min = Guess;
            int newGuess;
            int offset = (this.max - Guess);
            if(offset > 1){
                newGuess = Guess + offset/2;
            }else{
                newGuess = Guess + 1;
            }
            startGame(newGuess, count+1);
        }else if(split1[0].endsWith("higher")){
            this.max = Guess;
            int newGuess;
            int offset = (Guess - this.min);
            if(offset > 1){
                newGuess = Guess - offset/2;
            }else{
                newGuess = Guess - 1;
            }
            startGame(newGuess, count+1);
        }else{
//          //construct get request
            HttpRequest resetRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/index.html?"))
                    .header("set-cookie", this.myCookie)
                    .build();
            client.send(resetRequest, HttpResponse.BodyHandlers.ofString());
            //HttpResponse<String> resetResponse = null;
            //resetResponse = client.send(resetRequest, HttpResponse.BodyHandlers.ofString());
            //System.out.println(resetResponse.body());

            //reset guess
            this.min = 0;
            this.max = 100;
            this.finish++;
            this.sum = this.sum + count;
            System.out.println("Finish " + finish + " : " + count +" times" + " | Total times: " + this.sum);
            return;
        }
    }

    private void showResult() {
        float num= (float)this.sum/this.finish;
        DecimalFormat df = new DecimalFormat("0.00");
        String result = df.format(num);
        System.out.println("Average times: "+ result );
    }

    public static void main(String[] args) {
        try {

            AutoGuess autoGuess = new AutoGuess();
            //get cookie
            autoGuess.getCookie();
            for(int i = 0; i < 100; i++){
                autoGuess.startGame(50, 1);
            }
            autoGuess.showResult();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
