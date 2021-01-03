import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumSet;

public class MemoryMapOutputStream implements CustomOutputStream{
    private File file;
    private String path;
    private int buffSize;
    private FileChannel fileChannel;
    private MappedByteBuffer mapBuff;
    private int offset;
    private char[] buffer;
    private int spaceLeftInBuffer;
    private int charsCounter;


    public MemoryMapOutputStream(String path) {
        this.path = path;
        this.buffSize = 0;
        this.offset = 0;
        this.spaceLeftInBuffer = 0;
        this.charsCounter = 0;
    }

    public MemoryMapOutputStream(String path, int buffSize) {
        this(path);
        this.buffSize = buffSize;
        this.buffer = new char[buffSize];
        this.spaceLeftInBuffer = 0;
        this.charsCounter = 0;
    }

    void create() throws IOException {
        this.file = new File(this.path);
        this.file.createNewFile();
        this.fileChannel = (FileChannel)Files.newByteChannel(Paths.get(this.path), EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));
    }

    boolean allocateMemory(int remaining) throws IOException {
        if(this.mapBuff != null && this.mapBuff.position() < buffSize) {
            return false;
        }

        if (remaining > 0) {
            if (this.mapBuff != null) {
                this.mapBuff.force();
            }

            this.mapBuff = this.fileChannel.map(MapMode.READ_WRITE, this.offset, 2 * this.buffSize);
            this.offset += this.buffSize;
            this.spaceLeftInBuffer = this.buffSize;

            return true;
        }

        return false;
    }

    @Override
    public void writeln(String line) throws IOException {
        CharBuffer charBuff = CharBuffer.wrap(line);
        this.allocateMemory(charBuff.remaining());

        while(charBuff.remaining() > 0) {
            int size = 0;
            int availableSpace = spaceLeftInBuffer - charsCounter;

            if(charBuff.length() <= availableSpace) {
                size = charBuff.length();
            } else {
                size = availableSpace;
            }

            buffer = new char[size];
            charBuff.get(buffer, 0, size);
            this.mapBuff.put(Charset.forName("utf-8").encode(CharBuffer.wrap(buffer)));
            this.spaceLeftInBuffer -= size;
            allocateMemory(charBuff.remaining());
        }
    }

    @Override
    public void close() throws IOException {
        this.mapBuff.clear();
        this.fileChannel.close();
    }
}
