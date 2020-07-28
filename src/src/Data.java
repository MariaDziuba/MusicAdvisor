package src;

public class Data {
    String album, artists, category, link;

    void setAlbum(String album) {
        this.album = album;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        if (album != null) {
            info.append(album).append("\n");
        }
        if (artists != null) {
            info.append(artists).append("\n");
        }
        if (link != null) {
            info.append(link).append("\n");
        }
        if (category != null) {
            info.append(category).append("\n");;
        }
        return info.toString().replaceAll("\"", "");
    }
}
