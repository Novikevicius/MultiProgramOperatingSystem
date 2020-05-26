package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.Resources.*;

public class StartStop extends Process {
    
    public StartStop(){
        super(null, "StartStop", 10);
    }
    @Override
    public void execute() {
        switch(counter){
            case 0:
            kernel.createResource(new MOSFinal(this));
            kernel.createResource(new InputStreamResource(this));
            kernel.createResource(new SupervisorMemoryResource(this));
            kernel.createResource(new MemoryResource(this));
            kernel.createResource(new TaskInMemoryResource(this));
            kernel.createResource(new TaskParametersResource(this));
            break;
            
            case 1:
            kernel.createProcess(new ReadFromFlash(this));
            kernel.createProcess(new JCL(this));
            kernel.createProcess(new MainProc(this));
            break;

            case 2:
            kernel.requestResource(this, new MOSFinal(this));
            break;
            
            case 3:
            for (Process process : children) {
                kernel.destroyProcess(process);
            }
            for (Resource resource : resources) {
                kernel.deleteResource(resource);
            }
            kernel.shutdownOS();
            break;
        default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
            break;
        }
    }
}