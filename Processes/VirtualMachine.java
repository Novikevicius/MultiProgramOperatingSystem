package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.VirtualMachine.VM;

public class VirtualMachine extends Process {

    private VM vm;

    public VirtualMachine(Process parent) {
        super(parent, "VirtualMachine", 5);
        vm = new VM(kernel.getRM(), this);
    }

    @Override
    public void execute() {
        switch (counter) {
            case 0:
                kernel.getRM().setMODE((byte) 0);
                break;
            case 1:
                try {
                    vm.runProgram();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}