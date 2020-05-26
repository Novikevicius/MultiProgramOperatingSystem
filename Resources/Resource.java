package MultiProgramOperatingSystem.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void giveResource()
    {
        for (Map.Entry<Process,Integer> entry : waitingProcesses.entrySet()) {
            if(resourceElements.size() > entry.getValue())
            {
                Process p = entry.getKey();
                for (int i = 0; i < entry.getValue(); i++) {
                    Resource r = resourceElements.get(0);
                    resourceElements.remove(0);
                    p.takeResource(r);
                }
                waitingProcesses.remove(p);
            }
        }
    }
    public boolean hasAvailableElement(){
        return resourceElements.isEmpty();
    }
    public void requestResource(Process process, int count){
        waitingProcesses.put(process, count);
    }
    @Override
    public String toString() {
        return name;
    }
}