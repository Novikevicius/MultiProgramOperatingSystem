package MultiProgramOperatingSystem.RealMachine;

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
    private byte CH1;
    private byte CH2;
    private byte CH3;

    private static final int PAGE_SIZE = 16;
    private static final int PAGE_COUNT_PER_VM = 16; // per one Virtual Machine
    private static final int MAX_VM_COUNT = 64;
    private static final int WORD_SIZE = 4;

    private byte[] MEMORY = new byte[PAGE_COUNT_PER_VM * PAGE_SIZE * MAX_VM_COUNT * WORD_SIZE];

    public static HDD hdd;

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

    public byte getCH1() {
        return CH1;
    }
    public void setCH1(byte CH1) {
        this.CH1 = CH1;
    }

    public byte getCH2() {
        return CH2;
    }
    public void setCH2(byte CH2) {
        this.CH2 = CH2;
    }

    public byte getCH3() {
        return CH3;
    }
    public void setCH3(byte CH3) {
        this.CH3 = CH3;
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
}