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

}
