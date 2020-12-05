import java.io.*;
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

    public MemoryMapInputStream(String path, int buffSize) {
        this.path = path;
        this.isOpen = false;
        this.offset = 0;
        this.buffSize = buffSize;
        this.positionInMapBuff = 0;
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

                while(line.charAt(line.length() - 1) != '\n') {
                    String tmp = "";
                    allocateMemory();
                    tmp = readMemoryLine();
                    line += tmp;
                    positionInMapBuff += line.length();
                }
            } else {
                line = "";
            }
        } else {
            line = readMemoryLine();
            this.positionInMapBuff += line.length();

            if (line.length() > 0 && line.charAt(line.length() - 1) != '\n') {
                this.tmpLine = line;
                line = "";
//                line = readln();
            }
        }

        return line;
    }

    String readMemoryLine() {
        char[] charBuff = Charset.forName("UTF-8").decode(mapBuff).array();
        String line = "";
        int i = 0;

        while (i < charBuff.length && charBuff[i] != '\n') {
            line += charBuff[i];
            i++;
        }

        if(i < charBuff.length && charBuff[i] == '\n') {
            line += '\n';
        }

        return line;
    }

    void seek() {

    }

    boolean endofstream() {
        return false;
    }

}
