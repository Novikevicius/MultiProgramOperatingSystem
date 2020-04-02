package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.RealMachine.*;
import MultiProgramOperatingSystem.VirtualMachine.*;

public class Main {
    public static final boolean DEBUG = true;
  
    public static void main(String[] args) {
        RM rm = new RM();
        VM vm = new VM(rm);
        rm.create_virtual_memory();
        
        
        try {
            vm.loadProgram("MultiProgramOperatingSystem/Program.txt");
            vm.printRegisters();
            vm.runProgram();
            vm.printRegisters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}