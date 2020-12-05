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
import java.util.EnumSet;

public class MemoryMapOutputStream {
    private File file;
    private String path;
    private int buffSize;
    private FileChannel fileChannel;
    private MappedByteBuffer mapBuff;
    private int offset;


    public MemoryMapOutputStream(String path) {
        this.path = path;
        this.buffSize = 0;
        this.offset = 0;
    }

    void create() throws IOException {
        this.file = new File(this.path);
        this.file.createNewFile();
        this.fileChannel = (FileChannel)Files.newByteChannel(Paths.get(this.path), EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));
    }

    void allocateMemory(String line) throws IOException {
        if (this.mapBuff != null) {
            this.mapBuff.force();
        }

        this.buffSize = line.length();
        this.mapBuff = this.fileChannel.map(MapMode.READ_WRITE, this.offset, this.buffSize);
        this.offset += this.buffSize;
    }

    void writeln(String line) throws IOException {
        this.allocateMemory(line);
        CharBuffer charBuff = CharBuffer.wrap(line);
        this.mapBuff.put(Charset.forName("utf-8").encode(charBuff));
        this.mapBuff.clear();
    }

    void close() throws IOException {
        this.mapBuff.clear();
        this.fileChannel.close();
    }
}
