package MultiProgramOperatingSystem.VirtualMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VM {
    private int IC;
    private int CMP;
    private int R1;
    private int R2;
    private int R3;

    private static final int PAGE_SIZE = 16;
    private static final int PAGE_COUNT = 16;
    private static final int WORD_SIZE = 4;
    private static final int MEMORY_SIZE = PAGE_SIZE * PAGE_COUNT;
    private int[] MEMORY = new int[MEMORY_SIZE];
    private static final int DATA_SEGMENT_START = 0;
    private static final int CODE_SEGMENT_START = PAGE_SIZE * 4;
    
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
        MEMORY[address] = word;
    }
    public void writeWord(int page, int offset, int word)
    {
        int address = page * PAGE_SIZE + offset;
        writeWord(address, word);
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
        int op = MEMORY[getIC()];
        incrementIC();
        if(op == Instruction.ADD.getOpcode())
            ADD();
        else if (op == Instruction.SUB.getOpcode())
            SUB();
        else if (op == Instruction.MUL.getOpcode())
            MUL();
        else if (op == Instruction.HALT.getOpcode())
            HALT();
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
    
    public void HALT() throws Exception {
        throw new Exception("HALT");
    }
}