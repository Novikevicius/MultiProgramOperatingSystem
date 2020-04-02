package MultiProgramOperatingSystem.RealMachine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class HDD {

    private static final int SECTORS = 1000;
    private static final int WORDS_PER_SECTOR = 16;
    private static final String EMPTY_SECTOR = "                ";
    public static ArrayList<Integer> usedSectors = new ArrayList<>();
    private static RandomAccessFile file;

    public HDD() throws FileNotFoundException{
        file = new RandomAccessFile("MultiProgramOperatingSystem/hdd_test.txt", "rw");
        try{
            for(int i = 0; i < SECTORS; i++){
                file.seek(WORDS_PER_SECTOR * 2 * i);
                file.writeChars(EMPTY_SECTOR);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("HDD initialized");
    }
    public static void write(char[] data, int sector){
        if(sector < 0 || sector > SECTORS){
            throw new IllegalArgumentException("Sector is illegal");
        }
        try{ 
            file.seek(WORDS_PER_SECTOR * sector * 2);
            file.writeChars(new String(data));
            usedSectors.add(sector);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static char[] read(int sector){
        if(sector < 0 || sector > SECTORS){
            throw new IllegalArgumentException("Sector argument is illegal");
        }
        char[] data = new char[WORDS_PER_SECTOR];
        try{
            file.seek(WORDS_PER_SECTOR * sector * 2);
            for (int i = 0; i < WORDS_PER_SECTOR; ++i) {
                data[i] = file.readChar();
            }
            for(char el: data){
                System.out.print(el + ", ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static boolean isEmpty(int sector){
        if(sector < 0 || sector > SECTORS){
            throw new IllegalArgumentException("Sector argument is illegal");
        }
        return new String(read(sector).equals(EMPTY_SECTOR));
    }
    public static void clear(int sector){
        if(sector < 0 || sector > SECTORS){
            throw new IllegalArgumentException("Sector argument is illegal");
        }
        try{
            file.seek(WORDS_PER_SECTOR * sector * 2);
            file.writeChars(EMPTY_SECTOR);
            usedSectors.remove(sector);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
