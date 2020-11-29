import java.io.*;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BufferedInputStream {
    private File file;
    private String path;
    private boolean isOpen;
    private FileReader fileReader;
    private BufferedReader br;
    private int size;
    private char[] buffer;
    private int currentPos;

    private static int DEFAULT_CHAR_BUFFER_SIZE = 8192; // Default buffer size for the buffered reader

    public BufferedInputStream(String path) {
        this.file = null;
        this.isOpen = false;
        this.fileReader = null;
        this.path = path;
        this.br = null;
        this.size = DEFAULT_CHAR_BUFFER_SIZE;
        this.currentPos = 0; // Used for the third implementation.
    }

    public BufferedInputStream(String path, int size) {
        this(path);
        this.size = size;
        this.buffer = new char[this.size];
    }

    void open() throws FileNotFoundException {
        if (!isOpen) {
            isOpen = true;
            file = new File(path);
            fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);
        }
    }

    String readln() throws IOException {
        if(size == DEFAULT_CHAR_BUFFER_SIZE) {
            return br.readLine();
        } else {
            String line = "";
            int nbChars = fileReader.read(this.buffer);

            while(!containsEndLine(this.buffer) && nbChars >= size) {
                line += String.valueOf(this.buffer);
                flushBuffer();
                nbChars = fileReader.read(this.buffer, this.currentPos, this.size);
                this.currentPos += this.size;
            }

            int i = 0;
            while(nbChars != -1 && i < nbChars && buffer[i] != '\n') {
                line += buffer[i];
                i++;
            }
            this.currentPos += i;

            return line;
        }
    }

    private boolean containsEndLine(char[] buffer) {
        return false;
    }

    private void flushBuffer() {
        this.buffer = new char[this.size];
    }

    void seek(int pos, boolean absolute) throws IOException {
        if (absolute) {
            FileReader tmp_fileReader = new FileReader(file);
            BufferedReader tmp_bufferedReader = new BufferedReader(tmp_fileReader);
            tmp_bufferedReader.skip(pos);
            this.br = tmp_bufferedReader;
        } else {
            br.skip(pos);
        }
    }

    boolean endofstream() throws IOException {
        br.mark(1);
        int byte1 = br.read();
        br.reset();

        return byte1 == -1;
    }
}
