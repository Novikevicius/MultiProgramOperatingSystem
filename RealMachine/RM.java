package MultiProgramOperatingSystem.RealMachine;

import java.io.FileNotFoundException;

public class RM {
    private byte MODE;
    private int PTR;
    private int IC;
    private byte PI;
    private byte SI;
    private int R1;
    private int R2;
    private int R3;
    private byte CMP;
    private byte IO;
    private int TI;
    private static byte CH1; // - HDD
    private static byte CH2; // - Flash Memory
    private static byte CH3; // - Printer

    private static final int PAGE_SIZE = 16;
    private static final int PAGE_COUNT_PER_VM = 16; // per one Virtual Machine
    private static final int MAX_VM_COUNT = 64;
    private static final int WORD_SIZE = 4;

    private byte[] MEMORY = new byte[PAGE_COUNT_PER_VM * PAGE_SIZE * MAX_VM_COUNT * WORD_SIZE];

    // Physiscal įrenginiai
    public static HDD hdd;
    public static FlashMemory flashMemory;

    static{
        try {
            hdd = new HDD();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setZF(){
        CMP |= (1 << 6);
    }
    public void clearZF(){
        CMP &= ~(1 << 6);
    }
    public byte getZF(){
        return (byte)((CMP >> 6) & 1);
    }

    public void setSF(){
        CMP |= (1 << 5);
    }
    public void clearSF(){
        CMP &= ~(1 << 5);
    }
    public byte getSF(){
        return (byte)((CMP >> 5) & 1);
    }
    public void setOF(){
        CMP |= (1 << 4);
    }
    public void clearOF(){
        CMP &= ~(1 << 4);
    }
    public byte getOF(){
        return (byte)((CMP >> 4) & 1);
    }

    public int getR1() {
        return R1;
    }
    public void setR1(int r1) {
        R1 = r1;
    }

    public int getR2() {
        return R2;
    }
    public void setR2(int r2) {
        R2 = r2;
    }

    public int getR3() {
        return R3;
    }
    public void setR3(int r3) {
        R3 = r3;
    }

    public int getPTR() {
        return PTR;
    }
    public void setPTR(int PTR) {
        this.PTR = PTR;
    }

    public int getIC() {
        return IC;
    }
    public void setIC(int IC) {
        this.IC = IC;
    }

    public static byte getCH1() {
        return CH1;
    }
    public static void setCH1(byte state) {
        channelHelper(state, "CH1:0. Supervisor-HDD channel freed",
                "CH1:1. Supervisor-HDD channel busy - transferring data");
        if (state == 0 || state == 1)
            CH1 = state;
    }

    public static byte getCH2() {
        return CH2;
    }
    public static void setCH2(byte state) {
        channelHelper(state, "CH2:0. FlashMemory channel freed",
                "CH2:1. FlashMemory channel busy - transferring data");
        if (state == 0 || state == 1)
            CH2 = state;
    }

    public static byte getCH3() {
        return CH3;
    }
    public static void setCH3(byte state) {
        channelHelper(state, "CH3:0. Printer channel freed",
                "CH3:1. Printer channel busy - transferring data");
        if (state == 0 || state == 1)
            CH3 = state;
    }

    public byte getPI() {
        return PI;
    }
    public void setPI(byte PI) {
        this.PI = PI;
    }

    public byte getSI() {
        return SI;
    }
    public void setSI(byte SI) {
        this.SI = SI;
    }

    public int getTI() {
        return TI;
    }
    public void setTI(int TI) {
        this.TI = TI;
    }

    public byte getIO() {
        return IO;
    }
    public void setIOI(byte IO) {
        this.IO = IO;
    }

    public byte getMODE() {
        return MODE;
    }
    public void setMODE(byte MODE) {
        this.MODE = MODE;
    }

    private static void channelHelper(byte state, String freedInterrupt, String busyInterrupt) {
        switch (state) {
            case 0:
                System.out.println(freedInterrupt);
                break;
            case 1:
                System.out.println(busyInterrupt);
                break;
            default:
                break;
        }
    }
}