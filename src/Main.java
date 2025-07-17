// import memory.*;
// import process.Process;
// import process.ProcessManager;
// import utils.*;
// import java.util.LinkedHashMap;
// import java.util.Map;


// public class Main {
//     public static int runSimulation(String policyName, PageReplacementPolicy policy) {
//         Logger.log("\n=== Running with " + policyName + " ===");

//     MemoryManager memoryManager = new MemoryManager(3, policy);
//     ProcessManager processManager = new ProcessManager();

//     Process p = processManager.createProcess(6);

//     int[] accessSequence = {0, 1, 2, 0, 3, 0, 4, 0, 5};

//     for (int vp : accessSequence) {
//         memoryManager.access(p, vp);
//     }

//     memoryManager.deallocate(p);
//     processManager.removeProcess(p.getPid());

//     Logger.log("=== Simulation finished with " + policyName + " ===");
//     return memoryManager.getPageFaults();
//     }

//     public static void main(String[] args) {
//         Logger.log("=== OS MEMORY MANAGEMENT SIMULATION STARTED ===");

//         Map<String,Integer> policyToFaults = new LinkedHashMap<>();

//         policyToFaults.put("FIFO",runSimulation("FIFO Policy", new FIFOPolicy()));
//         policyToFaults.put("LRU",runSimulation("LRU Policy", new LRUPolicy()));
//         policyToFaults.put("Random",runSimulation("Random Policy", new RandomPolicy()));

//         for(String policy : policyToFaults.keySet()) {
//             Logger.log(policy + " gives " + policyToFaults.get(policy) + " Page Faults");
//         }

//         Logger.log("=== SIMULATION COMPLETE ===");
//     }
// }


import java.util.Scanner;

import memory.*;
import process.Process;
import process.ProcessManager;
import utils.Logger;
import utils.*;

public class Main {
    public static void main(String[] args) {
        Logger.log("=== OS MEMORY MANAGEMENT SIMULATION WITH TLB ===");

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total number of frames: ");
        int totalFrames = sc.nextInt();

        System.out.print("Enter TLB capacity: ");
        int tlbCapacity = sc.nextInt();

        System.out.print("Enter replacement policy (FIFO/LRU/RANDOM): ");
        String policy = sc.next().toUpperCase();

        PageReplacementPolicy replacementPolicy = switch (policy) {
            case "LRU" -> new LRUPolicy();
            case "FIFO" -> new FIFOPolicy();
            case "RANDOM" -> new RandomPolicy();
            default -> throw new IllegalArgumentException("Unknown policy");
        };

        MemoryManager memory = new MemoryManager(totalFrames, replacementPolicy,tlbCapacity);

        // Setup process manager and create a process
        ProcessManager pm = new ProcessManager();
        Process p1 = pm.createProcess(1);

        Logger.log("-- Allocating pages for Process 1 --");

        // Allocate 3 pages → fills 3 frames
        memory.access(p1, 0); // Page fault, allocate
        memory.access(p1, 1); // Page fault, allocate
        memory.access(p1, 2); // Page fault, allocate

        // Access again → should be TLB hits
        memory.access(p1, 0); // TLB hit
        memory.access(p1, 1); // TLB hit
        memory.access(p1, 2); // TLB hit

        Logger.log("-- Triggering eviction with new page --");

        // Access another page → causes replacement and TLB update
        memory.access(p1, 3); // Page fault, evict, update TLB

        Logger.log("-- Accessing old pages again to show TLB miss and page table hit/fault --");
        memory.access(p1, 0); // Might be page table hit or fault if evicted
        memory.access(p1, 2); // Might be hit or fault

        Logger.log("-- Deallocating Process 1 --");
        memory.deallocate(p1);

        memory.printMetrics();

        Logger.log("=== SIMULATION COMPLETE ===");
    }
}
