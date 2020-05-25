package MultiProgramOperatingSystem.MOS;

import java.util.ArrayList;
import java.util.PriorityQueue;

import MultiProgramOperatingSystem.Processes.Process;
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
    public void createProcess(Process parent, Process process){

    }
    public void destroyProcess(){

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