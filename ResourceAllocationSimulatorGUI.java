import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;  // This is the key import
import java.util.List;  // Explicitly import java.util.List

public class ResourceAllocationSimulatorGUI {
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField resourceField, processField, requestField, releaseField;
    private JButton addResourceBtn, addProcessBtn, requestBtn, releaseBtn, detectBtn;
    
    private Map<String, Integer> resources;
    private Map<String, List<String>> allocationMap;
    private Map<String, List<String>> requestMap;
    private List<String> processes;

    public ResourceAllocationSimulatorGUI() {
        resources = new HashMap<>();
        allocationMap = new HashMap<>();
        requestMap = new HashMap<>();
        processes = new ArrayList<>();
        
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Resource Allocation Graph Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        inputPanel.add(new JLabel("Resource (Name Instances):"));
        resourceField = new JTextField();
        inputPanel.add(resourceField);
        
        inputPanel.add(new JLabel("Process Name:"));
        processField = new JTextField();
        inputPanel.add(processField);
        
        inputPanel.add(new JLabel("Request (Process Resource):"));
        requestField = new JTextField();
        inputPanel.add(requestField);
        
        inputPanel.add(new JLabel("Release (Process Resource):"));
        releaseField = new JTextField();
        inputPanel.add(releaseField);
        
        addResourceBtn = new JButton("Add Resource");
        addProcessBtn = new JButton("Add Process");
        requestBtn = new JButton("Make Request");
        releaseBtn = new JButton("Release Resource");
        detectBtn = new JButton("Detect Deadlock");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        buttonPanel.add(addResourceBtn);
        buttonPanel.add(addProcessBtn);
        buttonPanel.add(requestBtn);
        buttonPanel.add(releaseBtn);
        buttonPanel.add(detectBtn);

        // Output Panel
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Add action listeners
        addResourceBtn.addActionListener(e -> addResource());
        addProcessBtn.addActionListener(e -> addProcess());
        requestBtn.addActionListener(e -> requestResource());
        releaseBtn.addActionListener(e -> releaseResource());
        detectBtn.addActionListener(e -> detectDeadlock());

        frame.setVisible(true);
    }

    private void addResource() {
        String[] parts = resourceField.getText().split(" ");
        if (parts.length != 2) {
            showError("Invalid format! Use: ResourceName Instances");
            return;
        }
        try {
            String resource = parts[0];
            int instances = Integer.parseInt(parts[1]);
            resources.put(resource, instances);
            outputArea.append("Added Resource: " + resource + " (Instances: " + instances + ")\n");
            resourceField.setText("");
        } catch (NumberFormatException e) {
            showError("Invalid number format for instances!");
        }
    }

    private void addProcess() {
        String process = processField.getText().trim();
        if (process.isEmpty()) {
            showError("Process name cannot be empty!");
            return;
        }
        processes.add(process);
        allocationMap.put(process, new ArrayList<>());
        requestMap.put(process, new ArrayList<>());
        outputArea.append("Added Process: " + process + "\n");
        processField.setText("");
    }

    private void requestResource() {
        String[] parts = requestField.getText().split(" ");
        if (parts.length != 2) {
            showError("Invalid format! Use: Process Resource");
            return;
        }
        String process = parts[0];
        String resource = parts[1];
        
        if (!resources.containsKey(resource)) {
            showError("Resource " + resource + " does not exist!");
            return;
        }
        if (!processes.contains(process)) {
            showError("Process " + process + " does not exist!");
            return;
        }

        if (resources.get(resource) > 0) {
            allocateResource(process, resource);
            outputArea.append(process + " allocated " + resource + "\n");
        } else {
            requestMap.get(process).add(resource);
            outputArea.append(process + " is BLOCKED waiting for " + resource + "\n");
        }
        requestField.setText("");
        updateVisualization();
    }

    private void allocateResource(String process, String resource) {
        allocationMap.get(process).add(resource);
        resources.put(resource, resources.get(resource) - 1);
    }

    private void releaseResource() {
        String[] parts = releaseField.getText().split(" ");
        if (parts.length != 2) {
            showError("Invalid format! Use: Process Resource");
            return;
        }
        String process = parts[0];
        String resource = parts[1];
        
        if (!allocationMap.get(process).contains(resource)) {
            showError(process + " does not hold " + resource);
            return;
        }

        allocationMap.get(process).remove(resource);
        resources.put(resource, resources.get(resource) + 1);
        outputArea.append(process + " released " + resource + "\n");

        // Check blocked requests
        for (String p : processes) {
            if (requestMap.get(p).contains(resource)) {
                requestMap.get(p).remove(resource);
                allocateResource(p, resource);
                outputArea.append(p + " (previously blocked) now gets " + resource + "\n");
            }
        }
        releaseField.setText("");
        updateVisualization();
    }

    private void detectDeadlock() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String process : processes) {
            if (isCyclic(process, visited, recursionStack)) {
                outputArea.append("⛔ Deadlock Detected!\n");
                return;
            }
        }
        outputArea.append("✅ No Deadlock Detected\n");
    }

    private boolean isCyclic(String process, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(process)) return true;
        if (visited.contains(process)) return false;

        visited.add(process);
        recursionStack.add(process);

        for (String resource : requestMap.get(process)) {
            for (String holder : processes) {
                if (allocationMap.get(holder).contains(resource)) {
                    if (isCyclic(holder, visited, recursionStack)) {
                        return true;
                    }
                }
            }
        }

        recursionStack.remove(process);
        return false;
    }

    private void updateVisualization() {
        // Simple text visualization
        outputArea.append("\n--- Current State ---\n");
        outputArea.append("Resources: " + resources + "\n");
        outputArea.append("Allocations: " + allocationMap + "\n");
        outputArea.append("Requests: " + requestMap + "\n\n");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResourceAllocationSimulatorGUI());
    }
}