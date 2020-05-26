package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.RealMachine.FlashMemory;
import MultiProgramOperatingSystem.Resources.*;

public class JCL extends Process {
    
    public JCL(Process parent){
        super(parent, "JCL", 20);
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new TaskInMemoryResource());
            break;

            case 1:
            break;

            case 2:
            //kernel.freeResource(new TaskParametersResource());
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}