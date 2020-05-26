package MultiProgramOperatingSystem.RealMachine;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import MultiProgramOperatingSystem.MOS.Kernel;

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
    public static int readFromFlashToSupervisorMemory() {
        String sourceFile = "MultiProgramOperatingSystem/flash.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(sourceFile));
            int c;
            RM rm = Kernel.getInstance().getRM();
            int block = RM.SUPERVISOR_MEMORY_START;
            int offset = 0;
            while ((c = br.read()) != -1) {
                if(block >= RM.SUPERVISOR_MEMORY_END)
                {
                    System.out.println("Not Enough Supervisor Memory");
                    return -1;
                }
                rm.setWordAtMemory(block, offset++, (int)c);
                if(offset >= RM.PAGE_SIZE)
                {
                    offset = 0;
                    block += 1;
                }
            }
            return block * RM.PAGE_SIZE + offset;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }
}