package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class TaskParametersResource extends Resource  {
    private int size = 0;
    private int end = 0;
    private Process sender;
    public TaskParametersResource(Process creator)
    {
        super("TaskParametersResource", creator);
    }
    public TaskParametersResource(Process sender, int end)
    {
        this(sender, end, -1);
    }
    public TaskParametersResource(Process sender, int end, int size)
    {
        super("TaskParametersResource", null);
        this.sender = sender;
        this.size = size;
        this.end = end;
    }
    public TaskParametersResource()
    {
        super("TaskParametersResource", null);
    }
    public void setEnd(int end)
    {
        this.end = end;
    }
    public int getEnd()
    {
        return end;
    }
    public Process getSender()
    {
        return sender;
    }
    public int getSize()
    {
        return size;
    }
}