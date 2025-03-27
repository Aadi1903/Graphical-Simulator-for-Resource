import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphSimulatorGUI {
    private ResourceAllocationGraph rag;
    private JTextArea graphDisplay;

    public GraphSimulatorGUI() {
        rag = new ResourceAllocationGraph();

        JFrame frame = new JFrame("Resource Allocation Graph Simulator");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        graphDisplay = new JTextArea(10, 40);
        frame.add(new JScrollPane(graphDisplay));

        JButton addProcessBtn = new JButton("Add Process");
        JButton addResourceBtn = new JButton("Add Resource");
        JButton requestBtn = new JButton("Request Resource");
        JButton allocateBtn = new JButton("Allocate Resource");
        JButton checkDeadlockBtn = new JButton("Check Deadlock");

        frame.add(addProcessBtn);
        frame.add(addResourceBtn);
        frame.add(requestBtn);
        frame.add(allocateBtn);
        frame.add(checkDeadlockBtn);

        addProcessBtn.addActionListener(e -> {
            String process = JOptionPane.showInputDialog("Enter Process Name:");
            if (process != null) {
                rag.addProcess(process);
                updateGraphDisplay();
            }
        });

        addResourceBtn.addActionListener(e -> {
            String resource = JOptionPane.showInputDialog("Enter Resource Name:");
            if (resource != null) {
                rag.addResource(resource);
                updateGraphDisplay();
            }
        });

        requestBtn.addActionListener(e -> {
            String process = JOptionPane.showInputDialog("Enter Process Name:");
            String resource = JOptionPane.showInputDialog("Enter Resource Name:");
            if (process != null && resource != null) {
                rag.requestResource(process, resource);
                updateGraphDisplay();
            }
        });

        allocateBtn.addActionListener(e -> {
            String resource = JOptionPane.showInputDialog("Enter Resource Name:");
            String process = JOptionPane.showInputDialog("Enter Process Name:");
            if (resource != null && process != null) {
                rag.allocateResource(resource, process);
                updateGraphDisplay();
            }
        });

        checkDeadlockBtn.addActionListener(e -> {
            boolean deadlock = rag.checkDeadlock();
            JOptionPane.showMessageDialog(frame, deadlock ? "Deadlock Detected!" : "No Deadlock.");
        });

        frame.setVisible(true);
    }

    private void updateGraphDisplay() {
        graphDisplay.setText(rag.getGraphAsString());
    }

    public static void main(String[] args) {
        new GraphSimulatorGUI();
    }
}
