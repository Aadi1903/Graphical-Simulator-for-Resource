import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class ResourceAllocationSimulatorGUI {
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField resourceField, processField, requestField, releaseField;
    private JButton addResourceBtn, addProcessBtn, requestBtn, releaseBtn, detectBtn;
    private Map<String, Integer> resources;
    private Map<String, List<String>> allocationMap;
    private Map<String, List<String>> requestMap;
    private List<String> processes;
    private GraphPanel graphPanel;

    public ResourceAllocationSimulatorGUI() {
        resources = new HashMap<>();
        allocationMap = new HashMap<>();
        requestMap = new HashMap<>();
        processes = new ArrayList<>();
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Resource Allocation Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        resourceField = new JTextField();
        processField = new JTextField();
        requestField = new JTextField();
        releaseField = new JTextField();

        inputPanel.add(new JLabel("Resource (Name Instances):"));
        inputPanel.add(resourceField);
        inputPanel.add(new JLabel("Process Name:"));
        inputPanel.add(processField);
        inputPanel.add(new JLabel("Request (Process Resource):"));
        inputPanel.add(requestField);
        inputPanel.add(new JLabel("Release (Process Resource):"));
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

        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        graphPanel = new GraphPanel();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(graphPanel, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

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
            showError("Format: ResourceName Instances");
            return;
        }
        try {
            String res = parts[0];
            int count = Integer.parseInt(parts[1]);
            resources.put(res, count);
            outputArea.append("Added Resource: " + res + " (Instances: " + count + ")\n");
            resourceField.setText("");
            graphPanel.repaint();
        } catch (Exception e) {
            showError("Invalid format");
        }
    }

    private void addProcess() {
        String p = processField.getText().trim();
        if (p.isEmpty() || processes.contains(p)) {
            showError("Process name invalid or already exists");
            return;
        }
        processes.add(p);
        allocationMap.put(p, new ArrayList<>());
        requestMap.put(p, new ArrayList<>());
        outputArea.append("Added Process: " + p + "\n");
        processField.setText("");
        graphPanel.repaint();
    }

    private void requestResource() {
        String[] parts = requestField.getText().split(" ");
        if (parts.length != 2) {
            showError("Format: Process Resource");
            return;
        }
        String p = parts[0], r = parts[1];
        if (!processes.contains(p) || !resources.containsKey(r)) {
            showError("Invalid process or resource");
            return;
        }
        if (resources.get(r) > 0) {
            allocateResource(p, r);
            outputArea.append(p + " allocated " + r + "\n");
        } else {
            requestMap.get(p).add(r);
            outputArea.append(p + " is BLOCKED for " + r + "\n");
        }
        requestField.setText("");
        updateState();
    }

    private void allocateResource(String p, String r) {
        allocationMap.get(p).add(r);
        resources.put(r, resources.get(r) - 1);
    }

    private void releaseResource() {
        String[] parts = releaseField.getText().split(" ");
        if (parts.length != 2) {
            showError("Format: Process Resource");
            return;
        }
        String p = parts[0], r = parts[1];
        if (!allocationMap.getOrDefault(p, new ArrayList<>()).contains(r)) {
            showError("Resource not held by process");
            return;
        }
        allocationMap.get(p).remove(r);
        resources.put(r, resources.get(r) + 1);
        outputArea.append(p + " released " + r + "\n");

        for (String proc : processes) {
            if (requestMap.get(proc).contains(r)) {
                requestMap.get(proc).remove(r);
                allocateResource(proc, r);
                outputArea.append(proc + " now allocated " + r + "\n");
            }
        }
        releaseField.setText("");
        updateState();
    }

    private void detectDeadlock() {
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        for (String p : processes) {
            if (isCyclic(p, visited, stack)) {
                outputArea.append("⛔ Deadlock Detected!\n");
                return;
            }
        }
        outputArea.append("✅ No Deadlock Detected\n");
    }

    private boolean isCyclic(String p, Set<String> visited, Set<String> stack) {
        if (stack.contains(p)) return true;
        if (visited.contains(p)) return false;
        visited.add(p);
        stack.add(p);
        for (String r : requestMap.getOrDefault(p, new ArrayList<>())) {
            for (String holder : processes) {
                if (allocationMap.getOrDefault(holder, new ArrayList<>()).contains(r)) {
                    if (isCyclic(holder, visited, stack)) return true;
                }
            }
        }
        stack.remove(p);
        return false;
    }

    private void updateState() {
        outputArea.append("\n--- Current State ---\n");
        outputArea.append("Resources: " + resources + "\n");
        outputArea.append("Allocations: " + allocationMap + "\n");
        outputArea.append("Requests: " + requestMap + "\n\n");
        graphPanel.repaint();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    class GraphPanel extends JPanel {
        public GraphPanel() {
            this.setPreferredSize(new Dimension(1000, 300));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = 100, y = 50;
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));

            for (String r : resources.keySet()) {
                g.setColor(Color.BLUE);
                g.fillOval(x, y, 40, 40);
                g.setColor(Color.WHITE);
                g.drawString(r, x + 10, y + 25);
                x += 100;
            }

            int px = 100, py = 150;
            for (String p : processes) {
                g.setColor(Color.GREEN.darker());
                g.fillRect(px, py, 40, 40);
                g.setColor(Color.WHITE);
                g.drawString(p, px + 10, py + 25);

                List<String> allocs = allocationMap.get(p);
                List<String> reqs = requestMap.get(p);
                int i = 0;
                for (String r : allocs) {
                    int rx = 100 + (new ArrayList<>(resources.keySet()).indexOf(r)) * 100;
                    g.setColor(Color.BLACK);
                    g.drawLine(px + 20, py, rx + 20, y + 40);
                    i++;
                }
                for (String r : reqs) {
                    int rx = 100 + (new ArrayList<>(resources.keySet()).indexOf(r)) * 100;
                    g.setColor(Color.RED);
                    g.drawLine(px + 20, py + 40, rx + 20, y + 40);
                }
                px += 100;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ResourceAllocationSimulatorGUI::new);
    }
}
