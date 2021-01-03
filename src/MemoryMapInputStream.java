import java.io.*;
import java.nio.ByteBuffer;
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
    private boolean EOF;

    public MemoryMapInputStream(String path, int buffSize) {
        this.path = path;
        this.isOpen = false;
        this.offset = 0;
        this.buffSize = buffSize;
        this.positionInMapBuff = 0;
        this.tmpLine = "";
        this.tmpNextLine = "";
        this.EOF = false;
        this.fileSize = 0;
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

    boolean allocateMemory(int offset) throws IOException {
        if (bytesLeft > 0) {
            if (mapBuff != null) {
                mapBuff.clear();
            }

            if (fileSize - offset < buffSize) {
                buffSize = (int) (fileSize - offset);
            }

            mapBuff = fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, this.buffSize);
            bytesLeft -= buffSize;
            offset += buffSize;
            this.offset = offset;
            positionInMapBuff = 0;

            return true;
        }

        return false;
    }

    String readln() throws IOException {
        StringBuilder line = null;

        if (this.positionInMapBuff >= this.buffSize) {
            if (allocateMemory()) {
                line = new StringBuilder(readMemoryLine());

                if (!tmpLine.isEmpty()) {
                    line.insert(0, tmpLine);
//                    line = tmpLine + line;
                    tmpLine = "";
                }

                while (line.charAt(line.length() - 1) != '\n') {
                    String tmp = "";

                    if (!allocateMemory())
                        break;

                    tmp = readMemoryLine();
                    line.append(tmp);

                    if (tmp.isEmpty() && line.charAt(line.length() - 1) != '\n') {
                        line.append('\n');
                    }
                }
            } else {
                this.EOF = true;
            }
        } else {
            line = new StringBuilder(readMemoryLine());

            while (line.length() > 0 && line.charAt(line.length() - 1) != '\n') {
                String tmp = "";

                // End of stream reached
                if (!allocateMemory()) {
                    if(line.charAt(line.length() - 1) != '\n') {
                        line.append('\n');
                        break;
                    }
                }

                tmp = readMemoryLine();
                line.append(tmp);

                if (tmp.isEmpty() && line.charAt(line.length() - 1) != '\n') {
                    line.append('\n');
                }
            }
        }

        return line != null ? line.toString() : null;
    }

    String readMemoryLine() {
        StringBuilder line = new StringBuilder();

        if (mapBuff.remaining() == 0) {
            line.append("\n");
            return line.toString();
        }

        int i = 0;

        ByteBuffer tmp = ByteBuffer.allocate(2);
        byte c;
        final int indexOfCharacter = 1;
        char decodedChar = 0;

        do {
            tmp.clear();
            c = mapBuff.get();
            positionInMapBuff++;
            tmp.putChar((char) c);
            tmp.rewind();
//            decodedChar = Charset.forName("UTF-8").decode(tmp).charAt(indexOfCharacter);
            decodedChar = (char) tmp.get(indexOfCharacter);
            if (decodedChar != '\r' && decodedChar != '\n')
                line.append(decodedChar);
        } while (mapBuff.remaining() > 0 && decodedChar != '\r' && decodedChar != '\n');

        if (decodedChar == '\r') {
            line.append('\n');
            positionInMapBuff++;
            mapBuff.get();
        }

        if(decodedChar == '\n') {
            line.append('\n');
        }

        return line.toString();
    }

    void seek(int pos, boolean absolute) throws IOException {
        if (absolute) {
            bytesLeft = fileSize;
            allocateMemory(pos);
            mapBuff.position(0);
            tmpNextLine = "";
        } else {
            throw new UnsupportedOperationException("There is no need for relative seek");
        }
    }

    boolean endofstream() {
        return this.EOF;
    }

    int getFileSize() {
        return (int) this.fileSize;
    }
}
