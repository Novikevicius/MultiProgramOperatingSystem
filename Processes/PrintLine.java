package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.RealMachine.Printer;
import MultiProgramOperatingSystem.RealMachine.RM;
import MultiProgramOperatingSystem.Resources.Channel3Resource;
import MultiProgramOperatingSystem.Resources.PrintResource;
import MultiProgramOperatingSystem.Resources.PrintedResource;

public class PrintLine extends Process {
    private PrintResource r;
    private int page;
    private int index;
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
            r = (PrintResource)resources.get(0);
            resources.remove(0);
            page = r.getPage();
            index = 0;
            break;

            case 3:
            char c = (char)(kernel.getRM().getWordAtMemory(page, index));
            index++;
            if (c != 0){
                Printer.print(c);
                counter = 2;
            }
            break;

            case 4:
            resources.remove(resources.size()-1);
            kernel.freeResource(new Channel3Resource(this));
            break;

            case 5:
            kernel.freeResource(new PrintedResource(this));
            counter = -1;
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
        }
    }
}