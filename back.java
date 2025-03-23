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

    public void displayGraph() {
        for (String node : adjList.keySet()) {
            System.out.println(node + " -> " + adjList.get(node));
        }
    }
}
