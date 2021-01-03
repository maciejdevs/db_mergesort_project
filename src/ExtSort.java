import java.io.File;
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
    private int temp_file_index;

    public ExtSort(String f, int k, int M, int d){
        this.f = f;
        this.k = k;
        this.M = M;
        this.d = d;
        this.buffer = new ArrayList<>();
        this.is = new BufferedInputStream(f);
        this.os = null;
        this.queue = new ArrayList<>();
        temp_file_index = 0;
    }


    void mergesort() throws IOException {
        String tmp = "";
        int buff_bytes_read = 0;
        int bytes_read = 0;

        is.open();

        while(bytes_read < is.getFileSize()) {
            while (tmp != null && buff_bytes_read < M) {
                tmp = is.readln();
                if (tmp != null) {
                    buff_bytes_read += tmp.length() + 2; // readLn does not read \n and \r, we have to increment the counter by ourselves
                    buffer.add(tmp + "\n");
                }
            }
            bytes_read += buff_bytes_read;
            buff_bytes_read = 0;

            this.buffer = sort(this.buffer);

            //System.out.println("Bytes read : " + bytes_read + " | File Size : " + is.getFileSize());

            os = new BufferedOutputStream("src/tmp/temp"+ temp_file_index +".csv");
            queue.add(new BufferedInputStream("src/tmp/temp"+ temp_file_index +".csv"));
            os.create();

            for (String line : buffer) {
                os.writeln(line);
            }

            os.close();
            buffer.clear();
            temp_file_index++;
        }

        sortQueue();

    }

    void sortQueue() throws IOException {
        String line = "";
        List<String> all_lines = new ArrayList<>();
        BufferedInputStream is_tmp;

        while(queue.size() > 1) {
            if(queue.size() >= d) {
                for (int i = 0; i < d; i++) {
                    is_tmp = queue.get(i);
                    is_tmp.open();
                    while ((line = is_tmp.readln()) != null) {
                        all_lines.add(line);
                    }
                }

                for (int i = 0; i < d; i++) {
                    queue.remove(0);
                }

            } else {
                for(int i = 0; i < queue.size(); i++){
                    is_tmp = queue.get(i);
                    is_tmp.open();
                    while ((line = is_tmp.readln()) != null) {
                        all_lines.add(line);
                    }
                }

                for (int i = 0; i < queue.size() + 1; i++) {
                    queue.remove(0);
                }
            }

            all_lines = sort(all_lines);

            os = new BufferedOutputStream("src/tmp/temp" + temp_file_index + ".csv");
            queue.add(new BufferedInputStream("src/tmp/temp" + temp_file_index + ".csv"));
            os.create();

            for (String tmp_line : all_lines) {
                os.writeln(tmp_line + "\n");
            }

            os.close();
            all_lines.clear();
            temp_file_index++;
        }

        os = new BufferedOutputStream("src/tmp/output.csv");
        os.create();

        String tmp_line2 = "";
        BufferedInputStream tmp_is = queue.get(0);
        tmp_is.open();
        while((tmp_line2 = tmp_is.readln()) != null){
            all_lines.add(tmp_line2);
        }

        for (String tmp_line : all_lines) {
            os.writeln(tmp_line + "\n");
        }

        os.close();
    }

    void deleteTmp(){
        for(int i = 0; i <= temp_file_index; i++) {
            File file = new File("src/tmp/temp" + temp_file_index + ".csv");
            file.delete();
        }
    }

    List<String> sort(List<String> buffer){
        //String[] cols = buffer.get(0).split(",");

        List<String[] > cols = new ArrayList<>();

        for(int i = 0; i < buffer.size(); i++){
            cols.add(buffer.get(i).split(","));
        }

        SortedMap<Integer, Integer> tmp_int_sort = new TreeMap<>();
        SortedMap<String, Integer> tmp_sort = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        boolean isInteger = false; //to check if we need to sort on an integer or a string

        //System.out.println("Cols size :" + cols.size());

        for(int j = 0; j < cols.size(); j++){
            if(isInteger){
                tmp_int_sort.put(Integer.valueOf(cols.get(j)[k]), j);
            } else {
                tmp_sort.put(cols.get(j)[k], j);
            }
        }

        String tmp = "";
        List<String> tmp_buffer = new ArrayList<>();

        if(isInteger){
            for(Map.Entry entry : tmp_int_sort.entrySet()) {
                int line = (int) entry.getValue();
                tmp = buffer.get(line);
                tmp_buffer.add(tmp);
            }
        } else {
            for (Map.Entry entry : tmp_sort.entrySet()) {
                int line = (int) entry.getValue();
                tmp = buffer.get(line);
                tmp_buffer.add(tmp);
            }
        }

        buffer.clear();
        buffer.addAll(tmp_buffer);

        return buffer;
    }

}