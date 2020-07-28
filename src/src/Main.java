package src;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    static String SERVER_PATH = "";
    static String RESOURCE = "";
    static int RESULTS_PER_PAGE;

    public static void main(String[] args) throws IOException {

        MusicAdvisor advisor = new MusicAdvisor();
        if (args.length > 1 && args[0].equals("-access")) {
            SERVER_PATH = args[1];
        }
        if (args.length > 2 && args[2].equals("-resource")) {
            RESOURCE = args[3];
        }
        if (args.length > 4 && args[4].equals("-page")) { RESULTS_PER_PAGE = Integer.parseInt(args[5]); }
        advisor.start();
    }
}
