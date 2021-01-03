import java.io.*;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BufferUtils {

    private final static int REPEAT_AMOUNT = 3;
    private static List<String[]> dataLines = new ArrayList<>();

    public static void measureTimeFor(List<String> filesPath, int implementationNumber, List<Integer> bufferSizes, boolean sequential) throws IOException {
        switch (implementationNumber) {
            case 1:
            case 2:
                handleImplementationOneAndTwo(filesPath, implementationNumber, sequential);
                break;
            case 3:
            case 4:
                handleImplementationThreeAndFour(filesPath, implementationNumber, bufferSizes, sequential);
                break;
        }
    }

    private static void handleImplementationOneAndTwo(List<String> filesPath, int implementationNumber, boolean sequential) throws IOException {
        long startTime;
        double totalTime;
        double totalTime100;
        double totalTime1000;
        double totalTime10000;
        dataLines.clear();

        for (String file : filesPath) {
            System.out.println("Implementation " + implementationNumber + ", " + (sequential ? "sequential" : "random"));
            totalTime = 0;
            totalTime100 = 0;
            totalTime1000 = 0;
            totalTime10000 = 0;
            for (int repeatIdx = 0; repeatIdx < REPEAT_AMOUNT; repeatIdx++) {
                if (sequential) {
                    startTime = System.currentTimeMillis();
                    SequentialReading sequentialReading = new SequentialReading(file);

                    if (implementationNumber == 1) {
                        sequentialReading.length1();
                    } else if (implementationNumber == 2) {
                        sequentialReading.length2();
                    }

                    totalTime += getElapsedTime(startTime);
                } else {
                    for (int i = 0; i <= 2; i++) {
                        startTime = System.currentTimeMillis();
                        int j = (int) (100 * Math.pow(10, i));
                        RandomReading randomReading = new RandomReading(file, j);
                        System.out.println("j value: " + j);

                        if (implementationNumber == 1) {
                            randomReading.randJump1();
                        } else if (implementationNumber == 2) {
                            randomReading.randJump2();
                        }

                        if (i == 0) {
                            totalTime100 += getElapsedTime(startTime);
                        } else if (i == 1) {
                            totalTime1000 += getElapsedTime(startTime);
                        } else {
                            totalTime10000 += getElapsedTime(startTime);
                        }
                    }
                }
            }

            if (sequential) {
                double avgTime = (totalTime / (double) REPEAT_AMOUNT);
                dataLines.add(new String[]{Double.toString(avgTime)});
                System.out.println("Filename: '" + file + "', elapsed time average: " + Double.toString(avgTime));
                System.out.println("---------------------------------------------------------------\n");
            } else {
                double avgTime100 = (totalTime100 / (double) REPEAT_AMOUNT);
                double avgTime1000 = (totalTime1000 / (double) REPEAT_AMOUNT);
                double avgTime10000 = (totalTime10000 / (double) REPEAT_AMOUNT);
                dataLines.add(new String[]{
                                Double.toString(avgTime100),
                                Double.toString(avgTime1000),
                                Double.toString(avgTime10000)}
                );
                System.out.println(
                        "Filename: '" + file +
                                ", avg j = 100: " + Double.toString(avgTime100) +
                                ", avg j 1000: " + Double.toString(avgTime1000) +
                                ", avg j 10000: " + Double.toString(avgTime10000));
                System.out.println("---------------------------------------------------------------\n");
            }
        }

        saveDataToCSV(implementationNumber, sequential ? "sequential" : "random", -1);
    }

    private static void handleImplementationThreeAndFour(List<String> filesPath, int implementationNumber, List<Integer> bufferSizes, boolean sequential) throws IOException {
        long startTime;
        double totalTime;
        double totalTime100;
        double totalTime1000;
        double totalTime10000;

        for (int bufferSize : bufferSizes) {
            dataLines.clear();
            for (String file : filesPath) {
                System.out.println("Implementation " + implementationNumber + ", " + (sequential ? "sequential" : "random"));
                totalTime = 0;
                totalTime100 = 0;
                totalTime1000 = 0;
                totalTime10000 = 0;
                for (int repeatIdx = 0; repeatIdx < REPEAT_AMOUNT; repeatIdx++) {
                    startTime = System.currentTimeMillis();
                    if (sequential) {
                        SequentialReading sequentialReading = new SequentialReading(file);

                        if (implementationNumber == 3) {
                            sequentialReading.length3(bufferSize);
                        } else if (implementationNumber == 4) {
                            sequentialReading.length4(bufferSize);
                        }

                        System.out.println("Buffer size: " + bufferSize);
                        totalTime += getElapsedTime(startTime);
                    } else {
                        for (int i = 0; i <= 2; i++) {
                            startTime = System.currentTimeMillis();
                            int j = (int) (100 * Math.pow(10, i));
                            RandomReading randomReading = new RandomReading(file, j);
                            System.out.println("j value: " + j);

                            if (implementationNumber == 3) {
                                randomReading.randJump3(bufferSize);
                            } else if (implementationNumber == 4) {
                                randomReading.randJump4(bufferSize);
                            }

                            System.out.println("Buffer size: " + bufferSize);
                            if (i == 0) {
                                totalTime100 += getElapsedTime(startTime);
                            } else if (i == 1) {
                                totalTime1000 += getElapsedTime(startTime);
                            } else {
                                totalTime10000 += getElapsedTime(startTime);
                            }
                        }
                    }
                }

                if (sequential) {
                    double avgTime = (totalTime / (double) REPEAT_AMOUNT);
                    dataLines.add(new String[]{Double.toString(avgTime)});
                    System.out.println("Filename: '" + file + "', elapsed time average: " + Double.toString(avgTime));
                    System.out.println("---------------------------------------------------------------\n");
                } else {
                    double avgTime100 = (totalTime100 / (double) REPEAT_AMOUNT);
                    double avgTime1000 = (totalTime1000 / (double) REPEAT_AMOUNT);
                    double avgTime10000 = (totalTime10000 / (double) REPEAT_AMOUNT);
                    dataLines.add(new String[]{
//                            file,
                                    Double.toString(avgTime100),
                                    Double.toString(avgTime1000),
                                    Double.toString(avgTime10000)}
                    );
                    System.out.println(
                            "Filename: '" + file + "', buffer: " + bufferSize +
                                    ", avg j = 100: " + Double.toString(avgTime100) +
                                    ", avg j 1000: " + Double.toString(avgTime1000) +
                                    ", avg j 10000: " + Double.toString(avgTime10000));
                    System.out.println("---------------------------------------------------------------\n");
                }
            }

            saveDataToCSV(implementationNumber, sequential ? "sequential" : "random", bufferSize);
        }
    }

    private static double getElapsedTime(long startTime) {
        long stopTime;
        long elapsedTime;
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;

        return elapsedTime;
    }

    private static void saveDataToCSV(int implement, String type, int buffer) {
        File csvOutputFile = new File((type + "_" + getImplementName(implement)) + (buffer != -1 ? ("_" + buffer) : "") + ".csv");

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(BufferUtils::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveRrmergeDataToCSV(int implement, String type, int buffer, String textToAppend) {
        File csvOutputFile = new File((type + "_" + getImplementName(implement)) + (buffer != -1 ? ("_" + buffer) : "") + ".csv");

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(
                    new FileWriter(csvOutputFile, true));

            writer.write(textToAppend + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getImplementName(int implement) {
        switch (implement) {
            case 1:
                return "byte";
            case 2:
            case 3:
                return "buffered";
            case 4:
                return "mmap";
        }

        return null;
    }

    public static String convertToCSV(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(","));
    }

    public static void measureTimeForRrmerge(List<String> filesPath, List<Integer> bufferSizes, int x, int y) throws IOException {
        switch (y) {
            case 1:
            case 2:
                benchmarkRrmergeWithoutBuffer(filesPath, x, y);
                break;
            case 3:
            case 4:
                benchmarkRrmergeWithBuffer(filesPath, bufferSizes, x, y);
                break;
        }
    }

    public static void benchmarkRrmergeWithoutBuffer(List<String> filesPath, int x, int y) {
        long startTime;
        double totalTime;
        System.out.println("Implementation rrmerge, x: " + x + ", y: " + y + ", files: " + filesPath.toString());

        try {
            Rrmerge rrmerge = new Rrmerge(filesPath);

            totalTime = 0;

            for (int repeatIdx = 0; repeatIdx < REPEAT_AMOUNT; repeatIdx++) {
                startTime = System.currentTimeMillis();

                switch (x) {
                    case 2:
                        switch (y) {
                            case 1:
                                rrmerge.rrmerge_buffer_byte();
                                break;
                            case 2:
                                rrmerge.rrmerge_buffer_buffer();
                                break;
                        }
                        break;
                    case 4:
                        switch (y) {
                            case 1:
                                rrmerge.rrmerge_mmap_byte();
                                break;
                            case 2:
                                rrmerge.rrmerge_mmap_buffer();
                                break;
                        }
                        break;
                }

                totalTime += getElapsedTime(startTime);
                System.out.println(totalTime);
            }

            double avgTime = (totalTime / (double) REPEAT_AMOUNT);
            dataLines.add(new String[]{Double.toString(avgTime)});
            System.out.println("Merging elapsed time: " + Double.toString(avgTime));
            System.out.println("---------------------------------------------------------------\n");
            saveDataToCSV(y, "rrmerge" + (x == 2 ? "_buffered" : "_mmap"), -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void benchmarkRrmergeWithBuffer(List<String> filesPath, List<Integer> bufferSizes, int x, int y) {
        long startTime;
        double totalTime;
        dataLines.clear();

        try {
            Rrmerge rrmerge = new Rrmerge(filesPath);

            for (int bufferSize : bufferSizes) {
                System.out.println("Implementation rrmerge, x: " + x + ", y: " + y + ", files: " + filesPath.toString() + ", buffer: " + bufferSize);
                dataLines.clear();
                totalTime = 0;

                for (int repeatIdx = 0; repeatIdx < REPEAT_AMOUNT; repeatIdx++) {
                    startTime = System.currentTimeMillis();

                    switch (x) {
                        case 2:
                            switch (y) {
                                case 3:
                                    rrmerge.rrmerge_buffer_sizedBuffer(bufferSize);
                                    break;
                                case 4:
                                    rrmerge.rrmerge_buffer_mmap(bufferSize);
                                    break;
                            }
                            break;
                        case 4:
                            switch (y) {
                                case 3:
                                    rrmerge.rrmerge_mmap_sizedBuffer(bufferSize);
                                    break;
                                case 4:
                                    rrmerge.rrmerge_mmap_mmap(bufferSize);
                                    break;
                            }
                            break;
                    }

                    totalTime += getElapsedTime(startTime);
                    System.out.println(totalTime);
                }

                double avgTime = (totalTime / (double) REPEAT_AMOUNT);

                System.out.println("Merging elapsed time: " + Double.toString(avgTime) + ", buffer: " + bufferSize);
                System.out.println("---------------------------------------------------------------\n");
                String lineToSave = Double.toString(avgTime);
                saveRrmergeDataToCSV(y, "rrmerge" + (x == 2 ? "_buffered" : "_mmap"), bufferSize, lineToSave);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearDataLines() {
        dataLines.clear();
    }

}
