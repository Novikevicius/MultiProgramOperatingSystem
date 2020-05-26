package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;
import MultiProgramOperatingSystem.RealMachine.RM;

public class SupervisorMemoryResource extends Resource  {
    private int address = -1;
    public SupervisorMemoryResource(Process creator)
    {
        super("SupervisorMemoryResource", creator);
        for(int i = RM.SUPERVISOR_MEMORY_START; i < RM.SUPERVISOR_MEMORY_END; i++)
        {
            resourceElements.add(new SupervisorMemoryResource(i));
        }
    }
    private SupervisorMemoryResource(int address){
        super("SupervisorMemoryResource", null);
        this.address = address;
    }
    public int getAddress(){
        return address;
    }
}