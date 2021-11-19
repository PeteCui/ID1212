package lab2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler implements Runnable {
    private static final String SERVER_ID_HEADER = "Server: game 1.0";
    private static final String HTTP_GET_METHOD = "GET";
    private static final String HTTP_POST_METHOD = "POST";
    private static final String HTTP_OK_RESPONSE = "HTTP/1.1 200 OK";
    private static final String NOT_FOUND_RESPONSE = "HTTP/1.1 404 HTML Not Found";
    private final Socket clientSocket;
    private BufferedReader fromClient;
    private PrintStream toClient;
    private final ArrayList<Game> games;
    private Game myGame;

    public RequestHandler(Socket clientSocket, ArrayList<Game> games) {
        this.clientSocket = clientSocket;
        this.games = games;
        try {
            this.fromClient = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
            this.toClient = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            HttpRequestHeader requestHeader = readRequestHeader();
            if (requestHeader == null){
                disconnect();
                return;
            }

            //Handle http "get" request!
            if (requestHeader.httpMethod.equals(HTTP_GET_METHOD)){
                handleGetRequest(requestHeader);
                disconnect();
            }

            //Handle http "post" request!
            else if(requestHeader.httpMethod.equals(HTTP_POST_METHOD)){
                handlePostRequest(requestHeader, getPostRequestBody(requestHeader));
                disconnect();
            }else{
                System.out.println("Can not parse this request!");
                toClient.println(NOT_FOUND_RESPONSE);
                disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            disconnect();
        }
    }

    private void disconnect() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpRequestHeader readRequestHeader() throws IOException{
        String requestLine = fromClient.readLine();
        //System.out.println("First line of request:" + requestLine);
        //if the first line is null, return null
        if (requestLine == null){
            return null;
        }
        //[0]: GET [1]: /index.html [2]: HTTP/1.1
        //[0]: GET [1]: /favicon.ico [2]: HTTP/1.1
        String[] requestTokens = requestLine.split(" ");
        HttpRequestHeader requestHeader = new HttpRequestHeader(requestTokens[0], requestTokens[1], requestTokens[2]);

        //readLine() will cause block!
        while ( (requestLine = fromClient.readLine()) != null && !requestLine.trim().equals("")){
            requestHeader.addHeader(requestLine);
        }
        return requestHeader;
    }

    //this method return the data to browser and create a game for this a new cookie
    private void handleGetRequest(HttpRequestHeader requestHeader) {

        //New Game
        if (requestHeader.path.endsWith("?")){
            requestHeader.path = requestHeader.path.substring(0,requestHeader.path.length()-1);
        }
        //response
        //byte[] fileContent =  readFile(requestHeader.path.substring(1));
        if (requestHeader.httpVersion.startsWith("HTTP/")){
            //"HTTP/1.1 200 OK"
            toClient.println(HTTP_OK_RESPONSE);
            //"Server: game 1.0"
            toClient.println(SERVER_ID_HEADER);
            //toClient.println("Content-length: " + fileContent.length);
            toClient.println("Content-length: " + HTML.INDEX.length());
            toClient.println("Content-type: " + "text/html");
            //set a cookie to browser if the client did not store the cookie
            if(!"/favicon.ico".equals(requestHeader.path)){
                if(requestHeader.cookie.equals("")) {
                    //create unique game
                    int gameId = HttpServer.getCookie();
                    Game newGame = new Game(gameId);
                    games.add(newGame);
                    System.out.println("New Client: " + newGame.getCookie());
                    System.out.println("Answer: " + newGame.getAns());
                    //return the cookie to client
                    toClient.println("Set-Cookie: clientId=" + gameId);
                }else{
                    myGame = findCurrentGame(requestHeader.cookie);
                    System.out.println("Old Client: " + myGame.getCookie());
                    System.out.println("Answer: " + myGame.getAns());
                }
            }
            //"/n"
            toClient.println();
            toClient.flush();
        }
        //toClient.write(fileContent);
        toClient.println(HTML.INDEX);

    }

    //Although we have post request header now, we need to get the request body as well!
    private String getPostRequestBody(HttpRequestHeader requestHeader) {
        StringBuilder sb = new StringBuilder();
        //read data from request body!
        int len = Integer.parseInt(requestHeader.contentLength);
        try {
            for (int i = 0; i < len; i++) {
                sb.append((char) fromClient.read());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Test Request body:"+ sb);
        String body = sb.toString();
        return body.split("=")[1];
    }

    private void handlePostRequest(HttpRequestHeader requestHeader, String postValue) {
        //continue to guess
        int guessNumber = Integer.parseInt(postValue);
        //System.out.println("My guess " + guessNumber);
        myGame = findCurrentGame(requestHeader.cookie);

        switch(myGame.guess(guessNumber)){
            //YES
            case 0:
                //System.out.println("YES!!!!");
                responsePostRequest(HTML.HEAD_AND_STYLE + HTML.YES1 + myGame.getCont() + HTML.YES2);
                myGame.update();
                break;
            //LOWER
            case 1:
                //System.out.println("LOWER!!!!");
                responsePostRequest(HTML.HEAD_AND_STYLE + HTML.LOWER1 + myGame.getCont() + HTML.LOWER2);
                break;
            //HIGHER
            case 2:
                //System.out.println("HIGHER!!!!");
                responsePostRequest(HTML.HEAD_AND_STYLE + HTML.HIGHER1 + myGame.getCont() + HTML.HIGHER2);
                break;
        }

    }

    private void responsePostRequest(String html) {
        //"HTTP/1.1 200 OK"
        toClient.println(HTTP_OK_RESPONSE);
        //"Server: game 1.0"
        toClient.println(SERVER_ID_HEADER);
        toClient.println("Content-length: " + html.length());
        toClient.println("Content-type: " + "text/html");
        //"/r/n"
        toClient.println();
        toClient.flush();
        toClient.println(html);
    }


    private Game findCurrentGame(String cookie) {
        int i = 0;
        for(; i < games.size() ;i++){
            //System.out.println("Current cookie: " + games.get(i).getCookie());
            if(games.get(i).getCookie().equals(cookie)){
                break;
            }
        }
        return games.get(i);
    }

    private byte[] readFile(String filePath) throws IOException{
        //System.out.println(filePath);
        //Absolute Path
        //File file = new File("C:\\Users\\pppp\\IdeaProjects\\ID1212\\src\\lab2\\index.html");
        //Find relative paths
        //System.out.println(new File(".").getAbsolutePath());
        File file = new File("src/lab2/"+filePath);
        try (FileInputStream fromFile = new FileInputStream(file)){
            //create a buffer as the same length as file
            byte[] buf = new byte[(int) file.length()];
            fromFile.read(buf);
            return buf;
        }
    }

    //this class for storing the request parsed
    private static class HttpRequestHeader{
        private String httpMethod;
        private String path;
        private String httpVersion;
        private String contentLength;
        private String cookie = "";
        private List<String> headers = new ArrayList<>();

        private HttpRequestHeader(String httpMethod, String path, String httpVersion){
            this.httpMethod = httpMethod;
            this.path = path;
            this.httpVersion = httpVersion;
        }

        private void addHeader(String header){
            //System.out.println(header);
            if(header.startsWith("Content-Length:")){
                String[] pairs = header.split(": ");
                this.contentLength = pairs[1];
                //System.out.println("test Length: " + this.contentLength);
            }
            if(header.contains("clientId=")){
                String[] split1 = header.split("clientId=");
                String[] split2 = split1[1].split(";");
                this.cookie = split2[0];
                //System.out.println("test Cookie:" + this.cookie);
            }
            headers.add(header);
        }
    }
}
