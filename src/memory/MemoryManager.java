package memory;
import utils.Logger;
import process.Process;
import java.util.*;


public class MemoryManager {
    private int totalFrames;
    private boolean[] frameOccupied;
    private PageReplacementPolicy replacementPolicy;    
    private TLB tlb;
    private int totalAccesses = 0;
    private int pageFaults = 0;
    private int tlbHits = 0;
    private int tlbMisses = 0;
    private int evictions = 0;


    public MemoryManager(int totalFrames, PageReplacementPolicy policy, int tlbCapacity) {
        this.totalFrames = totalFrames;
        this.frameOccupied = new boolean[totalFrames];
        this.replacementPolicy = policy;
        this.tlb  = new TLB(tlbCapacity);
    }

    public void allocate(Process p){
        int pagesNeeded = p.getPagesRequired();

        for(int pageNumber = 0; pageNumber < pagesNeeded; pageNumber++){
            int freeFrame = findFreeFrame();

            if(freeFrame == -1) {
                //System.out.println("RAM is full, Frame replacement required");
                Logger.warn("RAM is full, Frame replacement required");
                freeFrame = replacementPolicy.selectFrameToReplace();
                evictFrame(freeFrame);
            }

            frameOccupied[freeFrame] = true;
            p.getPageTable().mapPage(pageNumber, freeFrame);
            replacementPolicy.pageAccessed(freeFrame);

            //System.out.println("Allocated page " + pageNumber + " of process with pid " + p.getPid() + " to frame " + freeFrame);
            Logger.log("Allocated page " + pageNumber + " of process with pid " + p.getPid() + " to frame " + freeFrame);
        }

    }

    public void deallocate(Process p){
        List<Integer> usedFrames = p.getPageTable().getAllFrames();

        for(int frame : usedFrames) {
            frameOccupied[frame] = false;
            //System.out.println("Frame number " + frame + " has been deallocated");
            Logger.log("Frame number " + frame + " has been deallocated");
        }

        p.getPageTable().clear();
    }

    public void access(Process p, int virtualPage){
        int pid = p.getPid();
        Integer frame = tlb.lookup(pid, virtualPage);
            
        if(frame!=null) {
            Logger.log("[TLB HIT] page:"+ virtualPage + "->frame:" + frame);
            tlbHits++;
            replacementPolicy.pageAccessed(frame);
            totalAccesses++;
            Logger.log("Accessed process with pid " + pid + ", mapped page " + virtualPage + " to frame " + frame);
            return;
        }
        
        frame = p.getPageTable().getFrame(virtualPage);
        if(frame!=null) {
            Logger.log("[TLB MISS] but found in page table virtualpage:"+virtualPage+"->frame:"+frame);
            tlbMisses++;
            tlb.update(pid, virtualPage, frame);
            replacementPolicy.pageAccessed(frame);
            totalAccesses++;
            Logger.log("Accessed process with pid " + pid + ", mapped page " + virtualPage + " to frame " + frame);
            return;
        }
        
        //System.out.println("Page fault for page " + virtualPage + " for process with pid " + p.getPid());
        Logger.warn("Page fault for page " + virtualPage + " for process with pid " + p.getPid());
        pageFaults++;

        int freeFrame = findFreeFrame();

        if(freeFrame == -1) {
            //System.out.println("RAM is full, Frame replacement required");
            Logger.warn("RAM is full, Frame replacement required");
            freeFrame = replacementPolicy.selectFrameToReplace();
            evictFrame(freeFrame);
            tlb.invalidate(pid, freeFrame);
        }

        frameOccupied[freeFrame] = true;
        p.getPageTable().mapPage(virtualPage, freeFrame);
        tlb.update(pid, virtualPage, freeFrame);
        replacementPolicy.pageAccessed(freeFrame);
        totalAccesses++;
        Logger.log("Mapped page " + virtualPage + " to frame " + freeFrame);
    }

    public int findFreeFrame() {
        for(int i = 0; i < totalFrames; i++){
            if(frameOccupied[i]==false) {return i;}
        }
        return -1;
    }

    public void evictFrame(int freeFrame) {
        Logger.log("Evicting Frame");
        frameOccupied[freeFrame] = false;
        evictions++;
    }

    public int getPageFaults() {
        return pageFaults;
    }

    public void printMetrics() {
        Logger.log("\n=== Simulation Summary ===");
        Logger.log("Total Memory Accesses: " + totalAccesses);
        Logger.log("Page Faults: " + pageFaults);
        Logger.log("TLB Hits: " + tlbHits);
        Logger.log("TLB Misses: " + tlbMisses);
        Logger.log("Evictions: " + evictions);
    }

}
