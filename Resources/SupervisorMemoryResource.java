package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class SupervisorMemoryResource extends Resource  {
    private int address = -1;
    public SupervisorMemoryResource(Process creator)
    {
        super("SupervisorMemoryResource", creator);
        resourceElements.add(new SupervisorMemoryResource());
    }
    private SupervisorMemoryResource(int address){
        super("SupervisorMemoryResource", null);
        this.address = address;
    }
    public SupervisorMemoryResource()
    {
        this(-2);
    }
    public int getAddress(){
        return address;
    }
}