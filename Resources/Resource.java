package MultiProgramOperatingSystem.Resources;

import java.util.ArrayList;
import java.util.HashMap;

import MultiProgramOperatingSystem.Processes.Process;

public abstract class Resource {
    private static int IDs = 0;
    protected int id;
    protected String name;
    protected Process creator;
    protected ArrayList<Resource> resourceElements = new ArrayList<>();
    protected HashMap<Process, Integer> waitingProcesses = new HashMap<>(); // Process & how many resources it needs

    protected Resource(String name, Process creator)
    {
        this.id = IDs++;
        this.name = name;
        this.creator = creator;
    }
    public void freeResource(Resource element){
        this.resourceElements.add(element);
    }
    public void requestResource(Process process, int count){
        waitingProcesses.put(process, count);
    }
    @Override
    public String toString() {
        return name;
    }
}