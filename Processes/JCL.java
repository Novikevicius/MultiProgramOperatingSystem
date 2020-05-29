package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.RealMachine.RM;
import MultiProgramOperatingSystem.Resources.*;
import MultiProgramOperatingSystem.VirtualMachine.Instruction;

public class JCL extends Process {
    private TaskInMemoryResource task;
    public JCL(Process parent){
        super(parent, "JCL", 20);
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new TaskInMemoryResource());
            break;

            case 1:
            if (!checkSyntax())
                counter = -1;
            resources.remove(resources.size()-1);
            break;

            case 2:
            kernel.freeResource(new TaskParametersResource(this, 1, task.getEnd()));
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
    public boolean checkSyntax()
    {
        task = (TaskInMemoryResource) resources.get(0);
        try
        {
            String state = "START";
            String currentLine = "";
            RM rm = kernel.getRM();

            int page = RM.SUPERVISOR_MEMORY_START;
            int index = 0;
            while (true)
            {
                currentLine = "";
                char c = 0;
                while(true)
                {
                    if(task.getEnd() == page * RM.PAGE_SIZE + index) 
                        return true;
                    c = (char)rm.getWordAtMemory(page, index++);
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
                        continue;
                    }
                    else
                    {
                        return false;
                    }
                }
                else if(state.equals("DATA"))
                {
                    if(currentLine.equals("CODE"))
                    {
                        state = "CODE";
                    }
                    else if(currentLine.equals("DW"))
                    {
                        Integer.parseInt(split[1]);
                    }
                    continue;
                }
                else if(state.equals("CODE"))
                {
                    Instruction instr = Instruction.getInstructionByName(currentLine);
                    for (int i = 0; i < instr.getArgCount(); i++){
                            Integer.parseInt(split[i+1]);
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("JCL: error program's syntax is not correct");
            return false;
        }
    }
}