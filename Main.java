package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.MOS.Kernel;
import MultiProgramOperatingSystem.RealMachine.*;
import MultiProgramOperatingSystem.VirtualMachine.*;

public class Main {
    public static final boolean DEBUG = true;
  
    public static void main(String[] args) {
        Kernel kernel = Kernel.getInstance();
        kernel.start();
        /*
        RM rm = new RM();
        VM vm = new VM(rm);
        rm.create_virtual_memory();
        try {
            vm.loadProgram("MultiProgramOperatingSystem/Program.txt");
            vm.runProgram();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}