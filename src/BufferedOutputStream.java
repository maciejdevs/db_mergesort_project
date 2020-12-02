import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedOutputStream {
    private File file;
    private String path;
    private FileWriter fileWriter;
    private BufferedWriter bw;
    private int size;
    private char[] buffer;

    private static int DEFAULT_CHAR_BUFFER_SIZE = 8192; // Default buffer size for the buffered writer

    public BufferedOutputStream(String path) {
        this.file = null;
        this.path = path;
        this.fileWriter = null;
        this.size = DEFAULT_CHAR_BUFFER_SIZE;
        this.bw = null;
    }

    public BufferedOutputStream(String path, int size) {
        this(path);
        this.size = size;
        this.buffer = new char[this.size];
    }

    void create() throws IOException {
        this.file = new File(path);
        this.fileWriter = new FileWriter(this.file);
        this.file.createNewFile();
        this.bw = new BufferedWriter(fileWriter);
    }

    void writeln(String line) throws IOException {
        if(size == DEFAULT_CHAR_BUFFER_SIZE) {
            bw.write(line);
        } else {
            char[] lineArray = line.toCharArray();
            if(lineArray.length <= this.size){
                for(int i = 0; i < this.size; i++){
                    this.buffer[i] = lineArray[i];
                }
                for(char c : this.buffer){
                    fileWriter.write(c);
                }
                flushBuffer();
            } else {
                int cpt_lineArray = 0;
                while(cpt_lineArray < lineArray.length){
                    int i = 0;
                    while(cpt_lineArray < lineArray.length && i < this.size){
                        this.buffer[i] = lineArray[cpt_lineArray];
                        cpt_lineArray++;
                        i++;
                    }
                    for(int j = 0; j < i; j++){
                        fileWriter.write(this.buffer[j]);
                    }
                    flushBuffer();
                }
            }
        }
    }

    void close() throws IOException {
        bw.close();
        flushBuffer();
    }

    private void flushBuffer(){
        this.buffer = new char[this.size];
    }
}
