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

    private static int DEFAULT_CHAR_BUFFER_SIZE = 8192; // Default buffer size for the buffered reader

    public BufferedInputStream(String path) {
        this.file = null;
        this.isOpen = false;
        this.fileReader = null;
        this.path = path;
        this.br = null;
        this.size = DEFAULT_CHAR_BUFFER_SIZE;
        this.tmpNextLine = "";
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
        if (size == DEFAULT_CHAR_BUFFER_SIZE) {
            return br.readLine() + "\n";
        } else {
            String line = "";

            // Check if the tmp_next_line contains the entire next line. We should treat this case separately.
            if (containsNextLine(tmpNextLine)) {
                line = handleNextLineFromTmpString(line);
            } else {
                line = handleNextLineFromBuffer(line);
            }

            return line;
        }
    }

    private String handleNextLineFromBuffer(String line) throws IOException {
        int nbChars = fileReader.read(this.buffer);

        // Continue reading until the buffer contains an end of line
        while (!containsEndLine(this.buffer) && nbChars >= size) {
            line += String.valueOf(this.buffer);
            flushBuffer();
            nbChars = fileReader.read(this.buffer);
        }

        // Add every character from the buffer before the first encountered end of line
        int i = 0;
        while (nbChars != -1 && i < nbChars && buffer[i] != '\n') {
            line += buffer[i];
            i++;
        }

        // Concat the line with the temporary next line from the previous reading.
        line = tmpNextLine + line;
        tmpNextLine = "";

        // Everything that wasn't read from the buffer is saved in the temporary next line, so it won't be lost.
        while (i < nbChars) {
            tmpNextLine += buffer[i];
            i++;
        }

        flushBuffer();
        return line;
    }

    private String handleNextLineFromTmpString(String line) {
        int i = 1; // skip the first '\n' which is useless in this case
        int nbChars = tmpNextLine.length();

        while (i < nbChars && tmpNextLine.charAt(i) != '\n') {
            line += tmpNextLine.charAt(i);
            i++;
        }

        flushTmpNextLine();
        return line;
    }

    /**
     * Check if the string contains the entire next line.
     * @param tmpNextLine the string that will be checked.
     * @return true if the string contains at least two end of lines, false otherwise.
     */
    private boolean containsNextLine(String tmpNextLine) {
        int endLinesCounter = 0;

        for (int i = 0; i < tmpNextLine.length(); i++) {
            if(tmpNextLine.charAt(i) == '\n') {
                endLinesCounter++;
            }
        }

        return endLinesCounter > 1;
    }

    private boolean containsEndLine(char[] buffer) {
        boolean ok = false;

        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == '\n') {
                ok = true;
                break;
            }
        }

        return ok;
    }

    private void flushBuffer() {
        this.buffer = new char[this.size];
    }

    /**
     * Remove the first line of the temporary next line.
     */
    private void flushTmpNextLine() {
        // first '\n' doesn't matter
        int first = tmpNextLine.indexOf('\n');

        // look for the second '\n' from the first + 1 index
        int second = tmpNextLine.indexOf('\n', first + 1);

        // take the string from the second '\n' (skip the line)
        tmpNextLine = tmpNextLine.substring(second);
    }

    void seek(int pos, boolean absolute) throws IOException {
        if (size == DEFAULT_CHAR_BUFFER_SIZE) {
            if (absolute) {
                FileReader tmp_fileReader = new FileReader(file);
                BufferedReader tmp_bufferedReader = new BufferedReader(tmp_fileReader);
                tmp_bufferedReader.skip(pos);
                this.br = tmp_bufferedReader;
            } else {
                br.skip(pos);
            }
        } else {
            if (absolute) {
                FileReader tmp_fileReader = new FileReader(file);
                tmp_fileReader.skip(pos);
                fileReader = tmp_fileReader;
                tmpNextLine = "";
            } else {
                fileReader.skip(pos);
//                tmpNextLine = "";
            }
        }

    }

    boolean endofstream() throws IOException {
        br.mark(1);
        int byte1 = br.read();
        br.reset();

        return byte1 == -1;
    }
}
