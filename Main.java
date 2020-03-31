package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.RealMachine.*;
import MultiProgramOperatingSystem.VirtualMachine.*;

public class Main {
    public static final boolean DEBUG = true;
  
    public static void main(String[] args) {
        RM rm = new RM();
        VM vm = new VM(rm);
        // set page table pointer to point at 1023 page and 0 offset
        rm.setPTR(1023, 0);
        // initialize Page Table Entries to point at different RAM pages (PTE 0 points at page 0 at RAM, PTE 1 at page 1, ..., PTE 15 - at 15)
        for(int i = 0; i < RM.ENTRIES_PER_PAGE_TABLE; i++){
            rm.setPTE(i, i);
        }
        rm.printPageTable(); // some debug info about Page Table
        rm.setWord(15, 5, 10); // test if the correct word in RAM is set
        System.out.println(rm.getPageTableAddress());
        System.out.println(rm.virtualToRealAddress(15, 5));
        System.out.println(rm.getWord(15, 5));
        
        
        try {
            vm.loadProgram("MultiProgramOperatingSystem/Program.txt");
            vm.printRegisters();
            vm.runProgram();
            vm.printRegisters();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Man reik daryt Lygiagrecius!");
    }
}