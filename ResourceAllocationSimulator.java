import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Backend Logic
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

    public void displayGraph() {
        for (String node : adjList.keySet()) {
            System.out.println(node + " -> " + adjList.get(node));
        }
    }
}

// GUI using Swing
class ResourceAllocationSimulator {
    private ResourceAllocationGraph graph;
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField processField, resourceField;

    public ResourceAllocationSimulator() {
        graph = new ResourceAllocationGraph();
        frame = new JFrame("Resource Allocation Simulator");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel processLabel = new JLabel("Process:");
        processField = new JTextField(10);
        JLabel resourceLabel = new JLabel("Resource:");
        resourceField = new JTextField(10);
        JButton addProcessBtn = new JButton("Add Process");
        JButton addResourceBtn = new JButton("Add Resource");
        JButton requestBtn = new JButton("Request Resource");
        JButton allocateBtn = new JButton("Allocate Resource");
        JButton checkDeadlockBtn = new JButton("Check Deadlock");
        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);

        addProcessBtn.addActionListener(e -> {
            String process = processField.getText();
            if (!process.isEmpty()) {
                graph.addProcess(process);
                outputArea.append("Added Process: " + process + "\n");
            }
        });

        addResourceBtn.addActionListener(e -> {
            String resource = resourceField.getText();
            if (!resource.isEmpty()) {
                graph.addResource(resource);
                outputArea.append("Added Resource: " + resource + "\n");
            }
        });

        requestBtn.addActionListener(e -> {
            String process = processField.getText();
            String resource = resourceField.getText();
            if (!process.isEmpty() && !resource.isEmpty()) {
                graph.requestResource(process, resource);
                outputArea.append("Process " + process + " requested resource " + resource + "\n");
            }
        });

        allocateBtn.addActionListener(e -> {
            String process = processField.getText();
            String resource = resourceField.getText();
            if (!process.isEmpty() && !resource.isEmpty()) {
                graph.allocateResource(resource, process);
                outputArea.append("Resource " + resource + " allocated to process " + process + "\n");
            }
        });

        checkDeadlockBtn.addActionListener(e -> {
            boolean deadlock = graph.checkDeadlock();
            outputArea.append(deadlock ? "Deadlock Detected!\n" : "No Deadlock Detected.\n");
        });

        frame.add(processLabel);
        frame.add(processField);
        frame.add(resourceLabel);
        frame.add(resourceField);
        frame.add(addProcessBtn);
        frame.add(addResourceBtn);
        frame.add(requestBtn);
        frame.add(allocateBtn);
        frame.add(checkDeadlockBtn);
        frame.add(new JScrollPane(outputArea));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ResourceAllocationSimulator::new);
    }
}