package MultiProgramOperatingSystem.VirtualMachine;

/**
 * Instructions
 */
public enum Instruction {
    ADD(0x01),
    SUB(0x02),
    MUL(0x03);
    private int opcode;
    
    private Instruction(int opcode)
    {
        this.opcode = opcode;
    }
    public int getOpcode()
    {
        return opcode;
    }
}