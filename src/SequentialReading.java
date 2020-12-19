import java.io.FileNotFoundException;
import java.io.IOException;

public class SequentialReading {

    private String path;

    public SequentialReading(String path) {
        this.path = path;
    }

    public void length1() throws IOException {
        int sum = 0;
        int counter = 0;
        ByteInputStream is = new ByteInputStream(path);
        is.open();
        String line = "";

        while (!is.endofstream()) {
            line = is.readln();
            sum += line.length();
//            System.out.println(counter++);
        }

        System.out.println("\nSum: " + sum);
    }

    public void length2() throws IOException {
        int sum = 0;
        int counter = 0;
        BufferedInputStream is = new BufferedInputStream(path);
        is.open();
        String line = null;

        while ((line = is.readln()) != null) {
            sum += line.length();
//            System.out.println(counter++);
        }

        System.out.println("\nSum: " + sum);
    }

    public void length3() throws IOException {
        int sum = 0;
        int counter = 0;
        BufferedInputStream is = new BufferedInputStream(path, 256);
        is.open();
        String line;

        while ((line = is.readln()) != null) {
            sum += line.length();
//            System.out.println(line);
        }

        System.out.println("\nSum: " + sum);
    }

    public void length4() throws IOException {
        int sum = 0;
        int counter = 0;
        MemoryMapInputStream is = new MemoryMapInputStream(path, 10000);
        is.open();
        String line;

        while ((line = is.readln()) != null) {
//            System.out.print(line);
            sum += line.length();
//            System.out.println(counter++);
        }

        System.out.println("\nSum: " + sum);
    }

}
