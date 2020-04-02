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

    public static final int PAGE_SIZE = 16;
    public static final int PAGE_COUNT_PER_VM = 16; // per one Virtual Machine
    public static final int MAX_VM_COUNT = 64;
    public static final int WORD_SIZE = 4;

    public static final int ENTRIES_PER_PAGE_TABLE = PAGE_COUNT_PER_VM;
    private int[] MEMORY = new int[PAGE_COUNT_PER_VM * PAGE_SIZE * MAX_VM_COUNT];

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

    /**
     * changes value in virtual memory which is mapped to memory in RAM
     * @param page - page number in VM
     * @param offset - offset in the page
     * @param value - new value at specified memory location
     */
    public void setWord(int page, int offset, int value)
    {
        MEMORY[virtualToRealAddress(page, offset)] = value;
    }
    public int getWord(int page, int offset)
    {
        return MEMORY[virtualToRealAddress(page, offset)];
    }
    public int virtualToRealAddress(int page, int offset)
    {
        int tableAddress = getPageTableAddress();
        return MEMORY[tableAddress + page] * PAGE_SIZE + offset;
    }
    public int getPageTableAddress()
    {
        // PTR:
        // 00000000 (in hex)
        // 0xxxxxyy
        // xxxxx - page number in RAM where the Page Table is stored
        // yy    - offset in page
        int offset = PTR & 0xFF;
        int page   = (PTR >> 8) & 0x03FF;  
        return PAGE_SIZE * page + offset;
    }
    public void printPageTable()
    {
        int tableAddress = getPageTableAddress();
        System.out.println( "---------Page table -----------------");
        System.out.println( "PTR: " + getPTR());
        for (int i = 0; i < ENTRIES_PER_PAGE_TABLE; i++) {
            System.out.println( (tableAddress+i) + ": " + MEMORY[tableAddress+i]);
        }
        System.out.println( "-------------------------------------");
    }
    // set Page Table Entry
    /***
     * maps page in Virtual memory to Page in RAM
     * @param pageInVM - page number in virtual memory, [0, 255]
     * @param pageInRAM - page number in RAM, [0, 1023]
     */
    public void setPTE(int pageInVM, int pageInRAM)
    {
        if(pageInVM < 0 || pageInVM >= ENTRIES_PER_PAGE_TABLE || pageInRAM < 0 || pageInRAM > MAX_VM_COUNT * PAGE_COUNT_PER_VM)
            return;
        int table = getPageTableAddress();
        MEMORY[table + pageInVM] = pageInRAM;
    }
    /***
     * change Page Table Pointer to point to actual page table at specified location in RAM
     * @param page   - page where the Page Table is stored in RAM
     * @param offset - offset in the page for the Page Table pointer
     */
    public void setPTR(int page, int offset)
    {
        if( page < 0 || page > 0x03FF || offset < 0 || offset >= PAGE_SIZE)
            return;
        PTR = (page << 8) | offset;
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
    /* ---------Virtual Memory model-------*/
    /*
        0 1  2  3  4  5  6 7 8 9 A B C D E F|
       -----------Data segment---------------
       0            0-16                    |
       1            16-32                   |
       2            32-48                   |
       3            48-64                   |
       ------------Code segment--------------
       4            64-80                   |
       5            80-96                   |
       6            96-112                  |
       7            112-128                 |
       8            128-144                 |
       9            144-160                 |
       A            160-176                 |
       B            176-192                 |
       -----------Page table ----------------
       C            192-208                 |
       -----------Shared memory--------------
       D            208-224                 |
       E            224-240                 |
       F            240-256                 |
       --------------------------------------
     */
}