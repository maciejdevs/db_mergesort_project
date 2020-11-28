import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            ByteOutputStream os = new ByteOutputStream("src/create.txt");
            os.create();

            ByteInputStream is = new ByteInputStream("src/file.txt");
            is.open();
            os.writeln(is.readln());

            os.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
