package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class InterruptResource extends Resource {

    public InterruptResource()
    {
        super("InterruptResource", null);
    }

    public InterruptResource(String type, int value)
    {
        this();
        this.type = type;
        this.value = value;
    }   

    public String getType() {
        return this.type;
    }

    public int getValue() {
        return this.value;
    }
}