package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.RealMachine.FlashMemory;
import MultiProgramOperatingSystem.Resources.*;

public class ReadFromFlash extends Process {
    private int end = 0;
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
            end = FlashMemory.readFromFlashToSupervisorMemory();
            if(end == 0)
                counter = -1;
            break;

            case 3:
            kernel.freeResource(new TaskInMemoryResource(end));
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}