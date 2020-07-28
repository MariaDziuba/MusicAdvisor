package src;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.URI.create;

public class Authorization {


    void createServer() throws NullPointerException {

        String uri = Params.SERVER_PATH + "/authorize" +
                "?client_id=" + Params.CLIENT_ID +
                "&redirect_uri=" + Params.REDIRECT_URI +
                "&response_type=code";
        System.out.println("Click on this link to authorize:\n" + uri);

        try {
            HttpServer server = HttpServer.create(); // create a server
            server.bind(new InetSocketAddress(8080), 0); // bind it to the port
            server.start();
            server.createContext("/", // create mapping between uri path to a http handler
                    // which is invoked to handle requests destined for that path on the associated Http server
                    exchange -> {
                        String query = exchange.getRequestURI().getQuery();

                        String request = "";
                        if (query != null && query.contains("code")) {
                            Params.AUTH_CODE = query.substring(5);
                            System.out.println("code received");
                            request = "Got the code. Return back to your program.";
                        } else {
                            request = "Not found authorization code. Try again.";
                        }

                        exchange.sendResponseHeaders(200, request.length());
                        exchange.getResponseBody().write(request.getBytes());
                        exchange.getResponseBody().close();
                    });

            System.out.println("waiting for code...");
            while (Params.AUTH_CODE.isBlank()) { Thread.sleep(10); }
            server.stop(10);

        } catch (IOException | InterruptedException e) {
            System.out.println("Server error");
        }
    }

    void makeRequest() {
        System.out.println("Making http request for access_token...");

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(create(Params.SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code" +
                                "&code=" + Params.AUTH_CODE +
                                "&client_id=" + Params.CLIENT_ID +
                                "&client_secret=" + Params.CLIENT_SECRET +
                                "&redirect_uri=" + Params.REDIRECT_URI))
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null && response.body().contains("access_token")) { parseAccessToken(response.body()); }
            System.out.println( "---SUCCESS---");
        } catch (InterruptedException | IOException e) { System.out.println("Error response"); }
    }

    void parseAccessToken(String body) {

        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Params.ACCESS_TOKEN = jo.get("access_token").getAsString();
    }
}
