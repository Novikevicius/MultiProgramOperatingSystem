package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.Resources.*;
import MultiProgramOperatingSystem.Processes.*;
import MultiProgramOperatingSystem.RealMachine;

public class Interrupt extends Process {

    public Interrupt(Process parrent) {
        super(parent, "Interrupt", 99);
    }
    
    @Override
    public void execute() {
        switch(counter)
        {
            case 0:
            kernel.requestResource(this, new InterruptResource());
            break;

            case 1:
            Resource res = this.resources.get(0);
            String intType = "";
            int value = -1;
            if(RM.getPI() != 0){
                intType = "PI";
                value = RM.getPI();
            }
            else if(RM.getSI() != 0){
                intType = "SI";
                value = RM.getSI();
            }
            else if (RM.getTI() <= 0){
                intType = "TI";
                value = RM.getTI();
            }
            else if (RM.getIO() != 0){
                intType = "IO";
                value = RM.getIO();
            }
            else{
                Logger.log("Could not determine interrupt type");
            }
            kernel.freeResource(this, new InterruptResource(intType, value));
            kernel.freeResource(this, res);
            this.resetIC();
            break;

            default:
            System.out.println("Unrecognized step for " + this + ": " + counter);
    }
}