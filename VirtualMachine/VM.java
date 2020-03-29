package MultiProgramOperatingSystem.VirtualMachine;

public class VM {
    private int IC;
    private int CMP;
    private int R1;
    private int R2;
    private int R3;

    private static final int PAGE_SIZE = 16;
    private static final int PAGE_COUNT = 16;
    private static final int WORD_SIZE = 4;

    private byte[] MEMORY = new byte[PAGE_SIZE * PAGE_COUNT * WORD_SIZE];

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


    // VM instructions
    // Arithemtic
    public void ADD() {
        setR1(getR1() + getR2());
    }
    public void SUB() {
        setR1(getR1() - getR2());
    }
    public void MUL() {
        setR1(getR1() * getR2());
    }
}