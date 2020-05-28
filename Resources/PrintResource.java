package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class PrintResource extends Resource {
    private int page = -1;
    public PrintResource(Process creator)
    {
        super("PrintResource", creator);
    }
    public PrintResource(Process creator, int page)
    {
        super("PrintResource", creator);
        this.page = page;
    }
    public PrintResource()
    {
        super("PrintResource", null);
    }
    public int getPage(){
        return page;
    }
}