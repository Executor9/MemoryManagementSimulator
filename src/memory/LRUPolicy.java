package memory;

import java.util.LinkedList;

public class LRUPolicy implements PageReplacementPolicy {
    private LinkedList<Integer> lruQueue = new LinkedList<>();

    @Override
    public int selectFrameToReplace(){
        return lruQueue.removeFirst();
    }

    @Override
    public void pageAccessed(int frameNumber){
        lruQueue.remove((Integer) frameNumber);
        lruQueue.addLast((Integer) frameNumber);
    }
}
