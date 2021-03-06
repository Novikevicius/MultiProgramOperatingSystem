package MultiProgramOperatingSystem.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import MultiProgramOperatingSystem.MOS.Kernel;
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
    public boolean hasAvailableElement(){
        return !resourceElements.isEmpty() && !waitingProcesses.isEmpty();
    }
    public void requestResource(Process process, int count){
        waitingProcesses.put(process, count);
    }
    public Process getCreator(){
        return creator;
    }
    @Override
    public String toString() {
        return name;
    }
    public HashMap<Process, Integer> getWaitingProcesses()
    {
        return waitingProcesses;
    }
    public ArrayList<Resource> getElements(){
        return resourceElements;
    }
}