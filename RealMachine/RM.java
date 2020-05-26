package MultiProgramOperatingSystem.RealMachine;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;

import MultiProgramOperatingSystem.VirtualMachine.VM;

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
    private byte LCK;
    private int SHR = 0;
    private boolean setSHR = false;
    private static byte CH1; // - HDD
    private static byte CH2; // - Flash Memory
    private static byte CH3; // - Printer

    public static final int PAGE_SIZE = 16;
    public static final int PAGE_COUNT_PER_VM = 16; // per one Virtual Machine
    public static final int MAX_VM_COUNT = 4;
    public static final int WORD_SIZE = 4;
    public static final int PAGE_TABLE_PAGE_IN_VM = 12;
    public static final int SHARED_MEMORY_IN_VM = 13;
    
    public static final int ENTRIES_PER_PAGE_TABLE = PAGE_COUNT_PER_VM;
    public static final int PAGE_COUNT = PAGE_COUNT_PER_VM * MAX_VM_COUNT;
    private static final int MEMORY_SIZE = PAGE_COUNT * PAGE_SIZE;
    private int[] MEMORY = new int[MEMORY_SIZE];

    private HashMap<Integer, Integer> allocatedMemory = new HashMap<>();
    // Physiscal įrenginiai
    public static HDD hdd;
    public static FlashMemory flashMemory;
    public static Printer printer;
    public static final int SUPERVISOR_MEMORY_START = 0;
    public static final int SUPERVISOR_MEMORY_END = 16;
    public static final int MEMORY_START = 17;
    public static final int MEMORY_END = PAGE_COUNT;
    static{
        try {
            hdd = new HDD();
            printer = new Printer();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("--------RM--------\n");
        builder.append("MODE: " + getMODE() + "\t\tPTR: " + getPTR() + "\tIC: " + getIC() + "\n");
        builder.append("PI: " + getPI() + "\t\tSI: " + getSI() + "\t\tR1: " + getR1() + "\n");
        builder.append("R2: " + getR2() + "\t\tR3: " + getR3() + "\t\tCMP: " + getCMP() + "\n");
        builder.append("IO: " + getIO() + "\t\tTI: " + getTI() + "\t\tCH1: " + getCH1() + "\n");
        builder.append("CH2: " + getCH3() + "\t\tCH3: " + getCH3() + "\t\tSHR: " + getSHR() + "\n");
        builder.append("LCK: " + getLCK() + "\n");
        builder.append("------------------\n");
        return builder.toString();
    }
    public void create_virtual_memory()
    {
        Random r = new Random(0);
        int range = PAGE_COUNT_PER_VM * MAX_VM_COUNT;
        int block = r.nextInt(range);
        while(allocatedMemory.get(block) != null)
            block = r.nextInt(range);
        
        setPTR(block); // set page table pointer

        // initialize Page Table Entries to point at different RAM pages (PTE 0 points at page 0 at RAM, PTE 1 at page 1, ..., PTE 15 - at 15)
        for(int i = 0; i < RM.ENTRIES_PER_PAGE_TABLE; i++){
            block = r.nextInt(range);
            while(allocatedMemory.get(block) != null)
                block = r.nextInt(range);
            if(i >= 0x0D && setSHR)
            {
                setPTE(i, MEMORY[getSHR() * PAGE_SIZE + (i - 0x0D)]);
                continue;
            }
            allocatedMemory.put(block, i);
            setPTE(i, block);   // virtual memory at page i create page table entry in real memory
            if(!setSHR && i == 0x0D)
            {
                setSHR(block);
                setSHR = true;
            }
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
    public void setWordAtMemory(int page, int offset, int value)
    {
        MEMORY[page * PAGE_SIZE + offset] = value;
    } 
    public int getWordAtMemory(int page, int offset)
    {
        return MEMORY[page * PAGE_SIZE + offset];
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
        int page   = PTR;  
        return PAGE_SIZE * page;
    }
    public void printPageTable()
    {
        int tableAddress = getPageTableAddress();
        System.out.println( "---------Page table -----------------");
        System.out.println( "PTR: " + getPTR());
        System.out.print( (tableAddress) + ": ");
        for (int i = 0; i < ENTRIES_PER_PAGE_TABLE; i++) {
            System.out.print(" " + MEMORY[tableAddress+i]);
        }
        System.out.println( "\n-------------------------------------");
    }
    public void printVirtualMemory(int start, int end)
    {
        if(start < 0 || start > end || end < 0 || end >= PAGE_COUNT_PER_VM)
            return;
        for(int page = start; page <= end; page++)
        {
            System.out.print(String.format("%-3d:", (page * PAGE_SIZE)));
            for (int offset = 0; offset < + PAGE_SIZE; offset++ ) {
                System.out.print(" " + getWord(page, offset));
            }
            System.out.print("\n");
        }
    }
    public void printRealMemory(int start, int end)
    {
        if(start < 0 || start > end || end < 0 || end >= MEMORY_SIZE / PAGE_SIZE)
        {
            System.out.println("Wrong start and end page interval: min: 0 max: " + (MEMORY_SIZE / PAGE_SIZE-1) + "\n");
            return;
        }
        for(int page = start; page <= end; page++)
        {
            System.out.print(String.format("%-3d:", (page * PAGE_SIZE)));
            for (int offset = 0; offset < + PAGE_SIZE; offset++ ) {
                System.out.print(" " + MEMORY[page * PAGE_SIZE + offset]);
            }
            System.out.print("\n");
        }
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
    public void printRegChange(String reg, int old, int n)
    {
        System.out.println(reg + ": " + old + " -> " + n);
    }
    public void test(){
        if(getSI() + getPI() > 0)
        {
            System.out.println("Interrupt detected...");
            setMODE((byte)1);
            if(getSI() == 1)
            {
                
            }
            else if(getSI() == 2)
            {
                StringBuilder bd = new StringBuilder();
                for (int offset = 0; offset < PAGE_SIZE; offset++)
                {
                    bd.append((char)getWord(VM.printerPage, offset));
                }
                Printer.print(bd.toString().toCharArray());
            }
            else if(getSI() == 4)
            {
                if(getLCK() == 0)
                {
                    System.out.println("Locking");
                    setLCK((byte)1);
                }
                else
                {
                    System.out.println("Unlocking");
                    setLCK((byte)0);
                }
            }
            else if(getSI() == 4)
            {
                System.out.println("Reading from shared memory");
            }
            else if(getSI() == 5)
            {
                System.out.println("Writing to shared memory");
            }
            setSI((byte)0);
            setPI((byte)0);
            setMODE((byte)0);
        }


    }
    public byte getLCK(){
        return LCK;
    }
    public void setLCK(byte v){
        printRegChange("LCK", LCK, v);
        LCK = v;
    }
    public int getSHR(){
        return SHR;
    }
    public void setSHR(int v){
        printRegChange("SHR", SHR, v);
        SHR = v;
    }
    public byte getCMP(){
        return CMP;
    }
    public void setZF(){
        CMP |= (1 << 6);
        printRegChange("ZF", getZF(), 1);
    }
    public void clearZF(){
        CMP &= ~(1 << 6);
        printRegChange("ZF", getZF(), 0);
    }
    public byte getZF(){
        return (byte)((CMP >> 6) & 1);
    }

    public void setSF(){
        CMP |= (1 << 5);
        printRegChange("SF", getSF(), 1);
    }
    public void clearSF(){
        CMP &= ~(1 << 5);
        printRegChange("SF", getZF(), 0);
    }
    public byte getSF(){
        return (byte)((CMP >> 5) & 1);
    }
    public void setOF(){
        CMP |= (1 << 4);
        printRegChange("OF", getOF(), 1);
    }
    public void clearOF(){
        CMP &= ~(1 << 4);
        printRegChange("OF", getOF(), 0);
    }
    public byte getOF(){
        return (byte)((CMP >> 4) & 1);
    }

    public int getR1() {
        return R1;
    }
    public void setR1(int r1) {
        printRegChange("R1", getR1(), r1);
        R1 = r1;
    }

    public int getR2() {
        return R2;
    }
    public void setR2(int r2) {
        printRegChange("R2", getR2(), r2);
        R2 = r2;
    }

    public int getR3() {
        return R3;
    }
    public void setR3(int r3) {
        printRegChange("R3", getR3(), r3);
        R3 = r3;
    }

    public int getPTR() {
        return PTR;
    }
    /***
     * change Page Table Pointer to point to actual page table at specified location in RAM
     * @param page   - page where the Page Table is stored in RAM
     */
    public void setPTR(int page) {
        this.PTR = page;
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
        if(PI == 1){
            System.out.println("Division by 0");
        }
        else if(PI == 2){
            System.out.println("Unrecognized opcode");
        }
        else if(PI == 3){
            System.out.println("Memory access violation");
        }
    }

    public byte getSI() {
        return SI;
    }
    public void setSI(byte SI) {
        printRegChange("SI", getSI(), SI);
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
        printRegChange("IO", getIO(), IO);
        this.IO = IO;
    }

    public byte getMODE() {
        return MODE;
    }
    public void setMODE(byte MODE) {
        printRegChange("MODE", getMODE(), MODE);
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