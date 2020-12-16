import java.io.*;

public class BufferedInputStream {
    private File file;
    private String path;
    private boolean isOpen;
    private FileReader fileReader;
    private BufferedReader br;
    private int size;
    private char[] buffer;
    private String tmpNextLine;
    private int currentBufferIndex;
    private boolean skipLeftEndLine;
    private int nbChars;

    private static int DEFAULT_CHAR_BUFFER_SIZE = 8192; // Default buffer size for the buffered reader

    public BufferedInputStream(String path) {
        this.file = null;
        this.isOpen = false;
        this.fileReader = null;
        this.path = path;
        this.br = null;
        this.size = DEFAULT_CHAR_BUFFER_SIZE;
        this.tmpNextLine = "";
        this.currentBufferIndex = 0;
        this.skipLeftEndLine = false;
        this.nbChars = 0;
    }

    public BufferedInputStream(String path, int size) {
        this(path);
        this.size = size;
        this.buffer = new char[this.size];
    }

    void open() throws IOException {
        if (!isOpen) {
            isOpen = true;
            file = new File(path);
            fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);
            br.mark(getFileSize());
        }
    }


    String readln() throws IOException {
        if (size == DEFAULT_CHAR_BUFFER_SIZE) {
            return br.readLine();
        } else {
            String line = null;
            boolean eol = false;
            char lastChar = ' ';

            if (currentBufferIndex < size && skipLeftEndLine && buffer[currentBufferIndex] == '\n')
                currentBufferIndex++;

            if (currentBufferIndex >= size)
                currentBufferIndex = 0;

            if (currentBufferIndex == 0)
                nbChars = fileReader.read(this.buffer);

            // Init only if the line exists (the stream hasn't ended yet)
            // We need to return null when we the end of stream was reached
            if (nbChars != -1 && currentBufferIndex < size && !eol) {
                line = "";
            }

            while (nbChars != -1 && currentBufferIndex < size && !eol) {
                while (currentBufferIndex < nbChars) {
                    lastChar = buffer[currentBufferIndex];
                    if (buffer[currentBufferIndex] == '\r' || buffer[currentBufferIndex] == '\n') {
                        eol = true;
                        break;
                    } else {
                        line += lastChar;
                    }

                    currentBufferIndex++;
                }

                if (!eol) {
                    flushBuffer();
                    currentBufferIndex = 0;
                    nbChars = fileReader.read(this.buffer);
                } else {
                    currentBufferIndex++;
                    if (lastChar == '\r') {
                        skipLeftEndLine = true;
                    }
                }
            }

            return line;
        }
    }

    private void flushBuffer() {
        this.buffer = new char[this.size];
    }

    void seek(int pos, boolean absolute) throws IOException {
        if (size == DEFAULT_CHAR_BUFFER_SIZE) {
            if (absolute) {
                br.reset();
                br.skip(pos);
            } else {
                br.skip(pos);
            }
        } else {
            if (absolute) {
                FileReader tmp_fileReader = new FileReader(file);
                tmp_fileReader.skip(pos);
                fileReader = tmp_fileReader;
                tmpNextLine = "";
                currentBufferIndex = 0;
            } else {
                fileReader.skip(pos);
//                tmpNextLine = "";
            }
        }

    }

    boolean endofstream() throws IOException {
        br.mark(currentBufferIndex);
        int byte1 = 0;

        if (size == DEFAULT_CHAR_BUFFER_SIZE) {
            byte1 = br.read();
            br.reset();
        } else {
            // TODO: repair this function
            PeekReader peekReader = new PeekReader(fileReader);

            byte1 = fileReader.read();
            PushbackReader pushback = new PushbackReader(fileReader);
            byte1 = pushback.read();
            char test = (char) byte1;
            pushback.unread(byte1);
        }

        return byte1 == -1;
    }

    int getFileSize() {
        return (int) file.length();
    }
}
