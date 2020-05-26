package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.Resources.*;

public class MainProc extends Process {
    private TaskParametersResource task;
    private int jobGovernorsCount = 0;
    public MainProc(Process parent){
        super(parent, "MainProc", 50);
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new TaskParametersResource());
            break;

            case 1:
            task = (TaskParametersResource) resources.get(0);
            resources.remove(0);
            if(task.getEnd() == 0)
            {
                counter = 2;
                return;
            }
            break;

            case 2:
            kernel.createProcess(new JobGovernor(this));
            jobGovernorsCount += 1;
            counter = -1;
            break;

            case 3:
            Process sender = task.getSender();
            kernel.destroyProcess(sender);
            jobGovernorsCount -= 1;
            if (jobGovernorsCount == 0)
            {
                kernel.freeResource(new MOSFinal(this));
            }
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}