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


    public void displayGraph() {
        for (String node : adjList.keySet()) {
            System.out.println(node + " -> " + adjList.get(node));
        }
    }
}
