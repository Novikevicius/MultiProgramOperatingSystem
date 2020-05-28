package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.VirtualMachine.VM;

public class VirtualMachine extends Process {

    private VM vm;
    private int ptr;
    private int old_ptr;
    public VirtualMachine(Process parent) {
        super(parent, "VirtualMachine", 5);
        vm = new VM(kernel.getRM(), this);
    }

    @Override
    public void execute() {
        switch (counter) {
            case 0:
                kernel.getRM().setMODE((byte) 0);
                old_ptr = kernel.getRM().getPTR();
                vm.loadValues();
                kernel.getRM().setPTR(ptr);
                break;
            case 1:
                try {
                    vm.runProgram();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                vm.saveValues();
                kernel.getRM().setPTR(old_ptr);
                counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
    public void setPTR( int ptr ){
        this.ptr = ptr;
    }
    @Override
    public String toString() {
        return super.toString() + " " + id;
    }
}