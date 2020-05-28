package MultiProgramOperatingSystem.Resources;

import MultiProgramOperatingSystem.Processes.Process;

public class Channel3Resource extends Resource {
    public Channel3Resource(Process creator)
    {
        super("Channel3Resource", creator);
        resourceElements.add(new Channel3Resource());
    }
    public Channel3Resource()
    {
        super("Channel3Resource", null);
    }
}