import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
    private String tmpString;

    public MemoryMapInputStream(String path, int buffSize){
        this.path = path;
        this.isOpen = false;
        this.offset = 0;
        this.buffSize = buffSize;
        this.positionInMapBuff = 0;
    }

    void open() throws IOException {
        if(!isOpen){
            isOpen = true;

            file = new File(path);
            fileSize = file.length();
            bytesLeft = fileSize;

            fileInputStream = new FileInputStream(file);
            fileChannel = fileInputStream.getChannel();

            allocateMemory();
        }
    }

    void allocateMemory() throws IOException {
        mapBuff = fileChannel.map(FileChannel.MapMode.READ_ONLY, this.offset, this.buffSize);
        bytesLeft -= buffSize;
        offset += buffSize;
        positionInMapBuff = 0;
    }

    void readln() throws IOException {
        if(this.positionInMapBuff >= this.buffSize){
            //allocateMemory();
        } else {
            String line = readMemoryLine();
            System.out.println(line);
        }
    }

    String readMemoryLine(){
        CharBuffer charBuff = Charset.forName("UTF-8").decode(mapBuff);
        return charBuff.toString();
    }

    void seek(){

    }

    boolean endofstream(){
        return false;
    }

}
