// Initial Backend Setup
import java.util.*;

class ResourceAllocationGraph {
    private Map<String, List<String>> adjList;

    public ResourceAllocationGraph() {
        adjList = new HashMap<>();
    }

    public void addProcess(String process) {
        adjList.putIfAbsent(process, new ArrayList<>());
    }

    public void addResource(String resource) {
        adjList.putIfAbsent(resource, new ArrayList<>());
    }
    
    //Implement request & allocation edges
public void requestResource(String process, String resource) {
    adjList.get(process).add(resource);
}

public void allocateResource(String resource, String process) {
    adjList.get(resource).add(process);
}

// Implement Deadlock Detection
private boolean detectCycle(String node, Set<String> visited, Set<String> stack) {
    if (stack.contains(node)) return true;
    if (visited.contains(node)) return false;

    visited.add(node);
    stack.add(node);
    
    for (String neighbor : adjList.getOrDefault(node, new ArrayList<>())) {
        if (detectCycle(neighbor, visited, stack)) return true;
    }

    stack.remove(node);
    return false;
}

public boolean checkDeadlock() {
    Set<String> visited = new HashSet<>();
    Set<String> stack = new HashSet<>();

    for (String node : adjList.keySet()) {
        if (detectCycle(node, visited, stack)) return true;
    }
    return false;
}


    public void displayGraph() {
        for (String node : adjList.keySet()) {
            System.out.println(node + " -> " + adjList.get(node));
        }
    }
}
