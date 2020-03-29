package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.VirtualMachine.VM;

public class Main {
    public static final boolean DEBUG = false;
    public static void main(String[] args) {
        VM vm = new VM();
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