import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedOutputStream {
    private File file;
    private String path;
    private FileWriter fileWriter;
    private BufferedWriter bw;

    public BufferedOutputStream(String path) {
        this.file = null;
        this.path = path;
        this.fileWriter = null;
        this.bw = null;
    }

    void create() throws IOException {
        this.file = new File(path);
        this.fileWriter = new FileWriter(this.file);
        this.file.createNewFile();
        this.bw = new BufferedWriter(fileWriter);
    }

    void writeln(String line) throws IOException {
        bw.write(line);
    }

    void close() throws IOException {
        bw.close();
    }
}
