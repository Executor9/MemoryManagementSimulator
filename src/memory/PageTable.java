package memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// public class PageTable {
//     private Map<Integer,Integer> pageToFrameMap = new HashMap<>();

//     public void mapPage(int pageNumber, int frameNumber) {
//         pageToFrameMap.put(pageNumber,frameNumber);
//     } 

//     public Integer getFrame(int pageNumber) {
//         return pageToFrameMap.get(pageNumber);
//     }

//     public Set<Integer> getAllFrames() {
        
//         return new HashSet<>(pageToFrameMap.values());
//     }

//     public void clear() {
//         pageToFrameMap.clear();
//     }
// }

public class PageTable {
    private Map<Integer,Map<Integer,Integer>> outerTable;

    public PageTable() {
        outerTable = new HashMap<>();
    }

    public void mapPage(int pageNumber, int frameNumber) {
        int outerIndex = (pageNumber>>2)& 0b11;
        int innerIndex = pageNumber & 0b11;

        outerTable.putIfAbsent(outerIndex, new HashMap<>());
        outerTable.get(outerIndex).put(innerIndex,frameNumber);
    } 

    public Integer getFrame(int pageNumber) {
        int outerIndex = (pageNumber>>2)& 0b11;
        int innerIndex = pageNumber & 0b11;

        Map<Integer,Integer> innerTable = outerTable.get(outerIndex);
        if(innerTable==null) {return null;}
        return innerTable.get(innerIndex);
    }

    public List<Integer> getAllFrames() {
        List<Integer> frames = new ArrayList<>();

        for(Map<Integer,Integer> inner : outerTable.values()) {
            frames.addAll(inner.values());
        }

        return frames;
        
    }

    public void unmapFrame(int frameNumber) {
        for (Map<Integer, Integer> inner : outerTable.values()) {
            inner.entrySet().removeIf(e -> e.getValue() == frameNumber);
        }
    }

    public void clear() {
        outerTable.clear();
    }
}