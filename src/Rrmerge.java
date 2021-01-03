import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rrmerge {

    private List<String> f;
    private final int MMAP_INPUT_BUFFSIZE = 8192;
    private final String OUTPUT_FILE = "src/output_file.csv";

    public Rrmerge(List<String> f) {
        this.f = f;
    }

    void rrmerge_buffer_byte() throws IOException {
        ByteOutputStream outputStream = new ByteOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_buffered(outputStream);
        outputStream.close();
    }

    void rrmerge_buffer_buffer() throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_buffered(outputStream);
        outputStream.close();
    }

    void rrmerge_buffer_sizedBuffer(int bufferSize) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE, bufferSize);
        outputStream.create();

        round_robin_buffered(outputStream);
        outputStream.close();
    }

    void rrmerge_buffer_mmap(int bufferSize) throws IOException {
        MemoryMapOutputStream outputStream = new MemoryMapOutputStream(OUTPUT_FILE, bufferSize);
        outputStream.create();

        round_robin_buffered(outputStream);
        outputStream.close();
    }


    void rrmerge_mmap_byte() throws IOException {
        ByteOutputStream outputStream = new ByteOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_mmap(outputStream);
        outputStream.close();
    }

    void rrmerge_mmap_buffer() throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_buffered(outputStream);
        outputStream.close();
    }

    void rrmerge_mmap_sizedBuffer(int bufferSize) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE, bufferSize);
        outputStream.create();

        round_robin_mmap(outputStream);
        outputStream.close();
    }

    void rrmerge_mmap_mmap(int bufferSize) throws IOException {
        MemoryMapOutputStream outputStream = new MemoryMapOutputStream(OUTPUT_FILE, bufferSize);
        outputStream.create();

        round_robin_mmap(outputStream);
        outputStream.close();
    }

    private void round_robin_buffered(CustomOutputStream outputStream) throws IOException {
        List<BufferedInputStream> buffer_streams = new ArrayList<>();
        BufferedInputStream tmp = null;
        String line = "";
        int cpt = f.size();

        for (String file : f) {
            tmp = new BufferedInputStream(file);
            tmp.open();
            buffer_streams.add(tmp);
        }

        while (cpt > 0) {
            Iterator<BufferedInputStream> isIterator = buffer_streams.iterator();

            while (isIterator.hasNext()) {
                if ((line = isIterator.next().readln()) != null) {
                    outputStream.writeln(line + '\n');
                } else {
                    cpt--;
                    isIterator.remove();
                }
            }
        }

        outputStream.close();
    }

    private void round_robin_mmap(CustomOutputStream outputStream) throws IOException {
        List<MemoryMapInputStream> buffer_streams = new ArrayList<>();
        MemoryMapInputStream tmp = null;
        String line = null;
        int cpt = f.size();


        for (String file : f) {
            tmp = new MemoryMapInputStream(file, MMAP_INPUT_BUFFSIZE);
            tmp.open();
            buffer_streams.add(tmp);
        }

        while (cpt > 0) {
            Iterator<MemoryMapInputStream> isIterator = buffer_streams.iterator();

            while (isIterator.hasNext()) {
                if ((line = isIterator.next().readln()) != null) {
                    outputStream.writeln(line);
                } else {
                    cpt--;
                    isIterator.remove();
                }
            }
        }

        outputStream.close();
    }
}
