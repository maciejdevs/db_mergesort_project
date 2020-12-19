import java.io.IOException;
import java.util.List;

public abstract class BufferUtils {

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
        long startTime;

        switch (implementationNumber) {
            case 1:
            case 2:
                handleImplementationOneAndTwo(filesPath, implementationNumber, sequential);
                break;
            case 3:
                for (int bufferSize : bufferSizes) {
                    startTime = System.currentTimeMillis();

                    if (sequential) {
                        SequentialReading sequentialReading = new SequentialReading("src/imdb/movie_link.csv");
                        sequentialReading.length1();
                    }

                    getEndTime(startTime, "");
                }
                break;
        }
    }

    private static void handleImplementationOneAndTwo(List<String> filesPath, int implementationNumber, boolean sequential) throws IOException {
        long startTime;
        System.out.println("Implementation " + implementationNumber + " ");

        for (String file : filesPath) {
            if (sequential) {
                startTime = System.currentTimeMillis();
                SequentialReading sequentialReading = new SequentialReading(file);
                if (implementationNumber == 1) {
                    sequentialReading.length1();
                } else if (implementationNumber == 2) {
                    sequentialReading.length2();
                }

                getEndTime(startTime, file);
            } else {
                for (int i = 1; i <= 3; i++) {
                    startTime = System.currentTimeMillis();
                    RandomReading randomReading = new RandomReading(file, (int) Math.pow(100, i));
                    if (implementationNumber == 1) {
                        randomReading.randJump1();
                    } else if (implementationNumber == 2) {
                        randomReading.randJump2();
                    }

                    getEndTime(startTime, file);
                }
            }
        }
    }

    private static void getEndTime(long startTime, String file) {
        long stopTime;
        long elapsedTime;
        double elapsedTimeInSecond;
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        elapsedTimeInSecond = (double) elapsedTime / 1000;
        System.out.println("Filename: '" + file + "', elapsed time: " + elapsedTimeInSecond);
    }

}
