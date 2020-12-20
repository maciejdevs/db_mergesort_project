import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
//
//            ByteOutputStream os = new ByteOutputStream("src/create.txt");
//            os.create();
//
//            ByteInputStream is = new ByteInputStream("src/file.txt");
//            is.open();
//            is.seek(3, true);
//            os.writeln(is.readln());
//
//            os.close();


//            BufferedOutputStream os = new BufferedOutputStream("src/create.txt");
//            os.create();
//
//            BufferedInputStream is = new BufferedInputStream("src/file.txt", 10);
//            is.open();
//            System.out.println(is.readln());
//            System.out.println(is.readln());
//            System.out.println(is.readln());
//            os.writeln(is.readln());
//            os.writeln(is.readln());
//            is.seek(3,true);
//            os.writeln(is.readln());
//            is.seek(5,true);
//            os.writeln(is.readln());

//            os.close();


//            MemoryMapInputStream is = new MemoryMapInputStream("src/file.txt", 3);
//            is.open();
//            System.out.print(is.readln());
//            System.out.print(is.readln());
//            is.seek(2, true);
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

//            long startTime = System.currentTimeMillis();
////
////            SequentialReading sequentialReading = new SequentialReading("src/imdb/test.csv");
//            SequentialReading sequentialReading = new SequentialReading("src/imdb/movie_companies.csv");
//            sequentialReading.length4(8192);
//
////            RandomReading randomReading = new RandomReading("src/imdb/company_name.csv", 100);
////            randomReading.randJump3();
//
//            long stopTime = System.currentTimeMillis();
//            long elapsedTime = stopTime - startTime;
//            double elapsedTimeInSecond = (double) elapsedTime / 1000;
//            System.out.println(elapsedTimeInSecond);

//            List<String> files = Arrays.asList(
//                    "src/imdb/comp_cast_type.csv",
//                    "src/imdb/kind_type.csv",
//                    "src/imdb/company_type.csv",
//                    "src/imdb/role_type.csv",
//                    "src/imdb/link_type.csv",
//                    "src/imdb/info_type.csv",
//                    "src/imdb/company_name_short.csv",
//                    "src/imdb/movie_link.csv",
//                    "src/imdb/complete_cast.csv"
//            );

                        List<String> files = Arrays.asList(
                    "src/imdb/comp_cast_type.csv",
                    "src/imdb/kind_type.csv",
                    "src/imdb/company_type.csv",
                    "src/imdb/role_type.csv",
                    "src/imdb/link_type.csv",
                    "src/imdb/info_type.csv",
                    "src/imdb/company_name_short.csv",
                    "src/imdb/movie_link.csv",
                    "src/imdb/complete_cast.csv",
                    "src/imdb/keyword.csv",
                    "src/imdb/company_name.csv",
                    "src/imdb/movie_info_idx.csv",
                    "src/imdb/aka_title.csv",
                    "src/imdb/aka_name.csv",
                    "src/imdb/movie_companies.csv",
                    "src/imdb/movie_keyword.csv",
                    "src/imdb/title.csv",
                    "src/imdb/char_name.csv",
                    "src/imdb/name.csv",
                    "src/imdb/person_info.csv",
                    "src/imdb/movie_info.csv",
                    "src/imdb/cast_info.csv"
            );

//            List<String> files = Arrays.asList(
//                    "src/imdb/company_type.csv",
//                    "src/imdb/company_name.csv",
//                    "src/imdb/name.csv"
//            );

            List<Integer> bufferSizes = Arrays.asList(
                    8191
            );

//             Test implement 1 sequential
//            BufferUtils.measureTimeFor(files, 1, null, true);

//             Test implement 2 sequential
//            BufferUtils.measureTimeFor(files, 2,null, true);

//             Test implement 3 sequential
//            BufferUtils.measureTimeFor(files, 3, bufferSizes, true);

//             Test implement 4 sequential
//            BufferUtils.measureTimeFor(files, 4, bufferSizes, true);

            // Test implement 1 random
//            BufferUtils.measureTimeFor(files, 1, null, false);

            // Test implement 2 random
//            BufferUtils.measureTimeFor(files, 2,null, false);

            // Test implement 3 random
//            BufferUtils.measureTimeFor(files, 3, bufferSizes, false);

            BufferUtils.measureTimeFor(files, 4, bufferSizes, false);

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
