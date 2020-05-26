package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.Resources.*;

public class JobGovernor extends Process {
    
    public JobGovernor(Process parent){
        super(parent, "JobGovernor", 45);
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.freeResource(new TaskParametersResource(this, 0));
            break;

            case 1:
            break;

            case 2:
            counter = -1;
            break;

            case 3:
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}