package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.RealMachine.RM;
import MultiProgramOperatingSystem.VirtualMachine.*;
import MultiProgramOperatingSystem.Resources.*;

public class JobGovernor extends Process {
    private int size = 0;
    private static boolean setShr = false;
    private boolean semaphore = false;
    public JobGovernor(Process parent, int size){
        super(parent, "JobGovernor", 45);
        this.size = size;
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new MemoryResource(this), RM.PAGE_COUNT_PER_VM);
            break;

            case 1:
            RM rm = kernel.getRM();
            int index = 0;
            rm.setPTR(((MemoryResource)resources.get(index++)).getAddress());
            for(int i = 0; i < RM.PAGE_COUNT_PER_VM; i++)
            {
                if( i == RM.PAGE_TABLE_PAGE_IN_VM ){
                    rm.setPTE(i, rm.getPTR());
                    continue;
                }
                if ( i == RM.SHARED_MEMORY_IN_VM )
                {
                    if (!setShr)
                    {
                        rm.setSHR(((MemoryResource)resources.get(index++)).getAddress());
                        setShr = true;
                    }
                    rm.setPTE(i, rm.getSHR());
                    continue;
                }
                rm.setPTE(i, ((MemoryResource)resources.get(index++)).getAddress());
            }
            copyProgram();
            break;

            case 2:
            kernel.freeResource(new SupervisorMemoryResource());
            break;

            case 3:
            kernel.createProcess(new VirtualMachine(this));
            break;

            case 4:
            kernel.activateProcess(children.get(0));
            if(semaphore)
                kernel.getRM().setLCK((byte)1);
            break;

            case 5:
            kernel.requestResource(this, new FromInterruptResource());
            break;

            case 6:
            kernel.stopProcess(children.get(0));
            runInterrupt();
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
            counter = -1;
        }
    }
    private void runInterrupt(){
        FromInterruptResource r = (FromInterruptResource) resources.get(resources.size()-1);
        resources.remove(resources.size()-1);
        String interrupt = r.getInterruptType();
        switch (interrupt) {
            case "TIME":
                counter = 3;
                return;
                
            case "HALT":
                kernel.destroyProcess(children.get(0));
                TaskParametersResource task = new TaskParametersResource(this, 0);
                kernel.freeResource(task);
                changeState(State.BLOCKED);
                return;

            case "SEMAPHORE":
                if(hasSemaphore())
                {
                    kernel.freeResource(resources.get(resources.size()-1));
                    semaphore = false;
                }else{
                    kernel.requestResource(this, new SemaphoreResource());
                    semaphore = true;
                }
                counter = 3;
                return;

            case "MEMORY":
                System.out.println("VM is trying to access wrong memory, destroying it...");
                kernel.destroyProcess(children.get(0));
                TaskParametersResource task2 = new TaskParametersResource(this, 0);
                kernel.freeResource(task2);
                changeState(State.BLOCKED);
                return;
            default:
                counter = -1;
                break;
        }
    }
    private boolean hasSemaphore()
    {
        return resources.get(resources.size()-1).getClass().equals(SemaphoreResource.class);
    }
    @Override
    public void takeResource(Resource r)
    {
        resources.add(r);
    }
    public void copyProgram()
    {   
        RM rm = kernel.getRM();
        VM vm = new VM(rm, null);
        
        String state = "START";
        String currentLine = "";
        int offset = 0;
        int page = RM.SUPERVISOR_MEMORY_START;
        int index = 0;
        while (true)
        {
            if(size == page * RM.PAGE_SIZE + index) 
                return;
            currentLine = "";
            char c = 0;
            while(true)
            {
                c = (char)rm.getWordAtMemory(page, index++);
                if(size == page * RM.PAGE_SIZE + index){
                    currentLine += c;
                    break;
                }
                if(index >= RM.PAGE_SIZE)
                {
                    index = 0;
                    page++;
                }
                if(c == 10) continue;
                if(c == 13)
                {
                    break;
                }
                currentLine += c;
            }
            String[] split = currentLine.split(" ");
            currentLine = split[0];
            if(state.equals("START"))
            {
                if(currentLine.equals("DATA"))
                {
                    state = "DATA";
                    offset = VM.DATA_SEGMENT_START;
                    continue;
                }
            }
            else if(state.equals("DATA"))
            {
                if(currentLine.equals("CODE"))
                {
                    state = "CODE";
                    offset = VM.CODE_SEGMENT_START;
                }
                else if(currentLine.equals("DW"))
                {
                    vm.writeWord(offset++, Integer.parseInt(split[1]));
                }
                continue;
            }
            else if(state.equals("CODE"))
            {
                Instruction instr = Instruction.getInstructionByName(currentLine);
                vm.writeWord(offset++, instr.getOpcode());
                for (int i = 0; i < instr.getArgCount(); i++){
                    vm.writeWord(offset++, Integer.parseInt(split[i+1]));
                }
            }
        }
    }
}