package MultiProgramOperatingSystem.RealMachine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HDD {

    private static final int SECTORS = 1000; // actualy random nustaciau
    private static final int WORDS_PER_SECTOR = 16;
    private static final String EMPTY_SECTOR = "                ";
    private static RandomAccessFile file;

    public HDD() throws FileNotFoundException{
        file = new RandomAccessFile("hdd_test", "rw");
        try{
            for(int i = 0; i < SECTORS; i++){
                file.seek(WORDS_PER_SECTOR * 2 * i);
                file.writeChars(EMPTY_SECTOR);
            }
            System.out.println(file);

        } catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("HDD initialized");
    }
}
