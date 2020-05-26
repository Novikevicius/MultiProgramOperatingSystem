package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class SupervisorMemoryResource extends Resource  {
    private int address = -1;
    public SupervisorMemoryResource(Process creator)
    {
        super("SupervisorMemoryResource", creator);
        for(int i = 0; i < 100; i++)
        {
            resourceElements.add(new SupervisorMemoryResource(i));
        }
    }
    private SupervisorMemoryResource(int address){
        super("SupervisorMemoryResource", null);
        this.address = address;
    }
}