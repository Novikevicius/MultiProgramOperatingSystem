package MultiProgramOperatingSystem.Processes;

import java.util.ArrayList;

import MultiProgramOperatingSystem.MOS.Kernel;
import MultiProgramOperatingSystem.Resources.Resource;

public abstract class Process {

    private static int IDs = 0;

    protected Kernel kernel;
    protected Process parent;
    protected int id;
    protected String name;
    protected ArrayList<Resource> resources = new ArrayList<>();
    protected ArrayList<Process> children = new ArrayList<>();
    protected int priority;
    protected int counter = 0;
    protected State state;

    public Process(Process parent, String name, int priority){
        this.kernel = Kernel.getInstance();
        this.parent = parent;
        this.id = IDs++;
        this.name = name;
        this.priority = priority;
    }
    public void run()
    {
        execute();
        counter += 1;
    }
    protected abstract void execute();
    public void changeState(State newState)
    {
        state = newState;
    }
    public void destroy()
    {
        if(parent != null){
            parent.removeChild(this);
        }
        for (Resource resource : resources) {
            //kernel.freeResource(resource);
        }
        for (Process process : children) {
            kernel.destroyProcess(process);
        }
    }
    public void removeChild(Process p)
    {
        children.remove(p);
    }
    @Override
    public String toString() {
        return name;
    }
}