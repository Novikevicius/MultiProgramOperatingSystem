package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.RealMachine.*;
import MultiProgramOperatingSystem.VirtualMachine.*;

public class Main {
    public static final boolean DEBUG = false;
  
    public static void main(String[] args) {
        RM rm = new RM();
        //VM vm = new VM(rm);
        rm.setPTR(1023, 0);
        for(int i = 0; i < RM.ENTRIES_PER_PAGE_TABLE; i++){
            rm.setPTE(i, RM.ENTRIES_PER_PAGE_TABLE-i-1);
        }
        rm.printPageTable();
        rm.setWord(15, 5, 10);
        System.out.println(rm.getPageTableAddress());
        System.out.println(rm.virtualToRealAddress(15, 5));
        System.out.println(rm.getWord(15, 5));
        
        /*
        try {
            vm.loadProgram("MultiProgramOperatingSystem/Program.txt");
            vm.printRegisters();
            vm.runProgram();
            vm.printRegisters();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.out.println("Man reik daryt Lygiagrecius!");
    }
}