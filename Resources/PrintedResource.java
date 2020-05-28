package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class PrintedResource extends Resource {
    public PrintedResource(Process creator)
    {
        super("PrintedResource", creator);
    }
}