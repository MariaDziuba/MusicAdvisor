package src;

import java.util.List;

public class Print {
    static int elem;
    static int page;
    static List<Data> data;
    static int pagesCount;

    public static void print(List<Data> data) {
        elem = -5;
        page = 0;
        Print.data = data;
        pagesCount = data.size() / Params.RESULTS_PER_PAGE;
        pagesCount += data.size() % Params.RESULTS_PER_PAGE != 0 ? 1 : 0;

        printNextPage();
    }

    public static void printNextPage() {
        if (page > pagesCount) {
            System.out.println("No more pages.");
        } else {
            elem += Params.RESULTS_PER_PAGE;
            page++;
            print();
        }
    }

    public static void printPrevPage() {
        if (page == 1) {
            System.out.println("No more pages.");
        } else {
            elem -= Params.RESULTS_PER_PAGE;
            page--;
            print();
        }
    }

    public static void print() {
        data.stream()
                .skip(elem)
                .limit(Params.RESULTS_PER_PAGE)
                .forEach(System.out::println);
        System.out.printf("---PAGE %d OF %d---\n", page, pagesCount);
    }
}
