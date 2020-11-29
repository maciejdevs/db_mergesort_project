import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
//            BufferedInputStream in = new BufferedInputStream("src/file.txt");
//            in.open();
//
//            System.out.println(in.readln());
//            System.out.println(in.readln());
//            in.endofstream();
//            System.out.println(in.readln());

//            ByteOutputStream os = new ByteOutputStream("src/create.txt");
//            os.create();
//
//            ByteInputStream is = new ByteInputStream("src/file.txt");
//            is.open();
//            os.writeln(is.readln());
//
//            os.close();

            BufferedOutputStream os = new BufferedOutputStream("src/create.txt");
            os.create();

            BufferedInputStream is = new BufferedInputStream("src/file.txt", 10);
            is.open();
            os.writeln(is.readln());

            os.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
