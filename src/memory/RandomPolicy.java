package memory;

import java.util.*;

public class RandomPolicy implements PageReplacementPolicy{
    private Set<Integer> usedFrames= new HashSet<>();
    private Random rand = new Random();

    public int selectFrameToReplace(){
        int index = rand.nextInt(usedFrames.size());
        return new ArrayList<>(usedFrames).get(index);
        
    }
    public void pageAccessed(int frameNumber){
        usedFrames.add(frameNumber);
    }
}
