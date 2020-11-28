import java.io.*;

public class BufferedInputStream {
    private File file;
    private String path;
    private boolean isOpen;
    private FileReader fileReader;
    private BufferedReader br;

    public BufferedInputStream(String path) {
        this.file = null;
        this.isOpen = false;
        this.fileReader = null;
        this.path = path;
        this.br = null;
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
        return br.readLine();
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
