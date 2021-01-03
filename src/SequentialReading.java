import java.io.FileNotFoundException;
import java.io.IOException;

public class SequentialReading {

    private String path;

    public SequentialReading(String path) {
        this.path = path;
    }

    public void length1() throws IOException {
        int sum = 0;
        ByteInputStream is = new ByteInputStream(path);
        is.open();
        String line = "";

        while (!is.endofstream()) {
            line = is.readln();
            sum += line.length();
        }

        System.out.println("\nSum: " + sum);
    }

    public void length2() throws IOException {
        int sum = 0;
        BufferedInputStream is = new BufferedInputStream(path);
        is.open();
        String line = null;

        while ((line = is.readln()) != null) {
            sum += line.length();
        }

        System.out.println("\nSum: " + sum);
    }

    public void length3(int bufferSize) throws IOException {
        int sum = 0;
        BufferedInputStream is = new BufferedInputStream(path, bufferSize);
        is.open();
        String line;

        while ((line = is.readln()) != null) {
            sum += line.length();
        }

        System.out.println("\nSum: " + sum);
    }

    public void length4(int bufferSize) throws IOException {
        int sum = 0;
        MemoryMapInputStream is = new MemoryMapInputStream(path, bufferSize);
        is.open();
        String line;

        while ((line = is.readln()) != null) {
            sum += line.length();
        }

        System.out.println("\nSum: " + sum);
    }

}
