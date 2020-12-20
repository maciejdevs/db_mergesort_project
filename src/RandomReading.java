import java.io.IOException;

public class RandomReading {

    private String path;
    private int j;

    public RandomReading(String path, int j) {
        this.path = path;
        this.j = j;
    }

    void randJump1() throws IOException {
        int sum = 0;
        int p = 0;
        int l = 0;
        ByteInputStream is = new ByteInputStream(path);
        is.open();

        for (int i = 0; i < j; i++) {
            p = (int) (Math.random() * is.getFileSize());
            is.seek(p, true);
            l = is.readln().length();
            sum += l;
        }

        System.out.println("Sum: " + sum);
    }

    void randJump2() throws IOException {
        int sum = 0;
        int p = 0;
        int l = 0;
        String line = "";
        BufferedInputStream is = new BufferedInputStream(path);
        is.open();

        for (int i = 0; i < j; i++) {
            p = (int) (Math.random() * is.getFileSize());
            is.seek(p, true);
            if ((line = is.readln()) != null) {
                l = line.length();
                sum += l;
            }
        }

        System.out.println("Sum: " + sum);
    }

    void randJump3(int bufferSize) throws IOException {
        int sum = 0;
        int p = 0;
        int l = 0;
        String line = "";
        BufferedInputStream is = new BufferedInputStream(path, bufferSize);
        is.open();

        for (int i = 0; i < j; i++) {
            p = (int) (Math.random() * is.getFileSize());
            is.seek(p, true);
            if ((line = is.readln()) != null) {
                l = line.length();
                sum += l;
            }
        }

        System.out.println("Sum: " + sum);
    }

    void randJump4(int bufferSize) throws IOException {
        int sum = 0;
        int p = 0;
        int l = 0;
        MemoryMapInputStream is = new MemoryMapInputStream(path, bufferSize);
        is.open();

        for (int i = 0; i < j; i++) {
//            p = (int) (Math.random() * is.getFileSize());
            p = (int) (Math.random() * is.getFileSize());
            is.seek(p, true);
            l = is.readln().length();
//            System.out.println(i);
            sum += l;
        }

        System.out.println("Sum: " + sum);
    }

}
