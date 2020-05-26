package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;
import MultiProgramOperatingSystem.RealMachine.RM;

public class MemoryResource extends Resource  {
    private int address = -1;
    public MemoryResource(Process creator)
    {
        super("MemoryResource", creator);
        for(int i = RM.MEMORY_START; i < RM.MEMORY_END; i++)
        {
            resourceElements.add(new MemoryResource(i));
        }
    }
    private MemoryResource(int address){
        super("MemoryResource", null);
        this.address = address;
    }
    public int getAddress(){
        return address;
    }
}