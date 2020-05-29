package MultiProgramOperatingSystem.MOS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import MultiProgramOperatingSystem.Main;
import MultiProgramOperatingSystem.Processes.*;
import MultiProgramOperatingSystem.Processes.Process;
import MultiProgramOperatingSystem.RealMachine.RM;
import MultiProgramOperatingSystem.Resources.*;

public class Kernel {

    private static Kernel instance = null;

    private ArrayList<Resource> resources = new ArrayList<>();
    private ArrayList<Process> processes = new ArrayList<>();
    private PriorityQueue<Process> readyProcesses = new PriorityQueue<>();
    private PriorityQueue<Process> blockedProcesses = new PriorityQueue<>();
    private Process currentProcess;

    private boolean shutdown = false;
    private RM rm;

    public void start() {
        System.out.println("Starting OS");
        rm = new RM();
        getInstance().createProcess(new StartStop());
        resourceDistributor();
        while (!shutdown) {
            if (currentProcess != null) {
                if(Main.DEBUG)
                {
                    System.out.println("\tPress ENTER key to execute step " + currentProcess.getStep() + " of " + currentProcess);
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
                        String input = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("\tRunning " + currentProcess + ": step " + currentProcess.getStep());
                }
                currentProcess.run();
            }
            else
            {
                break;
            }
        }
        destroyProcess(processes.get(0));
        System.out.println("Shutting down OS");

    }
    public void createProcess(Process process){
        processes.add(process);
        readyProcesses.add(process);
        process.changeState(State.READY);
        System.out.println(process + " created");
    }
    public void destroyProcess(Process process){
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
        resources.add(r);
        System.out.println(r + " resource created");
    }
    public void deleteResource(Resource r){
        resources.remove(r);
        System.out.println(r + " resource deleted");
    }
    public void deleteResources(){
        while(resources.size() > 0) {
            deleteResource(resources.get(resources.size()-1));
        }
    }
    public void freeResource(Resource r){
        for (Resource resource : resources) {
            if(resource.getClass().equals(r.getClass()))
            {
                resource.freeResource(r);
            }
        }
        System.out.println("Freeing " + r);
        resourceDistributor();
    }
    public void requestResource(Process p, Resource r, int amount){
        for (Resource resource : resources) {
            if(resource.getClass().equals(r.getClass()))
            {
                resource.requestResource(p, amount);
                p.changeState(State.BLOCKED);
                readyProcesses.remove(p);
                break;
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
                HashMap<Process, Integer> waitingProcesses = resource.getWaitingProcesses();
                ArrayList<Resource> resourceElements = resource.getElements();
                while(waitingProcesses.size() > 0){
                    Set<Map.Entry<Process,Integer>> entrys = waitingProcesses.entrySet();
                    Map.Entry<Process,Integer> entry = entrys.iterator().next();
                    if(resourceElements.size() >= entry.getValue())
                    {
                        Process p = entry.getKey();
                        for (int i = 0; i < entry.getValue(); i++) {
                            Resource r = resourceElements.get(0);
                            resourceElements.remove(0);
                            p.takeResource(r);
                        }
                        if(currentProcess == p)
                        {
                            currentProcess = null;
                        }
                        waitingProcesses.remove(p);
                        blockedProcesses.remove(p);
                        p.changeState(State.READY);
                        readyProcesses.add(p);
                    }else break;
                }
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
                Process p = readyProcesses.poll();
                if(curState == State.READY || curState == State.RUNNING){
                    currentProcess.changeState(State.READY);
                    readyProcesses.add(currentProcess);
                }
                currentProcess = p;
                p.changeState(State.RUNNING);
                printProcesses();
                return;
            }
            else if(curState == State.READY || curState == State.RUNNING)
            {
                printProcesses();
                return;
            }
        }
        currentProcess = readyProcesses.poll();
        printProcesses();
    }
    private void printProcesses()
    {
        if(!Main.DEBUG) return;
        System.out.println("------------------------------------------");
        System.out.println("Current process "  + currentProcess + " RUNNING");
        System.out.println("Ready Processes ");
        if(readyProcesses.isEmpty())
        {
            System.out.println(("\tNo processes"));
        }
        StringBuilder bd = new StringBuilder();
        Iterator<Process> it = readyProcesses.iterator();
        while(it.hasNext()){
            Process p = it.next();
            System.out.println("\t" + p + " READY");
        }
        System.out.println("Blocked Processes ");
        if(blockedProcesses.isEmpty())
        {
            System.out.println(("\tNo processes"));
        }
        it = blockedProcesses.iterator();
        while(it.hasNext()){
            Process p = it.next();
            System.out.println("\t" + p + " BLOCKED");
        }
        System.out.println("------------------------------------------");
    }
    public void removeReady(Process p){
        readyProcesses.remove(p);
    }
    public void shutdownOS()
    {
        shutdown = true;
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