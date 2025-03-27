import java.util.*;

class ResourceAllocationSimulator {
    private Map<String, List<String>> allocationMap = new HashMap<>();
    private Map<String, String> resourceMap = new HashMap<>(); // Tracks which resource is allocated to which process
    private List<String[]> requestQueue = new ArrayList<>(); // Stores blocked requests

    public void addProcess(String process) {
        allocationMap.put(process, new ArrayList<>());
        System.out.println("Added Process: " + process);
    }

    public void addResource(String resource) {
        resourceMap.put(resource, null); // Resource is initially free
        System.out.println("Added Resource: " + resource);
    }

    public void requestResource(String process, String resource) {
        System.out.println(process + " requested " + resource);
        
        if (resourceMap.get(resource) == null) { // If resource is free
            allocateResource(process, resource);
        } else { // If resource is occupied, block the process
            System.out.println(process + " is BLOCKED, waiting for " + resource);
            requestQueue.add(new String[]{process, resource});
        }
    }

    private void allocateResource(String process, String resource) {
        allocationMap.get(process).add(resource);
        resourceMap.put(resource, process);
        System.out.println("Allocated " + resource + " to " + process);
    }

    public void releaseResource(String process, String resource) {
        if (resourceMap.get(resource).equals(process)) {
            resourceMap.put(resource, null); // Free the resource
            allocationMap.get(process).remove(resource);
            System.out.println(process + " released " + resource);

            // Check if any blocked request can now be fulfilled
            Iterator<String[]> iterator = requestQueue.iterator();
            while (iterator.hasNext()) {
                String[] req = iterator.next();
                if (req[1].equals(resource)) {
                    allocateResource(req[0], resource);
                    iterator.remove(); // Remove from blocked queue
                }
            }
        }
    }

    public void detectDeadlock() {
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        for (String process : allocationMap.keySet()) {
            if (isCyclic(process, visited, stack)) {
                System.out.println("Deadlock Detected!");
                return;
            }
        }
        System.out.println("No Deadlock Detected");
    }

    private boolean isCyclic(String process, Set<String> visited, Set<String> stack) {
        if (stack.contains(process)) return true;
        if (visited.contains(process)) return false;

        visited.add(process);
        stack.add(process);

        for (String resource : allocationMap.get(process)) {
            String nextProcess = resourceMap.get(resource);
            if (nextProcess != null && isCyclic(nextProcess, visited, stack)) {
                return true;
            }
        }
        stack.remove(process);
        return false;
    }

    public static void main(String[] args) {
        ResourceAllocationSimulator simulator = new ResourceAllocationSimulator();
        
        // Sample scenario
        simulator.addProcess("P1");
        simulator.addProcess("P2");
        
        simulator.addResource("R1");
        simulator.addResource("R2");
        
        simulator.requestResource("P1", "R1");
        simulator.requestResource("P2", "R2");
        
        simulator.requestResource("P1", "R2"); // This should block P1
        simulator.requestResource("P2", "R1"); // This should block P2
        
        simulator.detectDeadlock();
    }
}
