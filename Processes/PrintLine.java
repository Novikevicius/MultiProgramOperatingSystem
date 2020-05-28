package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.RealMachine.Printer;
import MultiProgramOperatingSystem.RealMachine.RM;
import MultiProgramOperatingSystem.Resources.Channel3Resource;
import MultiProgramOperatingSystem.Resources.PrintResource;
import MultiProgramOperatingSystem.Resources.PrintedResource;

public class PrintLine extends Process {
    
    public PrintLine(Process parent){
        super(parent, "PrintLine", 80);
    }
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new PrintResource(this));
            break;
            
            case 1:
            kernel.requestResource(this, new Channel3Resource(this));
            break;

            case 2:
            PrintResource r = (PrintResource)resources.get(0);
            resources.remove(0);
            int page = r.getPage();
            StringBuilder builder = new StringBuilder();
            for(int i=0; i < RM.PAGE_SIZE; i++)
            {
                int t = kernel.getRM().getWordAtMemory(page, i);
                char c = (char)t;
                if(c == 0) break;
                builder.append(c);
            }
            Printer.print(builder.toString());
            break;

            case 3:
            resources.remove(resources.size()-1);
            kernel.freeResource(new Channel3Resource(this));
            break;

            case 4:
            kernel.freeResource(new PrintedResource(this));
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}