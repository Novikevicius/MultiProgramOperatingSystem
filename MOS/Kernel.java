package MultiProgramOperatingSystem.MOS;

import java.util.ArrayList;
import java.util.PriorityQueue;

import MultiProgramOperatingSystem.Processes.Process;
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
    
    public void createResource(){

    }
    public void deleteResource(){

    }
    public void freeResource(){

    }
    public void requestResource(){

    }
    public static Kernel getInstance() {
        if(Kernel.instance == null)
        {
            instance = new Kernel();
        }
        return instance;
    }
}