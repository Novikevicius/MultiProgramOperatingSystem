package MultiProgramOperatingSystem.RealMachine;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class FlashMemory{

    public static int sector = 0;

    public static void readFromFlashToHDD(String sourceFile) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(sourceFile));
            StringBuilder line = new StringBuilder();
            int c;

            while ((c = br.read()) != -1) {
                if (line.length() == 16) {
                    HDD.write(line.toString().toCharArray(), sector);
                    sector++;
                    line = new StringBuilder();
                }
                // 
                if (c != 10 && c != 13) {
                    line.append((char) c);
                }
            }
            if (!line.toString().equals("")) {
                HDD.write(line.toString().toCharArray(), sector);
                sector++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Read from FLASH to HDD.");
    }
}