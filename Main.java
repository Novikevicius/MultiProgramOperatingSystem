package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.RealMachine.*;
import MultiProgramOperatingSystem.VirtualMachine.*;
import MultiProgramOperatingSystem.RealMachine.HDD;

public class Main {
    public static final boolean DEBUG = false;
    //public static HDD hdd;
    public static void main(String[] args) {
        RM rm = new RM();
        VM vm = new VM();
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