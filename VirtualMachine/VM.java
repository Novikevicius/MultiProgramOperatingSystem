package MultiProgramOperatingSystem.VirtualMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import MultiProgramOperatingSystem.Main;
import MultiProgramOperatingSystem.MOS.Kernel;
import MultiProgramOperatingSystem.Processes.Process;
import MultiProgramOperatingSystem.RealMachine.*;
import MultiProgramOperatingSystem.Resources.InterruptResource;

public class VM {
    private static final int PAGE_SIZE = 16;
    private static final int PAGE_COUNT = 16;
    private static final int MEMORY_SIZE = PAGE_SIZE * PAGE_COUNT;
    public static final int DATA_SEGMENT_START = 0;
    public static final int CODE_SEGMENT_START = PAGE_SIZE * 4;
    public static final int SHARED_MEMORY_SEGMENT = 0x0D;
    public static int printerPage;
    
    private RM rm;
    private RM saved;
    private Process process;
    private boolean running = false;
    private boolean repeat = false;
    public VM(RM rm, Process p) {
        this.rm = rm;
        process = p;
    }
    public void saveValues()
    {
        saved = (RM) rm.clone();
    }
    public void loadValues()
    {
        if(saved == null) return;
        rm = (RM)saved.clone();
    }
    public void printRegisters()
    {
        System.out.println("-----------VM-------------");
        System.out.println("R1: " + getR1() + "\t R2: " + getR2() + "\t R3: " + getR3());
        System.out.println("IC: " + getIC() + "\t CMP: " + rm.getCMP());
        System.out.println("--------------------------");
    }
    public void runProgram() throws Exception {
        if(!running){
            setIC(CODE_SEGMENT_START);
            running = true;
        }      
        try
        {
            while (true)
            {
                repeat = false;
                executeInstruction();
                if(repeat) continue;
                rm.test();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch(Exception e)
        {
            if(e.getMessage().contains("Interrupt"))
            {
                String[] interrupt = e.getMessage().split(" ");
                Kernel.getInstance().freeResource(new InterruptResource(process, interrupt[1]));
                return;
            }
            if(e.getMessage().equals("HALT")){
                Kernel.getInstance().freeResource(new InterruptResource(process, "HALT"));
                running = false;
                return;
            }
            if(e.getMessage().equals("SEMAPHORE")){
                Kernel.getInstance().freeResource(new InterruptResource(process, "SEMAPHORE"));
                return;
            }
            if(e.getMessage().contains("PRINT")){
                Kernel.getInstance().freeResource(new InterruptResource(process, e.getMessage()));
                return;
            }
            if(e.getMessage().equals("MEMORY")){
                Kernel.getInstance().freeResource(new InterruptResource(process, "MEMORY"));
                return;
            }
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
                    writeWord(offset++, instr.getOpcode());
                    for (int i = 0; i < instr.getArgCount(); i++){
                        writeWord(offset++, Integer.parseInt(split[i+1]));
                    }
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
        if(Main.DEBUG_VM)
        {
            System.out.println("[VM]Press ENTER to execute instruction: " + Instruction.getCommandName(op));
            System.out.println("Enter help for more debug commands");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
            String input = reader.readLine();
            if (input.equals("help"))
            {
                repeat = true;
                System.out.println("use <rm >to print Real Memory: usage rm <start> <end>");
                System.out.println("\t\t- rm 0 5");
                System.out.println("use <vm> to print Virtual Memory: usage vm <start> <end>");
                System.out.println("\t\t- vm 0 5");
                System.out.println("use <print rm> to see real machine state");
                System.out.println("use <print vm> to see virtual machine state");
                System.out.println("use <pt> to see page table");
                System.out.println("");
                return;
            }
            else if(input.equals("print rm"))
            {
                System.out.println(rm.toString());
                repeat = true;
                return;
            }
            else if(input.equals("print vm"))
            {
                printRegisters();
                repeat = true;
                return;
            }
            else if(input.equals("pt"))
            {
                rm.printPageTable();
                repeat = true;
                return;
            }
            else if(input.contains("vm") || input.contains("rm"))
            {
                repeat = true;
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
        else if (op == Instruction.JMP.getOpcode())
            JMP();
        else if (op == Instruction.JE.getOpcode())
            JE();
        else if (op == Instruction.JG.getOpcode())
            JG();
        else if (op == Instruction.JL.getOpcode())
            JL();
        else if (op == Instruction.WRT.getOpcode())
            WRT();
        else if (op == Instruction.READ.getOpcode())
            READ();
        else if (op == Instruction.LC.getOpcode())
            LC();
        else if (op == Instruction.UL.getOpcode())
            UL();
        else if (op == Instruction.SM.getOpcode())
            SM();
        else if (op == Instruction.LM.getOpcode())
            LM();
        else if (op == Instruction.HALT.getOpcode())
            HALT();
        else
            throw new Exception("Unrecognized instruction's opcode: " + op);
        if(Main.DEBUG)
        {
            System.out.println(rm.toString());
        }
    }
    public void setZF(){
        rm.setZF();
    }
    public void clearZF(){
        rm.clearZF();
    }
    public byte getZF(){
        return rm.getZF();
    }

    public void setSF(){
        rm.setSF();
    }
    public void clearSF(){
        rm.clearSF();
    }
    public byte getSF(){
        return rm.getSF();
    }
    public void setOF(){
        rm.setOF();;
    }
    public void clearOF(){
        rm.clearOF();
    }
    public byte getOF(){
        return rm.getOF();
    }

    public int getR1() {
        return rm.getR1();
    }
    public void setR1(int r1) {
        rm.setR1(r1);;
    }

    public int getR2() {
        return rm.getR2();
    }
    public void setR2(int r2) {
        rm.setR2(r2);;
    }

    public int getR3() {
        return rm.getR3();
    }
    public void setR3(int r3) {
        rm.setR3(r3);;
    }

    
    public int getIC() {
        return rm.getIC();
    }
    public void setIC(int ic) {
        rm.setIC(ic);;
    }
    public void incrementIC(){
        setIC(getIC()+1);
    }

    // VM instructions
    // Arithemtic
    public void ADD() {
        try {
            setR1(Math.addExact(getR1(), getR2()));
        } catch (ArithmeticException e) {
            setOF();
        }
        finally
        {
            if (((getR1() >> 8) & 1) == 1) {
                setSF();
            }
            rm.setTI(rm.getTI() - 1);
        }
    }
    public void SUB() {
        try {
            setR1(Math.subtractExact(getR1(), getR2()));
        } catch (ArithmeticException e) {
            setOF();
        }
        finally
        {
            if (((getR1() >> 8) & 1) == 1) {
                setSF();
            }
            rm.setTI(rm.getTI() - 1);
        }
    }
    public void MUL() {
        try {
            setR1(Math.multiplyExact(getR1(), getR2()));
        } catch (ArithmeticException e) {
            setOF();
        }
        finally
        {
            if (((getR1() >> 8) & 1) == 1) {
                setSF();
            }
            rm.setTI(rm.getTI() - 1);
        }
    }
    public void CMP() {
        if(getR1() == getR2())
            setZF();
        else
            clearZF();
        rm.setTI(rm.getTI() - 1);
    }
    public void LW1() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setR1(readWord(x1, x2));
        rm.setTI(rm.getTI() - 1);
    }    
    public void LW2() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setR2(readWord(x1, x2));
        rm.setTI(rm.getTI() - 1);
    }   
    public void LW3() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setR3(readWord(x1, x2));
        rm.setTI(rm.getTI() - 1);
    }    
    public void SW1() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        writeWord(x1, x2, getR1());
        rm.setTI(rm.getTI() - 1);
    }   
    public void SW2() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        writeWord(x1, x2, getR2());
        rm.setTI(rm.getTI() - 1);
    }   
    public void SW3() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        writeWord(x1, x2, getR3());
        rm.setTI(rm.getTI() - 1);
    }   
    public void HALT() throws Exception {
        rm.setSI((byte)3);
        rm.setTI(rm.getTI() - 1);
        throw new Exception("HALT");
    }
    public void JMP() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        setIC(PAGE_SIZE*x1 + x2);
        rm.setTI(rm.getTI() - 1);
    }
    public void JE() {
        if(getZF() == 1){
            int x1 = readWord(getIC());
            incrementIC();
            int x2 = readWord(getIC());
            incrementIC();
            setIC(PAGE_SIZE*x1 + x2);
        }
        rm.setTI(rm.getTI() - 1);
    }
    public void JG() {
        if(getZF() == 0 && getSF() == getOF()){
            int x1 = readWord(getIC());
            incrementIC();
            int x2 = readWord(getIC());
            incrementIC();
            setIC(PAGE_SIZE*x1 + x2);
        }
        rm.setTI(rm.getTI() - 1);
    }
    public void JL() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        if(getSF() != getOF()){
            setIC(PAGE_SIZE*x1 + x2);
        }
        rm.setTI(rm.getTI() - 1);
    }
    public void WRT() {
        int x = readWord(getIC());
        printerPage = x;
        incrementIC();
        RM.setCH3((byte)1);
        rm.setSI((byte)2);
        rm.setMODE((byte)1);
        rm.setTI(rm.getTI() - 3);
    }
    public void READ() {
        RM.setCH2((byte)1);
        rm.setSI((byte)1);
        rm.setMODE((byte)1);
        rm.setTI(rm.getTI() - 3);
    }
    public void LC() {
        rm.setSI((byte)4);
        rm.setTI(rm.getTI() - 1);
    }
    public void UL() {
        rm.setSI((byte)4);
        rm.setTI(rm.getTI() - 3);
    }
    public void LM() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        if(rm.getLCK() == 0)
        {
            rm.setPI((byte)3);
            return;
        }
        setR1(readWord((SHARED_MEMORY_SEGMENT +  x1) * PAGE_SIZE + x2));
        rm.setTI(rm.getTI() - 1);
    }    
    public void SM() {
        int x1 = readWord(getIC());
        incrementIC();
        int x2 = readWord(getIC());
        incrementIC();
        if(rm.getLCK() == 0)
        {
            rm.setPI((byte)3);
            return;
        }
        writeWord((SHARED_MEMORY_SEGMENT + x1) * PAGE_SIZE + x2, getR1());
        rm.setTI(rm.getTI() - 1);
    }   
}