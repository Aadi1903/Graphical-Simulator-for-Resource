// Basic GUI Setup
import javax.swing.*;

public class GraphSimulatorGUI {
    public GraphSimulatorGUI() {
        JFrame frame = new JFrame("Resource Allocation Graph Simulator");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GraphSimulatorGUI();
    }
}
