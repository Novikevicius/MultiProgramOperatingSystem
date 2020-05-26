package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.RealMachine.FlashMemory;
import MultiProgramOperatingSystem.Resources.*;

public class ReadFromFlash extends Process {
    
    public ReadFromFlash(Process parent){
        super(parent, "ReadFromFlash", 15);
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new InputStreamResource());
            break;

            case 1:
            kernel.requestResource(this, new SupervisorMemoryResource());
            break;

            case 2:
            FlashMemory.readFromFlashToSupervisorMemory();
            break;

            case 3:
            kernel.freeResource(new TaskInMemoryResource());
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}