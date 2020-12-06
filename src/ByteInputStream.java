import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ByteInputStream {

    private File file;
    private String path;
    private boolean isOpen;
    private FileReader fileReader;
    private int currentPos;

    public ByteInputStream(String path) {
        this.file = null;
        this.isOpen = false;
        this.fileReader = null;
        this.path = path;
        this.currentPos = 0;
    }

    void open() throws FileNotFoundException {
        if (!isOpen) {
            isOpen = true;
            file = new File(path);
            fileReader = new FileReader(file);
        }
    }

    String readln() throws IOException {
        String line = "";
        char temp_char;

        do {
            temp_char = (char) fileReader.read();
            line += temp_char;
            currentPos++;
        } while (temp_char != '\n' && !endofstream());

        return line;
    }

    void seek(int pos, boolean absolute) throws IOException {
        if (absolute) {
            FileReader tmp_fileReader = new FileReader(file);
            tmp_fileReader.skip(pos);
            this.fileReader = tmp_fileReader;
            currentPos = pos;
        } else {
            fileReader.skip(pos);
            currentPos += pos;
        }
    }

    boolean endofstream() throws IOException {
        int tmp = 0;
        tmp = fileReader.read();
        this.seek(currentPos, true);

        return tmp == -1;
    }
}
