import javax.swing.*;
import java.awt.*;

public class GraphSimulatorGUI {
    public GraphSimulatorGUI() {
        JFrame frame = new JFrame("Resource Allocation Graph Simulator");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set layout manager
        frame.setLayout(new FlowLayout());

        // Add buttons for user interaction
        JButton addProcessBtn = new JButton("Add Process");
        JButton addResourceBtn = new JButton("Add Resource");

        frame.add(addProcessBtn);
        frame.add(addResourceBtn);

        // Set visibility after adding components
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GraphSimulatorGUI();
    }
}
