package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class TaskInMemoryResource extends Resource  {
    private int end = 0;
    public TaskInMemoryResource(Process creator)
    {
        super("TaskInMemoryResource", creator);
    }
    public TaskInMemoryResource(int end)
    {
        super("TaskInMemoryResource", null);
        this.end = end;
    }
    public TaskInMemoryResource()
    {
        super("TaskInMemoryResource", null);
    }
    public void setEnd(int end)
    {
        this.end = end;
    }
    public int getEnd()
    {
        return end;
    }
}