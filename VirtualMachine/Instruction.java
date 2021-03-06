package MultiProgramOperatingSystem.VirtualMachine;

/**
 * Instructions
 */
public enum Instruction {
    ADD(0x01),
    SUB(0x02),
    MUL(0x03),
    CMP(0x04),
    LW1(0x05, 2),
    LW2(0x06, 2),
    LW3(0x07, 2),
    SW1(0x08, 2),
    SW2(0x09, 2),
    SW3(0x0A, 2),
    JMP(0x0B, 2),
    JE(0x0C, 2),
    JG(0x0D, 2),
    JL(0x0E, 2),
    WRT(0x20, 1),
    READ(0x21, 0),
    LC(0x22, 0),
    UL(0x23, 0),
    SM(0x24, 2),
    LM(0x25, 2),
    HALT(0x10);
    //JE(0x12, 2);
    private int opcode;
    private int args;
    private Instruction(int opcode)
    {
        this(opcode, 0);
    }
    private Instruction(int opcode, int argCount)
    {
        this.opcode = opcode;
        this.args = argCount;
    }
    public int getOpcode()
    {
        return opcode;
    }
    public int getArgCount()
    {
        return args;
    }
    public static int getOpcodeByName(String name){
       return Instruction.valueOf(name).getOpcode();
    }
    public static Instruction getInstructionByName(String name){
       return Instruction.valueOf(name);
    }
    public static String getCommandName(int op)
    {
        for (Instruction instruction : Instruction.values()) {
            if(instruction.opcode == op)
                return instruction.name();
        }
        return "";
    }
}