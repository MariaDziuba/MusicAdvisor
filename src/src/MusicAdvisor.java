package src;

import java.util.Scanner;

public class MusicAdvisor {


    Getter getter = new Getter();

    void featured() {
        System.out.println("---FEATURED---");
        Print.print(getter.getFeatures());
    }
    void newReleases() {
        System.out.println("---NEW RELEASES---");
        Print.print(getter.getNew());
    }
    void categories() {
        System.out.println("---CATEGORIES---");
        Print.print(getter.getCategories());
    }
    void playlists() {
        System.out.println("---PLAYLISTS---");
        Print.print(getter.getPlaylist(new Scanner(System.in).next()));
    }

    void start() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Hello! I am a music advisor! Print a command: ");
            String input;
            boolean authorized = false;
            while (!authorized) {
                input = sc.next();
                if (input.equals("auth")) {
                    Authorization auth = new Authorization();
                    auth.createServer();
                    auth.makeRequest();
                    authorized = true;
                } else {
                    System.out.println("Please, provide access for the application");
                }
            }
            while (true) {
                input = sc.next();
                switch (input) {
                    case "featured": {
                        featured();
                        break;
                    }
                    case "new": {
                        newReleases();
                        break;
                    }
                    case "categories": {
                        categories();
                        break;
                    }
                    case "playlists": {
                        playlists();
                        break;
                    }
                    case "exit": {
                        System.exit(0);
                    }
                }
            }

        }
    }

}
