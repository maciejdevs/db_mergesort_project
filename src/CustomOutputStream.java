import java.io.IOException;

public interface CustomOutputStream {

    void writeln(String line) throws IOException;

    void close() throws IOException;

}
