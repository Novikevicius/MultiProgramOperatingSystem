package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.VirtualMachine.VM;

public class Main {
    public static void main(String[] args) {
        VM vm = new VM();
        try {
            vm.runProgram("MultiProgramOperatingSystem/Program.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Man reik daryt Lygiagrecius!");
    }
}