package MultiProgramOperatingSystem.VirtualMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import MultiProgramOperatingSystem.Main;
import MultiProgramOperatingSystem.RealMachine.*;

public class VM {
    private int IC;
    private int CMP;
    private int R1;
    private int R2;
    private int R3;

    private static final int PAGE_SIZE = 16;
    private static final int PAGE_COUNT = 16;
    private static final int MEMORY_SIZE = PAGE_SIZE * PAGE_COUNT;
    private static final int DATA_SEGMENT_START = 0;
    private static final int CODE_SEGMENT_START = PAGE_SIZE * 4;

    private int[] asciiValues;
    
    private RM rm;
    public VM(RM rm) {
        this.rm = rm;
    }
    public void printRegisters()
    {
        System.out.println("-----------VM-------------");
        System.out.println("R1: " + getR1() + "\t R2: " + getR2() + "\t R3: " + getR3());
        System.out.println("IC: " + getIC() + "\t CMP: " + CMP);
        System.out.println("--------------------------");
    }
    public void runProgram() throws Exception {
        setIC(CODE_SEGMENT_START);      
        try
        {
            while (true)
            {
                executeInstruction();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch(Exception e)
        {
            if(e.getMessage().equals("HALT"))
                return;
            else
                throw e;
        }
    }
    public void writeWord(int address, int word)
    {
        if(address < 0 || address >= MEMORY_SIZE)
            return;
        int page = address / PAGE_SIZE;
        int offset = address - page * PAGE_SIZE;
        writeWord(page, offset, word);
    }
    /*
    public void writeWord(int address, String word)
    {
        if(address < 0 || address >= MEMORY_SIZE)
            return;
        asciiValues = new int[word.length()];
        for(int i=0; i < word.length(); i++){
            char c = name.charAt(i);
            asciiValues[i] = (int)c;
        }
        for(int el: asciiValues){
            System.out.print(el+ " ");
        }
        
    }*/
    public void writeWord(int page, int offset, int word)
    {
        if(page < 0 || page >= PAGE_COUNT || offset < 0 || offset >= PAGE_SIZE)
            return;
        rm.setWord(page, offset, word);
    }
    public int readWord(int address)
    {
        if(address < 0 || address >= MEMORY_SIZE)
            return -1;
        int page = address / PAGE_SIZE;
        int offset = address - page * PAGE_SIZE;
        return rm.getWord(page, offset);
    }
    public int readWord(int page, int offset)
    {
        return readWord(page * PAGE_SIZE + offset);
    }
    public void loadProgram(String program)
    {        
        try(BufferedReader br = new BufferedReader(new FileReader(program)))
        {
            String state = "START";
            String currentLine;
            int offset = 0;
            while ((currentLine = br.readLine()) != null)
            {
                String[] split = currentLine.split(" ");
                currentLine = split[0];
                if(state.equals("START"))
                {
                    if(currentLine.equals("DATA"))
                    {
                        state = "DATA";
                        offset = DATA_SEGMENT_START;
                        continue;
                    }
                    else
                    {
                        System.err.println("Program does not contain a data segmend");
                        return;
                    }
                }
                else if(state.equals("DATA"))
                {
                    if(currentLine.equals("CODE"))
                    {
                        state = "CODE";
                        offset = CODE_SEGMENT_START;
                    }
                    else if(currentLine.equals("DW"))
                    {
                        writeWord(offset++, Integer.parseInt(split[1]));
                    }
                    continue;
                }
                else if(state.equals("CODE"))
                {
                    Instruction instr = Instruction.getInstructionByName(currentLine);
                   /* if(instr.getOpcode() == Instruction.JMP.getOpcode())
                    {
                        writeWord(offset++, String.parseString(split[1]));
                    } else{*/
                        writeWord(offset++, instr.getOpcode());
                        for (int i = 0; i < instr.getArgCount(); i++){
                            writeWord(offset++, Integer.parseInt(split[i+1]));
                        }
                   // }
                }
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public void executeInstruction() throws Exception {
        int op = readWord(getIC());
        if(Main.DEBUG)
        {
            System.out.println("Press ENTER to execute instruction: " + op);
            System.out.println("Enter help for more debug commands");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
            String input = reader.readLine();
            if (input.equals("help"))
            {
                System.out.println("use rm to print Real Memory: usage rm <start> <end>");
                System.out.println("\t\t- rm 0 5");
                System.out.println("use vm to print Virtual Memory: usage vm <start> <end>");
                System.out.println("\t\t- vm 0 5");
                System.out.println("\n");
                return;
            }
            if(input.contains("vm") || input.contains("rm"))
            {
                try
                {
                    String[] split = input.split(" ");
                    if(split.length == 3)
                    {
                        int start = Integer.parseInt(split[1]);
                        int end = Integer.parseInt(split[2]);
                        if(input.contains("vm"))
                            rm.printVirtualMemory(start, end);
                        else if(input.contains("rm"))
                            rm.printRealMemory(start, end);
                            return;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        incrementIC();
        if(op == Instruction.ADD.getOpcode())
            ADD();
        else if (op == Instruction.SUB.getOpcode())
            SUB();
        else if (op == Instruction.MUL.getOpcode())
            MUL();
        else if (op == Instruction.CMP.getOpcode())
            CMP();
        else if (op == Instruction.LW1.getOpcode())
            LW1();
        else if (op == Instruction.LW2.getOpcode())
            LW2();
        else if (op == Instruction.LW3.getOpcode())
            LW3();
        else if (op == Instruction.SW1.getOpcode())
            SW1();
        else if (op == Instruction.SW2.getOpcode())
            SW2();
        else if (op == Instruction.SW3.getOpcode())
            SW3();
        /*else if (op == Instruction.JMP.getOpcode())
            JMP();
        else if (op == Instruction.JE.getOpcode())
            JE();*/
        else if (op == Instruction.HALT.getOpcode())
            HALT();
        else
            throw new Exception("Unrecognized instruction's opcode: " + op);
        if(Main.DEBUG)
        {
            printRegisters();
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

    
    public int getIC() {
        return IC;
    }
    public void setIC(int ic) {
        IC = ic;
    }
    public void incrementIC(){
        setIC(getIC()+1);
    }

    // VM instructions
    // Arithemtic
    public void ADD() {
        try {
            setR1(Math.addExact(getR1(), getR2()));
            if (((getR1() >> 6) & 1) == 1) {
                setSF();
            }
        } catch (ArithmeticException e) {
            setOF();
        }
    }
    public void SUB() {
        try {
            setR1(Math.subtractExact(getR1(), getR2()));
            if (((getR1() >> 6) & 1) == 1) {
                setSF();
            }
        } catch (ArithmeticException e) {
            setOF();
        }
    }
    public void MUL() {
        try {
            setR1(Math.multiplyExact(getR1(), getR2()));
            if (((getR1() >> 6) & 1) == 1) {
                setSF();
            }
        } catch (ArithmeticException e) {
            setOF();
        }
    }
    public void CMP() {
        if(getR1() == getR2())
            setZF();
        else
            clearZF();
    }
    public void LW1() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setR1(readWord(x1, x2));
    }    
    public void LW2() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setR2(readWord(x1, x2));
    }   
    public void LW3() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setR3(readWord(x1, x2));
    }    
    public void SW1() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        writeWord(x1, x2, getR1());
    }   
    public void SW2() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        writeWord(x1, x2, getR2());
    }   
    public void SW3() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        writeWord(x1, x2, getR3());
    }   
    public void HALT() throws Exception {
        throw new Exception("HALT");
    }
    /*
    public void JMP() {
        //writeWord(address, word);
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setIC(PAGE_SIZE*x1 + x2);
    }
    public void JE() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        if(getZF() == 1){
            setIC(PAGE_SIZE*x1 + x2);
        }
    }*/
}