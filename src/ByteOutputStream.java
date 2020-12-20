import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ByteOutputStream implements CustomOutputStream{

    private File file;
    private String path;
    private FileWriter fileWriter;

    public ByteOutputStream(String path) {
        this.file = null;
        this.path = path;
        this.fileWriter = null;
    }

    void create() throws IOException {
        this.file = new File(path);
        this.fileWriter = new FileWriter(this.file);
        this.file.createNewFile();
    }

    @Override
    public void writeln(String line) throws IOException {
        for (char c : line.toCharArray()) {
            fileWriter.write(c);
        }
    }

    @Override
    public void close() throws IOException {
        fileWriter.close();
    }
}
