package MultiProgramOperatingSystem.Processes;

import MultiProgramOperatingSystem.MOS.Kernel;

public abstract class Process {
    protected Kernel kernel;
    public Process(Kernel k){
        kernel = k;
    }
    public abstract void run();
}