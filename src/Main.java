import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private final static List<String> allFiles = Arrays.asList(
            "src/imdb/comp_cast_type.csv",
            "src/imdb/kind_type.csv",
            "src/imdb/company_type.csv",
            "src/imdb/role_type.csv",
            "src/imdb/link_type.csv",
            "src/imdb/info_type.csv",
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

    private final static List<String> bigFiles = Arrays.asList(
            "src/imdb/char_name.csv",
            "src/imdb/name.csv",
            "src/imdb/person_info.csv"
    );

    private final static List<String> mediumFiles = Arrays.asList(
            "src/imdb/aka_title.csv",
            "src/imdb/movie_companies.csv",
            "src/imdb/title.csv"
    );

    private final static List<String> smallFiles = Arrays.asList(
            "src/imdb/kind_type.csv",
            "src/imdb/movie_link.csv",
            "src/imdb/movie_info_idx.csv"
    );

    private final static List<String> mixedFiles = Arrays.asList(
            "src/imdb/kind_type.csv",
            "src/imdb/movie_link.csv",
            "src/imdb/aka_title.csv",
            "src/imdb/person_info.csv"
    );

    private final static List<String> firstImplementFiles = Arrays.asList(
            "src/imdb/comp_cast_type.csv",
            "src/imdb/kind_type.csv",
            "src/imdb/company_type.csv",
            "src/imdb/role_type.csv",
            "src/imdb/link_type.csv",
            "src/imdb/info_type.csv",
            "src/imdb/movie_link.csv",
            "src/imdb/complete_cast.csv"
    );

    private final static List<String> mergeSortTestFiles = Arrays.asList(
            //"src/imdb/char_name.csv",
            "src/imdb/aka_title.csv",
            "src/imdb/keyword.csv",
            "src/imdb/movie_link.csv"
    );

    private final static List<Integer> bufferSizes = Arrays.asList(
            64, 2048, 8200, 16384
    );

    private final static List<Integer> mergeSortBufferSizes = Arrays.asList(
            2048, 8192, 16384, 65536
    );

    private final static List<Integer> mergeSortStreams = Arrays.asList(
            8, 12, 16, 18, 20
    );


    private final static List<Integer> rrmergeBufferSizes = Arrays.asList(
            200 * 1024, 2 * 1024 * 1024, 20 * 1024 * 1024
    );

    public static void main(String[] args) {
        try {
            testExperimentReadingSequential();
            testExperimentReadingRandom();
            testExperimentCombinedReadWrite();

            testExperimentMergeSort();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private static void testExperimentReadingSequential() throws IOException {
        BufferUtils.measureTimeFor(firstImplementFiles, 1, null, true);
        BufferUtils.measureTimeFor(allFiles, 2, null, true);
        BufferUtils.measureTimeFor(allFiles, 3, bufferSizes, true);
        BufferUtils.measureTimeFor(allFiles, 4, bufferSizes, true);
    }

    private static void testExperimentReadingRandom() throws IOException {
        BufferUtils.measureTimeFor(allFiles, 1, null, false);
        BufferUtils.measureTimeFor(allFiles, 2, null, false);
        BufferUtils.measureTimeFor(allFiles, 3, bufferSizes, false);
        BufferUtils.measureTimeFor(allFiles, 4, bufferSizes, false);
    }

    private static void testExperimentCombinedReadWrite() throws IOException {
        BufferUtils.clearDataLines();
        BufferUtils.measureTimeForRrmerge(smallFiles, null, 2, 1);
        BufferUtils.measureTimeForRrmerge(mediumFiles, null, 2, 1);
        BufferUtils.measureTimeForRrmerge(bigFiles, null, 2, 1);
        BufferUtils.measureTimeForRrmerge(mixedFiles, null, 2, 1);
        BufferUtils.clearDataLines();
        BufferUtils.measureTimeForRrmerge(smallFiles, null, 2, 2);
        BufferUtils.measureTimeForRrmerge(mediumFiles, null, 2, 2);
        BufferUtils.measureTimeForRrmerge(bigFiles, null, 2, 2);
        BufferUtils.measureTimeForRrmerge(mixedFiles, null, 2, 2);
        BufferUtils.clearDataLines();
        BufferUtils.measureTimeForRrmerge(smallFiles, rrmergeBufferSizes, 2, 3);
        BufferUtils.measureTimeForRrmerge(mediumFiles, rrmergeBufferSizes, 2, 3);
        BufferUtils.measureTimeForRrmerge(bigFiles, rrmergeBufferSizes, 2, 3);
        BufferUtils.measureTimeForRrmerge(mixedFiles, rrmergeBufferSizes, 2, 3);
        BufferUtils.clearDataLines();
        BufferUtils.measureTimeForRrmerge(smallFiles, rrmergeBufferSizes, 2, 4);
        BufferUtils.measureTimeForRrmerge(mediumFiles, rrmergeBufferSizes, 2, 4);
        BufferUtils.measureTimeForRrmerge(bigFiles, rrmergeBufferSizes, 2, 4);
        BufferUtils.measureTimeForRrmerge(mixedFiles, rrmergeBufferSizes, 2, 4);
        BufferUtils.clearDataLines();

        BufferUtils.measureTimeForRrmerge(smallFiles, null, 4, 1);
        BufferUtils.measureTimeForRrmerge(mediumFiles, null, 4, 1);
        BufferUtils.measureTimeForRrmerge(bigFiles, null, 4, 1);
        BufferUtils.measureTimeForRrmerge(mixedFiles, null, 4, 1);

        BufferUtils.measureTimeForRrmerge(smallFiles, null, 4, 2);
        BufferUtils.measureTimeForRrmerge(mediumFiles, null, 4, 2);
        BufferUtils.measureTimeForRrmerge(bigFiles, null, 4, 2);
        BufferUtils.measureTimeForRrmerge(mixedFiles, null, 4, 2);

        BufferUtils.measureTimeForRrmerge(smallFiles, rrmergeBufferSizes, 4, 3);
        BufferUtils.measureTimeForRrmerge(mediumFiles, rrmergeBufferSizes, 4, 3);
        BufferUtils.measureTimeForRrmerge(bigFiles, rrmergeBufferSizes, 4, 3);
        BufferUtils.measureTimeForRrmerge(mixedFiles, rrmergeBufferSizes, 4, 3);

        BufferUtils.measureTimeForRrmerge(smallFiles, rrmergeBufferSizes, 4, 4);
        BufferUtils.measureTimeForRrmerge(mediumFiles, rrmergeBufferSizes, 4, 4);
        BufferUtils.measureTimeForRrmerge(bigFiles, rrmergeBufferSizes, 4, 4);
        BufferUtils.measureTimeForRrmerge(mixedFiles, rrmergeBufferSizes, 4, 4);
    }

    private static void testExperimentMergeSort() throws IOException {
        long start;
        long finish;
        long timeElapsed;

        int totalTime = 0;
        double averageTime = 0;
        String results = "";

        for (String f : mergeSortTestFiles) {
            for (int M : mergeSortBufferSizes) {
                for (int d : mergeSortStreams) {
                    for (int i = 0; i < 10; i++) {
                        start = System.currentTimeMillis();

                        ExtSort extSort = new ExtSort(f, 1, M, d);
                        extSort.mergesort();

                        finish = System.currentTimeMillis();
                        timeElapsed = finish - start;
                        totalTime += timeElapsed;

                        System.out.println("f : " + f);
                        System.out.println("M : " + M);
                        System.out.println("d : " + d);
                        System.out.println("Elapsed time : " + timeElapsed + " ms");
                        extSort.deleteTmp();
                    }
                    averageTime = totalTime / 10;
                    totalTime = 0;
                    results += "File : " + f + " \n";
                    results += "File size " + Files.size(Paths.get(f)) + " \n";
                    results += "Buffer Size : " + M + "\n";
                    results += "Merge streams : " + d + "\n";
                    results += "Average Time : " + averageTime + " ms \n";
                    results += " ----------------------------------------- \n \n";
                }
            }
        }

        File file = new File("src/results.txt");
        FileWriter fw = new FileWriter(file);
        fw.write(results);
        fw.close();
    }

}
