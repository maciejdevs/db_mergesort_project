import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MemoryMapInputStream {

    private File file;
    private String path;
    private FileInputStream fileInputStream;
    private boolean isOpen;
    private int buffSize;
    private long fileSize;
    private long bytesLeft;
    private FileChannel fileChannel;
    private MappedByteBuffer mapBuff;
    private int positionInMapBuff;
    private int offset;
    private String tmpLine;
    private String tmpNextLine;

    public MemoryMapInputStream(String path, int buffSize) {
        this.path = path;
        this.isOpen = false;
        this.offset = 0;
        this.buffSize = buffSize;
        this.positionInMapBuff = 0;
        this.tmpLine = "";
        this.tmpNextLine = "";
    }

    void open() throws IOException {
        if (!isOpen) {
            isOpen = true;

            file = new File(path);
            fileSize = file.length();
            bytesLeft = fileSize;

            fileInputStream = new FileInputStream(file);
            fileChannel = fileInputStream.getChannel();

            allocateMemory();
        }
    }

    boolean allocateMemory() throws IOException {
        if (bytesLeft > 0) {
            if (mapBuff != null) {
                mapBuff.clear();
            }

            if (fileSize - offset < buffSize) {
                buffSize = (int) (fileSize - offset);
            }

            mapBuff = fileChannel.map(FileChannel.MapMode.READ_ONLY, this.offset, this.buffSize);
            bytesLeft -= buffSize;
            offset += buffSize;
            positionInMapBuff = 0;

            return true;
        }

        return false;
    }

    String readln() throws IOException {
        String line = "";

        if (this.positionInMapBuff >= this.buffSize) {
            if (allocateMemory()) {
                line = readMemoryLine();

                if (!tmpLine.isEmpty()) {
                    line = tmpLine + line;
                    tmpLine = "";
                }

                positionInMapBuff += line.length();

                while (line.charAt(line.length() - 1) != '\n') {
                    String tmp = "";
                    allocateMemory();
                    tmp = readMemoryLine();
                    line += tmp;
                    positionInMapBuff += line.length();

                    if(tmp.isEmpty() && line.charAt(line.length() - 1) != '\n') {
                        line += '\n';
                    }
                }
            } else {
                line = tmpNextLine;
            }
        } else {
            line = readMemoryLine();
            this.positionInMapBuff += line.length();

            while (line.length() > 0 && line.charAt(line.length() - 1) != '\n') {
                String tmp = "";
                allocateMemory();
                tmp = readMemoryLine();
                line += tmp;
                positionInMapBuff += line.length();

                if(tmp.isEmpty() && line.charAt(line.length() - 1) != '\n') {
                    line += '\n';
                }
            }
        }

        // A line cannot begin with a '\n'
        if(line.length() > 0 && line.charAt(0) == '\n') {
            line = line.substring(1);
        }

        return line;
    }

    private String handleNextLineFromTmpString(String line) {
        int i = 1; // skip the first '\n' which is useless in this case
        int nbChars = tmpNextLine.length();

        while (i < nbChars && tmpNextLine.charAt(i) != '\n') {
            line += tmpNextLine.charAt(i);
            i++;
        }

        if (i < nbChars && tmpNextLine.charAt(i) == '\n') {
            line += '\n';
        }

        flushTmpNextLine();
        return line;
    }

    String readMemoryLine() {
        String line = "";

        if (BufferUtils.containsNextLine(tmpNextLine)) {
            line = handleNextLineFromTmpString(line);
        } else {
            CharBuffer charBuff = Charset.forName("UTF-8").decode(mapBuff);
            char[] charBuffArray = charBuff.array();
            int i = 0;

            while (i < charBuffArray.length && charBuffArray[i] != '\n') {
                line += charBuffArray[i];
                i++;
            }

            if (i < charBuffArray.length && charBuffArray[i] == '\n') {
                line += '\n';
            }

            // Concat the line with the temporary next line from the previous reading.
            line = tmpNextLine + line;
            tmpNextLine = "";

            // Everything that wasn't read from the buffer is saved in the temporary next line, so it won't be lost.
            while (i < charBuffArray.length) {
                tmpNextLine += charBuffArray[i];
                i++;
            }
        }

        return line;
    }

    void seek() {

    }

    boolean endofstream() {
        return false;
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
}
