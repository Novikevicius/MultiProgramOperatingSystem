package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.JobGovernor;
import MultiProgramOperatingSystem.Processes.Process;

public class FromInterruptResource extends Resource {
    private String interrupt;
    private JobGovernor target;
    public FromInterruptResource(Process creator, JobGovernor target, String interrupt)
    {
        super("FromInterruptResource", creator);
        this.interrupt = interrupt;
        this.target = target;
    }
    public FromInterruptResource(Process creator)
    {
        super("FromInterruptResource", creator);
    }
    public FromInterruptResource()
    {
        super("FromInterruptResource", null);
    }
    public JobGovernor getTarget()
    {
        return target;
    }
    public String getInterruptType()
    {
        return interrupt;
    }
}