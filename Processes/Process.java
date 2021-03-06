package MultiProgramOperatingSystem.Processes;

import java.util.ArrayList;

import MultiProgramOperatingSystem.MOS.Kernel;
import MultiProgramOperatingSystem.Resources.Resource;

public abstract class Process implements Comparable{

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
        if(parent != null)
        {
            parent.addChild(this);
        }
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
    public int getID(){
        return id;
    }
    public State getState(){
        return state;
    }
    public void destroy()
    {
        if(parent != null) parent.removeChild(this);
        kernel.removeReady(this);
        while(resources.size() > 0)
        {
            kernel.freeResource(resources.get(resources.size()-1));
            resources.remove(resources.size()-1);
        }
        while(children.size() > 0)
        {
            kernel.destroyProcess(children.get(children.size()-1));
            children.remove(children.size()-1);
        }
    }
    public void takeResource(Resource r)
    {
        resources.add(r);
    }
    public void addChild(Process p)
    {
        children.add(p);
    }
    public void removeChild(Process p)
    {
        children.remove(p);
    }
    public int getStep()
    {
        return counter + 1;
    }
    public Process getParent()
    {
        return parent;
    }
    @Override
    public int compareTo(Object o) {
        return ((Integer)(((Process) o).priority)).compareTo(((Integer)this.priority));
    }
    @Override
    public String toString() {
        return name;
    }
}