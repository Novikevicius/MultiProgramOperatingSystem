package MultiProgramOperatingSystem.RealMachine;

public class Printer {

    public static void print(Object data) {
        System.out.print("Printer: ");
        if (data instanceof char[]) {
            System.out.println((char[]) data);
        } else {
            System.out.println(data);
        } 
    }
}