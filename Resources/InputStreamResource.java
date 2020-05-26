package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class InputStreamResource extends Resource {
    public InputStreamResource(Process creator)
    {
        super("InputStreamResource", creator);
        resourceElements.add(new InputStreamResource());
    }
    public InputStreamResource(){
        super("InputStreamResource", null);
    }

}