package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.Resources.FromInterruptResource;
import MultiProgramOperatingSystem.Resources.InterruptResource;

public class Interrupt extends Process {
    private String interrupt;
    private Process target;
    public Interrupt(Process parent){
        super(parent, "Interrupt", 95);
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new InterruptResource());
            break;
            
            case 1:
            InterruptResource resource = (InterruptResource) resources.get(0);
            resources.remove(0);
            interrupt = resource.getInterruptType();
            target = resource.getCreator().getParent();
            break;

            case 2:
            kernel.freeResource(new FromInterruptResource(this, (JobGovernor)target, interrupt));
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}