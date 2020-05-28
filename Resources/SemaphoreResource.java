package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class SemaphoreResource extends Resource {
    public SemaphoreResource(Process creator)
    {
        super("SemaphoreResource", creator);
        resourceElements.add(new SemaphoreResource());
    }
    public SemaphoreResource()
    {
        super("SemaphoreResource", null);
    }
}