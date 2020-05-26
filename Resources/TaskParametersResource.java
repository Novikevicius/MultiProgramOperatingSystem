package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class TaskParametersResource extends Resource  {
    private int end = 0;
    private Process sender;
    public TaskParametersResource(Process creator)
    {
        super("TaskParametersResource", creator);
    }
    public TaskParametersResource(Process sender, int end)
    {
        super("TaskParametersResource", null);
        this.sender = sender;
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
}