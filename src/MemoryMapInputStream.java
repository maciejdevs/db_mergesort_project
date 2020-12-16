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
        String line = null;

        if (this.positionInMapBuff >= this.buffSize) {
            if (allocateMemory()) {
                line = readMemoryLine();

                if (!tmpLine.isEmpty()) {
                    line = tmpLine + line;
                    tmpLine = "";
                }

//                positionInMapBuff += line.length();

                while (line.charAt(line.length() - 1) != '\n') {
                    String tmp = "";

                    if (!allocateMemory())
                        break;

                    tmp = readMemoryLine();
                    line += tmp;
//                    positionInMapBuff += line.length();

                    if (tmp.isEmpty() && line.charAt(line.length() - 1) != '\n') {
                        line += '\n';
                    }
                }
            } else {
//                line = tmpNextLine;
                this.EOF = true;
            }
        } else {
            line = readMemoryLine();
//            this.positionInMapBuff += line.length();

            while (line.length() > 0 && line.charAt(line.length() - 1) != '\n') {
                String tmp = "";

                // End of stream reached
                if (!allocateMemory())
                    break;
                tmp = readMemoryLine();
                line += tmp;
//                positionInMapBuff += line.length();

                if (tmp.isEmpty() && line.charAt(line.length() - 1) != '\n') {
                    line += '\n';
                }
            }
        }

        // A line cannot begin with a '\n'
//        if (line.length() > 0 && line.charAt(0) == '\n') {
//            line = line.substring(1);
//        }

        return line;
    }

    String readMemoryLine() {
        String line = "";

        if (mapBuff.remaining() == 0) {
            line += "\n";
            return line;
        }

//            CharBuffer charBuff = Charset.forName("UTF-8").decode(mapBuff);
//            char[] charBuffArray = charBuff.array();
        int i = 0;

        ByteBuffer tmp = ByteBuffer.allocate(2);
        byte c;
//            char x = 0;
//            tmp.asCharBuffer().put((char) c);
//            while ((x = tmp.getChar()) != 0)
//                System.out.print(x + " ");
//            tmp.rewind();

        final int indexOfCharacter = 1;
//            char decodedChar = Charset.forName("UTF-8").decode(tmp).charAt(indexOfCharacter);
        char decodedChar = 0;
        int rem = mapBuff.remaining();

        do {
            tmp.clear();
            c = mapBuff.get();
            positionInMapBuff++;
            tmp.putChar((char) c);
            tmp.rewind();
            decodedChar = Charset.forName("UTF-8").decode(tmp).charAt(indexOfCharacter);
            if (decodedChar != '\r')
                line += decodedChar;
        } while (mapBuff.remaining() > 0 && decodedChar != '\r');

            /*while(mapBuff.remaining() > 0) {
                line += decodedChar;

//                tmp = ByteBuffer.allocate(2);
                tmp.clear();
                c = mapBuff.get();
                tmp.putChar((char) c);
                tmp.rewind();
                decodedChar = Charset.forName("UTF-8").decode(tmp).charAt(indexOfCharacter);
            }*/

//            mapBuff.rewind();

//            while (i < charBuffArray.length && charBuffArray[i] != '\n') {
//                line += charBuffArray[i];
//                i++;
//            }

        if (decodedChar == '\r') {
            line += '\n';
        }

/*            // Concat the line with the temporary next line from the previous reading.
            line = tmpNextLine + line;
            tmpNextLine = "";

            // Everything that wasn't read from the buffer is saved in the temporary next line, so it won't be lost.
            while (i < decodedChars.length) {
                tmpNextLine += decodedChars[i];
                i++;
            }*/


        return line;
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
        return (int) file.length();
    }
}
