package MultiProgramOperatingSystem.MOS;

import java.util.ArrayList;
import java.util.PriorityQueue;

import MultiProgramOperatingSystem.Processes.Process;
import MultiProgramOperatingSystem.Processes.StartStop;
import MultiProgramOperatingSystem.Processes.State;
import MultiProgramOperatingSystem.Resources.Resource;

public class Kernel {

    private static Kernel instance = null;

    private ArrayList<Resource> resources = new ArrayList<>();
    private ArrayList<Process> processes = new ArrayList<>();
    private PriorityQueue<Process> readyProcesses = new PriorityQueue<>();
    private PriorityQueue<Process> blockedProcesses = new PriorityQueue<>();
    private Process currentProcess;


    public void start(){
        getInstance().createProcess(new StartStop());
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
    public void activateProcess(){

    }
    public void stopProcess(){

    }
    public void changePriority(){

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
    public void freeResource(){

    }
    public void requestResource(){

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
                Process p = readyProcesses.poll();
                currentProcess = p;
                return;
            }
        }
        currentProcess = readyProcesses.poll();
    }
    public static Kernel getInstance() {
        if(Kernel.instance == null)
        {
            instance = new Kernel();
        }
        return instance;
    }
}