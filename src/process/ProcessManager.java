package process;
import utils.Logger;
import java.util.*;

public class ProcessManager {
    private Map<Integer,Process> pidToProcessMap;
    private int pid = 1;

    public ProcessManager() {
        pidToProcessMap = new HashMap<>();
        pid = 1;
    }

    public Process createProcess(int pagesRequired) {
        Process p = new Process(pid++, pagesRequired);
        pidToProcessMap.put(p.getPid(), p);

        Logger.log("New process with pid " + p.getPid() + " has been created.");
        return p;
    }

    public void removeProcess(int pid) {
        if(pidToProcessMap.containsKey(pid)){
            pidToProcessMap.remove(pid);
            Logger.log("Process with pid " + pid + " has been deleted.");
        }
        else{
            Logger.err("Process with pid " + pid + " does not exist.");
        }
        
    }

    public Process getProcess(int pid) {
        return pidToProcessMap.get(pid);
    }

    public Map<Integer,Process> getAllProcesses(){
        return pidToProcessMap;
    }
    
}
