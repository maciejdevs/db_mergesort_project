import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class InputStream {

    private File file;
    private boolean isOpen;
    private FileReader fileReader;

    public InputStream(File file){
        this.file = file;
        this.isOpen = false;
        this.fileReader = null;
    }

    void open(){
        try {
            if(!isOpen) {
                isOpen = true;
                fileReader = new FileReader(file);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void readln(){

    }

    void seek(int pos){

    }

    boolean endofstream(){
        return true;
    }
}
