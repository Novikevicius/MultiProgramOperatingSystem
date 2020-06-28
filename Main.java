package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.MOS.Kernel;

public class Main {
    public static final boolean DEBUG = false;
    public static final int VERBOSE = 0;
    public static final boolean DEBUG_VM = true;
  
    public static void main(String[] args) {
        Kernel kernel = Kernel.getInstance();
        kernel.start();
    }
}