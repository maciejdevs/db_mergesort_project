import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Rrmerge {

    private List<String> f;
    private List<Integer> buffer_sizes;
    private final int MMAP_INPUT_BUFFSIZE = 8192;
    private final String OUTPUT_FILE = "src/output_file.txt";

    public Rrmerge(List<String> f, List<Integer> buffer_sizes) {
        this.f = f;
        this.buffer_sizes = buffer_sizes;
    }

    void rrmerge_buffer_byte() throws IOException {
        ByteOutputStream outputStream = new ByteOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_buffered(outputStream);
    }

    void rrmerge_buffer_buffer() throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_buffered(outputStream);
    }

    void rrmerge_buffer_sizedBuffer() throws IOException {
        for(int bufferSize : buffer_sizes) {
            BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE, bufferSize);
            outputStream.create();
            round_robin_buffered(outputStream);
        }
    }

    void rrmerge_buffer_mmap() throws IOException {
        // TODO : Check le size du buffer de memeory map keskisposse ?
        MemoryMapOutputStream outputStream = new MemoryMapOutputStream(OUTPUT_FILE, 4);
        outputStream.create();

        round_robin_buffered(outputStream);
    }


    void rrmerge_mmap_byte() throws IOException {
        ByteOutputStream outputStream = new ByteOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_mmap(outputStream);
    }

    void rrmerge_mmap_buffer() throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE);
        outputStream.create();

        round_robin_buffered(outputStream);
    }

    void rrmerge_mmap_sizedBuffer() throws IOException {
        for(int bufferSize : buffer_sizes) {
            BufferedOutputStream outputStream = new BufferedOutputStream(OUTPUT_FILE, bufferSize);
            outputStream.create();
            round_robin_mmap(outputStream);
        }
    }

    void rrmerge_mmap_mmap() throws IOException {
        MemoryMapOutputStream outputStream = new MemoryMapOutputStream(OUTPUT_FILE, 4);
        outputStream.create();

        round_robin_mmap(outputStream);
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

        while(cpt > 0) {
            for (BufferedInputStream buffer_stream : buffer_streams) {
                if ((line = buffer_stream.readln()) != null) {
                    outputStream.writeln(line + "\n");
                } else {
                    cpt--;
                }
            }
        }

        outputStream.close();
    }

    private void round_robin_mmap(CustomOutputStream outputStream) throws IOException {
        List<MemoryMapInputStream> buffer_streams = new ArrayList<>();
        MemoryMapInputStream tmp = null;
        String line = "";
        int cpt = f.size();


        for (String file : f) {
            tmp = new MemoryMapInputStream(file, MMAP_INPUT_BUFFSIZE);
            tmp.open();
            buffer_streams.add(tmp);
        }

        while(cpt > 0) {
            for (MemoryMapInputStream buffer_stream : buffer_streams) {
                if ((line = buffer_stream.readln()) != null) {
                    outputStream.writeln(line);
                } else {
                    cpt--;
                }
            }
        }

        outputStream.close();
    }


}
