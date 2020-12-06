public class Main {
    public static void main(String[] args) {

        try {
//            BufferedInputStream in = new BufferedInputStream("src/file.txt");
//            in.open();
//
//            System.out.println(in.readln());
//            System.out.println(in.readln());
//            in.endofstream();
//            System.out.println(in.readln());

//            ByteOutputStream os = new ByteOutputStream("src/create.txt");
//            os.create();
//
//            ByteInputStream is = new ByteInputStream("src/file.txt");
//            is.open();
//            os.writeln(is.readln());
//
//            os.close();

            /**
            BufferedOutputStream os = new BufferedOutputStream("src/create.txt", 10);
            os.create();

            BufferedInputStream is = new BufferedInputStream("src/file.txt", 10);
            is.open();
            os.writeln(is.readln());
            //is.seek(3,true);
            os.writeln(is.readln());
            os.writeln(is.readln());
            os.writeln(is.readln());
            os.writeln(is.readln());

            os.close();
             **/

//            MemoryMapInputStream is = new MemoryMapInputStream("src/file.txt", 10);
//            is.open();
//            System.out.print(is.readln());
//            System.out.print(is.readln());
//            System.out.print(is.readln());
//            System.out.print(is.readln());
//            System.out.print(is.readln());
//            System.out.print(is.readln());

//            BufferedInputStream is = new BufferedInputStream("src/file.txt", 10);
//            MemoryMapInputStream is = new MemoryMapInputStream("src/file.txt", 10);
//            MemoryMapOutputStream os = new MemoryMapOutputStream("src/create.txt");
//
//            is.open();
//            os.create();
//
//            String tmp = is.readln();
//            System.out.print(tmp);
//            os.writeln(tmp);
//
//            tmp = is.readln();
//            System.out.print(tmp);
//            os.writeln(tmp);
//
//            tmp = is.readln();
//            System.out.print(tmp);
//            os.writeln(tmp);
//
//            tmp = is.readln();
//            System.out.print(tmp);
//            os.writeln(tmp);
//
//            os.close();

            long startTime = System.currentTimeMillis();

            SequentialReading sequentialReading = new SequentialReading("src/imdb/company_name.csv");
            sequentialReading.length4();

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            double elapsedTimeInSecond = (double) elapsedTime / 1000;
            System.out.println(elapsedTimeInSecond);

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
