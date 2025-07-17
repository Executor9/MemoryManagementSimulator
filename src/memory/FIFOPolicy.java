package memory;

import java.util.Queue;
import java.util.LinkedList;

public class FIFOPolicy implements PageReplacementPolicy{
    private Queue<Integer> frameQueue = new LinkedList<>();  

    @Override
    public int selectFrameToReplace(){
        return frameQueue.poll();
    }

    @Override
    public void pageAccessed(int frameNumber){
        if (!frameQueue.contains(frameNumber)) {
            frameQueue.offer(frameNumber);
        }
    }
}
