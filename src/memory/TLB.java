package memory;

import java.util.Map;
import java.util.LinkedHashMap;

public class TLB {
    private int capacity;
    private Map<String,Integer> tlb;

    public TLB(int capacity ) {
        this.capacity = capacity;
        this.tlb = new LinkedHashMap<String,Integer>(capacity,0.75f,true) {
            protected boolean removeEldestEntry(Map.Entry<String,Integer> eldest) {
                return size() > TLB.this.capacity;
            }
        };
    }

    public Integer lookup(int pid, int virtualPage) {
        return tlb.get(pid + ":" + virtualPage);
    } 

    public void update(int pid, int virtualPage, int frameNumber) {
        tlb.put(pid + ":" + virtualPage, frameNumber);
    }

    public void invalidate(int pid, int virtualPage) {
        tlb.remove(pid + ":" + virtualPage);
    }

    public void clear() {
        tlb.clear();
    }
}
