package MultiProgramOperatingSystem.Processes;

public enum State {
    READY,
    READY_SUSPENDED,
    BLOCKED,
    BLOCKED_SUSPENDED,
    RUNNING;
}