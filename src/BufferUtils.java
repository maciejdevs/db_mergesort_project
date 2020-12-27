import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

    /**
     * Check if the string contains the entire next line.
     *
     * @param tmpNextLine the string that will be checked.
     * @return true if the string contains at least two end of lines, false otherwise.
     */
    public static boolean containsNextLine(String tmpNextLine) {
        int endLinesCounter = 0;

        for (int i = 0; i < tmpNextLine.length(); i++) {
            if (tmpNextLine.charAt(i) == '\n') {
                endLinesCounter++;
            }
        }

        return endLinesCounter > 1;
    }

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

                    totalTime += getElapsedTime(startTime, file);
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
                            totalTime100 += getElapsedTime(startTime, file);
                        } else if (i == 1) {
                            totalTime1000 += getElapsedTime(startTime, file);
                        } else {
                            totalTime10000 += getElapsedTime(startTime, file);
                        }
                    }
                }
            }

            if (sequential) {
                double avgTime = (totalTime / (double) REPEAT_AMOUNT);
                avgTime = (double) Math.round(avgTime * 100000d) / 100000d;
                dataLines.add(new String[]{file, Double.toString(avgTime)});
                System.out.println("Filename: '" + file + "', elapsed time average: " + Double.toString(avgTime));
                System.out.println("---------------------------------------------------------------\n");
            } else {
                double avgTime100 = (totalTime100 / (double) REPEAT_AMOUNT);
                double avgTime1000 = (totalTime1000 / (double) REPEAT_AMOUNT);
                double avgTime10000 = (totalTime10000 / (double) REPEAT_AMOUNT);
                avgTime100 = (double) Math.round(avgTime100 * 100000d) / 100000d;
                avgTime1000 = (double) Math.round(avgTime1000 * 100000d) / 100000d;
                avgTime10000 = (double) Math.round(avgTime10000 * 100000d) / 100000d;
                dataLines.add(new String[]{
                        file,
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
                        totalTime += getElapsedTime(startTime, file);
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
                                totalTime100 += getElapsedTime(startTime, file);
                            } else if (i == 1) {
                                totalTime1000 += getElapsedTime(startTime, file);
                            } else {
                                totalTime10000 += getElapsedTime(startTime, file);
                            }
                        }
                    }
                }

                if (sequential) {
                    double avgTime = (totalTime / (double) REPEAT_AMOUNT);
                    avgTime = (double) Math.round(avgTime * 100000d) / 100000d;
                    dataLines.add(new String[]{file, Double.toString(avgTime)});
                    System.out.println("Filename: '" + file + "', elapsed time average: " + Double.toString(avgTime));
                    System.out.println("---------------------------------------------------------------\n");
                } else {
                    double avgTime100 = (totalTime100 / (double) REPEAT_AMOUNT);
                    double avgTime1000 = (totalTime1000 / (double) REPEAT_AMOUNT);
                    double avgTime10000 = (totalTime10000 / (double) REPEAT_AMOUNT);
                    avgTime100 = (double) Math.round(avgTime100 * 100000d) / 100000d;
                    avgTime1000 = (double) Math.round(avgTime1000 * 100000d) / 100000d;
                    avgTime10000 = (double) Math.round(avgTime10000 * 100000d) / 100000d;
                    dataLines.add(new String[]{
                            file,
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

    private static double getElapsedTime(long startTime, String file) {
        long stopTime;
        long elapsedTime;
        double elapsedTimeInSecond;
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        elapsedTimeInSecond = (double) elapsedTime / 1000;

        return elapsedTimeInSecond;
    }

    private static void saveDataToCSV(int implement, String type, int buffer) {
        File csvOutputFile = new File((type + "_" + getImplementName(implement)) + (buffer != -1 ? ("_" + buffer) : ""));

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(BufferUtils::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
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

}
