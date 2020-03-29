package MultiProgramOperatingSystem.VirtualMachine;

/**
 * Instructions
 */
public enum Instruction {
    ADD(0x01),
    SUB(0x02),
    MUL(0x03),
    HALT(0x04);
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
}