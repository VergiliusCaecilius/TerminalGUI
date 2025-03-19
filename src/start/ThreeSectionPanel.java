package start;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ThreeSectionPanel extends JPanel {
    private static final long serialVersionUID = -4036364215011277349L;
    public static ScrollableClickableList commandsPanel = new ScrollableClickableList();
    public static TerminalPanel terminalPanel = new TerminalPanel();
    public static JPanel manPanel = makeManPanel();

    public ThreeSectionPanel() {
        JFrame frame = new JFrame("Three Section Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());
        JPanel mainPanel = makeMainFrame();
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static JPanel makeMainFrame() {
        // Main panel with GridLayout (1 row, 3 columns)
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 7, 10)); // Horizontal layout

        // Add sub-panels
        mainPanel.add(commandsPanel);
        mainPanel.add(terminalPanel.getTerminalPanel()); // Using TerminalPanel instance
        mainPanel.add(manPanel);

        return mainPanel;
    }
    

    // Panel for Manual Section
    public static JPanel makeManPanel() {
        JPanel manPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        manPanel.add(scrollPane);
        manPanel.putClientProperty("manPageScrollPane", scrollPane);
        return manPanel;
    }
    
    
    public String getUserCommand() {
    	return terminalPanel.getMostRecentInput();
    }
    
    public void updateCommandsList(String keyword, String filename, int gramSize) {
 
    		commandsPanel.updateDisplayTerm(keyword, filename, gramSize);
    
    	
    }
    
    
    public void updateTerminalPanel(String text) {
    	terminalPanel.updateText(text);
    } 
}