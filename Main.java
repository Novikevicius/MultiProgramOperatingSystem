package MultiProgramOperatingSystem;

import MultiProgramOperatingSystem.MOS.Kernel;

public class Main {
    public static final boolean DEBUG = false;
  
    public static void main(String[] args) {
        Kernel kernel = Kernel.getInstance();
        kernel.start();
    }
}