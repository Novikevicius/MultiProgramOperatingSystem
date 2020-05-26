package MultiProgramOperatingSystem.MOS;

import java.util.ArrayList;
import java.util.PriorityQueue;

import MultiProgramOperatingSystem.Processes.Process;
import MultiProgramOperatingSystem.Processes.StartStop;
import MultiProgramOperatingSystem.Processes.State;
import MultiProgramOperatingSystem.RealMachine.RM;
import MultiProgramOperatingSystem.Resources.Resource;

public class Kernel {

    private static Kernel instance = null;

    private ArrayList<Resource> resources = new ArrayList<>();
    private ArrayList<Process> processes = new ArrayList<>();
    private PriorityQueue<Process> readyProcesses = new PriorityQueue<>();
    private PriorityQueue<Process> blockedProcesses = new PriorityQueue<>();
    private Process currentProcess;

    private boolean shutdown = false;
    private RM rm;

    public void start(){
        System.out.println("Starting OS");
        rm = new RM();
        getInstance().createProcess(new StartStop());
        resourceDistributor();
        while(!shutdown)
        {
            if(currentProcess != null)
            {
                currentProcess.run();
            }
            else
            {
                break;
            }
        }
        System.out.println("Shutting down OS");

    }
    public void createProcess(Process process){
        System.out.println("Creating " + process + " process");
        processes.add(process);
        readyProcesses.add(process);
        process.changeState(State.READY);
        System.out.println(process + " created");
    }
    public void destroyProcess(Process process){
        System.out.println("Destroying " + process + " process");
        process.destroy();
        processes.remove(process);
        readyProcesses.remove(process);
        blockedProcesses.remove(process);
        System.out.println(process + " destroyed");
    }
    public void activateProcess(Process p){
        if(p.getState() == State.READY_SUSPENDED)
        {
            p.changeState(State.READY);
            readyProcesses.add(p);
        }
        else if(p.getState() == State.BLOCKED_SUSPENDED)
        {
            p.changeState(State.BLOCKED);
            blockedProcesses.add(p);
        }
    }
    public void stopProcess(Process p){
        if(p.getState() == State.RUNNING)
        {
            p.changeState(State.READY_SUSPENDED);
        }
        else if(p.getState() == State.READY)
        {
            p.changeState(State.READY_SUSPENDED);
            readyProcesses.remove(p);
        }
        else if(p.getState() == State.BLOCKED)
        {
            p.changeState(State.BLOCKED_SUSPENDED);
            blockedProcesses.remove(p);
        }
    }
    public void createResource(Resource r){
        System.out.println("Creating " + r + " resource");
        resources.add(r);
        System.out.println(r + " resource created");
    }
    public void deleteResource(Resource r){
        System.out.println("Deleting " + r + " resource");
        resources.remove(r);
        System.out.println(r + " resource deleted");
    }
    public void freeResource(Resource r){
        for (Resource resource : resources) {
            if(resource.getClass().equals(r.getClass()))
            {
                resource.freeResource(r);
            }
        }
        resourceDistributor();
    }
    public void requestResource(Process p, Resource r, int amount){
        for (Resource resource : resources) {
            if(resource.getClass().equals(r.getClass()))
            {
                resource.requestResource(p, amount);
                p.changeState(State.BLOCKED);
                readyProcesses.remove(p);
            }
        }
        resourceDistributor();
    }
    public void requestResource(Process p, Resource r){
        requestResource(p, r, 1);
    }
    private void resourceDistributor()
    {
        for (Resource resource : resources) {
            if(resource.hasAvailableElement())
            {
                resource.giveResource();
            }
        }
        planner();
    }
    private void planner()
    {
        if(currentProcess != null)
        {
            State curState = currentProcess.getState();
            if(curState == State.BLOCKED)
            {
                blockedProcesses.add(currentProcess);
            }
            if(!readyProcesses.isEmpty())
            {
                currentProcess.changeState(State.READY);
                readyProcesses.add(currentProcess);
                Process p = readyProcesses.poll();
                currentProcess = p;
                p.changeState(State.RUNNING);
                return;
            }
        }
        currentProcess = readyProcesses.poll();
    }
    public RM getRM()
    {
        return rm;
    }
    public static Kernel getInstance() {
        if(Kernel.instance == null)
        {
            instance = new Kernel();
        }
        return instance;
    }
}