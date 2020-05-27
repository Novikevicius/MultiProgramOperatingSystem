package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class InterruptResource extends Resource {
    private String interrupt;
    public InterruptResource(Process creator, String interrupt)
    {
        super("InterruptResource", creator);
        this.interrupt = interrupt;
    }
    public InterruptResource()
    {
        super("InterruptResource", null);
    }
    public String getInterruptType()
    {
        return interrupt;
    }
}