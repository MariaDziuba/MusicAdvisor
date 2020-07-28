package src;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Getter {
    
    public List<Data> getCategories() {
        String path = Params.RESOURCE + "/v1/browse/categories";
        List<Data> data = new ArrayList<>();
        HttpRequest request = getRequest(path);

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categories = jo.getAsJsonObject("categories");

            for (JsonElement item : categories.getAsJsonArray("items")) {
                Data elem = new Data();
                elem.setCategory(item.getAsJsonObject().get("name").toString());
                data.add(elem);
            }

        } catch (InterruptedException | IOException e) { System.out.println("Error response"); }

        return data;
    }


    public List<Data> getFeatures() {
        String path = Params.RESOURCE + "/v1/browse/featured-playlists";
        HttpRequest request = getRequest(path);
        List<Data> data = null;

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            data = addPlaylists(response);
        } catch (InterruptedException | IOException e) { System.out.println("Error response"); }

        return data;
    }

    public List<Data> getNew() {
        List<Data> data = new ArrayList<>();
        String path = Params.RESOURCE + "/v1/browse/new-releases";
        HttpRequest request = getRequest(path);

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categories = jo.getAsJsonObject("albums");


            for (JsonElement item : categories.getAsJsonArray("items")) {
                Data elem = new Data();
                elem.setAlbum(item.getAsJsonObject().get("name").toString());

                StringBuilder artists = new StringBuilder("[");
                for (JsonElement name : item.getAsJsonObject().getAsJsonArray("artists")) {
                    if (!artists.toString().endsWith("[")) { artists.append(", "); }
                    artists.append(name.getAsJsonObject().get("name"));
                }
                elem.setArtists(artists.append("]").toString());

                elem.setLink(item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").toString());
                data.add(elem);
            }

        } catch (InterruptedException | IOException e) { System.out.println("Error response"); }

        return data;
    }

    public List<Data> getPlaylist(String category_id) {
        String path = Params.RESOURCE + "/v1/browse/categories/" + category_id + "/playlists";
        HttpRequest request = getRequest(path);
        List<Data> data = null;

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            if (jo.toString().contains("Specified id doesn't exist")) {
                System.out.println("Specified id doesn't exist");
            } else { data = addPlaylists(response); }

        } catch (InterruptedException | IOException e) { System.out.println("Error response"); }

        return data;
    }

    private HttpRequest getRequest(String path) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Params.ACCESS_TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();
    }

    private List<Data> addPlaylists(HttpResponse<String> response) {
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        List<Data> data = new ArrayList<>();

        for (JsonElement item : playlists.getAsJsonArray("items")) {
            Data elem = new Data();

            elem.setAlbum(item.getAsJsonObject().get("name").toString());
            elem.setLink(item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").toString());

            data.add(elem);
        }

        return data;
    }
}
