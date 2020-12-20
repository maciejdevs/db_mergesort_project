import java.io.IOException;
import java.util.*;

public class ExtSort {

    private String f;
    private int k;
    private int M;
    private int d;
    private List<String> buffer;
    private BufferedInputStream is;
    private BufferedOutputStream os;
    private List<BufferedInputStream> queue;

    public ExtSort(String f, int k, int M, int d){
        this.f = f;
        this.k = k;
        this.M = M;
        this.d = d;
        this.buffer = new ArrayList<>();
        this.is = new BufferedInputStream(f);
        this.os = null;
        this.queue = new ArrayList<>();
    }


    void mergesort() throws IOException {
        String tmp = "";
        int buff_bytes_read = 0;
        int bytes_read = 0;
        int i = 0;

        is.open();

        while(bytes_read < is.getFileSize()) {
            while (tmp != null && buff_bytes_read < M) {
                tmp = is.readln();
                if (tmp != null) {
                    buff_bytes_read += tmp.length();
                    buffer.add(tmp);
                }
            }
            bytes_read += buff_bytes_read;
            buff_bytes_read = 0;

            sort();

            os = new BufferedOutputStream("src/tmp/temp"+ i +".txt");
            queue.add(new BufferedInputStream("src/tmp/temp"+ i +".txt"));
            os.create();

            for (String line : buffer) {
                os.writeln(line);
            }

            os.close();
            i++;
        }

    }

    void sort(){
        //String[] cols = buffer.get(0).split(",");

        List<String[] > cols = new ArrayList<>();

        for(int i = 0; i < buffer.size(); i++){
            cols.add(buffer.get(i).split(","));
        }

        SortedMap<String, Integer> tmp_sort = new TreeMap<String, Integer>();

        for(int j = 0; j < cols.size(); j++){
            tmp_sort.put(cols.get(j)[k], j);
        }

        String tmp = "";
        List<String> tmp_buffer = new ArrayList<>();

        for(Map.Entry entry : tmp_sort.entrySet()) {
            String variable = (String) entry.getKey();
            int line = (int) entry.getValue();
            tmp = buffer.get(line);
            tmp_buffer.add(tmp);
        }

        buffer.clear();
        buffer.addAll(tmp_buffer);

    }

}