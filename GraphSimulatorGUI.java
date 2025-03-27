import javax.swing.*;

public class GraphSimulatorGUI {
    private ResourceAllocationGraph rag;
    private JTextArea graphDisplay;

    public GraphSimulatorGUI() {
        rag = new ResourceAllocationGraph();

        JFrame frame = new JFrame("Resource Allocation Graph Simulator");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void updateGraphDisplay() {
        graphDisplay.setText(rag.getGraphAsString());
    }

    public static void main(String[] args) {
        new GraphSimulatorGUI();
    }
}
