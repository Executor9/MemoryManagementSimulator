package memory;

public interface PageReplacementPolicy {
    public int selectFrameToReplace();
    public void pageAccessed(int frameNumber);
}
