package process;

import memory.PageTable;

public class Process {
    private int pid;
    private int pagesRequired;
    private PageTable pageTable;

    public Process(int pid, int memoryRequired) {
        this.pid = pid;
        this.pagesRequired = memoryRequired;
        this.pageTable = new PageTable();
    }

    public int getPid() {
        return pid;
    }

    public int getPagesRequired() {
        return pagesRequired;
    }

    public PageTable getPageTable() {
        return pageTable;
    }

}
