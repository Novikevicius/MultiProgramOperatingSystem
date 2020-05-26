package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class TaskInMemoryResource extends Resource  {
    public TaskInMemoryResource(Process creator)
    {
        super("TaskInMemoryResource", creator);
    }
    public TaskInMemoryResource()
    {
        super("TaskInMemoryResource", null);
    }
}