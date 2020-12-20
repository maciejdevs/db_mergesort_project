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

                    getEndTime(startTime, file);
                }
            }
        }
    }

    private static void handleImplementationThreeAndFour(List<String> filesPath, int implementationNumber, List<Integer> bufferSizes, boolean sequential) throws IOException {
        long startTime;
        System.out.println("Implementation " + implementationNumber + " ");

        for (int bufferSize : bufferSizes) {
            for (String file : filesPath) {
                startTime = System.currentTimeMillis();
                if (sequential) {
                    SequentialReading sequentialReading = new SequentialReading(file);

                    if (implementationNumber == 3) {
                        sequentialReading.length3(bufferSize);
                    } else if (implementationNumber == 4) {
                        sequentialReading.length4(bufferSize);
                    }

                    System.out.println("Buffer size: " + bufferSize);
                    getEndTime(startTime, file);
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
                        getEndTime(startTime, file);
                    }
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
        System.out.println("---------------------------------------------------------------\n");
    }

}
