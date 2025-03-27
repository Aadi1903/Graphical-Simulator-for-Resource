import javax.swing.*;
import java.awt.*;
import java.util.*;

// Backend: Resource Allocation Graph
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

    public void requestResource(String process, String resource) {
        adjList.get(process).add(resource);
    }

    public void allocateResource(String resource, String process) {
        adjList.get(resource).add(process);
    }

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

    public Map<String, List<String>> getGraph() {
        return adjList;
    }
}

// GUI: Resource Allocation Graph Visualization
class ResourceAllocationGUI extends JFrame {
    private ResourceAllocationGraph graph;
    private JTextArea displayArea;

    public ResourceAllocationGUI() {
        graph = new ResourceAllocationGraph();
        setTitle("Resource Allocation Graph Simulator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel();
        JButton addProcessBtn = new JButton("Add Process");
        JButton addResourceBtn = new JButton("Add Resource");
        JButton requestBtn = new JButton("Request Resource");
        JButton allocateBtn = new JButton("Allocate Resource");
        JButton checkDeadlockBtn = new JButton("Check Deadlock");
        
        controlPanel.add(addProcessBtn);
        controlPanel.add(addResourceBtn);
        controlPanel.add(requestBtn);
        controlPanel.add(allocateBtn);
        controlPanel.add(checkDeadlockBtn);
        add(controlPanel, BorderLayout.SOUTH);

        addProcessBtn.addActionListener(e -> {
            String process = JOptionPane.showInputDialog("Enter Process Name:");
            if (process != null) {
                graph.addProcess(process);
                updateDisplay();
            }
        });

        addResourceBtn.addActionListener(e -> {
            String resource = JOptionPane.showInputDialog("Enter Resource Name:");
            if (resource != null) {
                graph.addResource(resource);
                updateDisplay();
            }
        });

        requestBtn.addActionListener(e -> {
            String process = JOptionPane.showInputDialog("Enter Process Name:");
            String resource = JOptionPane.showInputDialog("Enter Resource Name:");
            if (process != null && resource != null) {
                graph.requestResource(process, resource);
                updateDisplay();
            }
        });

        allocateBtn.addActionListener(e -> {
            String resource = JOptionPane.showInputDialog("Enter Resource Name:");
            String process = JOptionPane.showInputDialog("Enter Process Name:");
            if (process != null && resource != null) {
                graph.allocateResource(resource, process);
                updateDisplay();
            }
        });

        checkDeadlockBtn.addActionListener(e -> {
            boolean deadlock = graph.checkDeadlock();
            JOptionPane.showMessageDialog(this, deadlock ? "Deadlock Detected!" : "No Deadlock Detected.");
        });
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : graph.getGraph().entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        displayArea.setText(sb.toString());
    }
}

// Main class
public class ResourceAllocationSimulator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResourceAllocationGUI().setVisible(true));
    }
}
