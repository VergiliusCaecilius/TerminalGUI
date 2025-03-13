package start;

import javax.swing.*;
import java.awt.*;

public class TerminalPanel extends JFrame {
    private static final long serialVersionUID = 755605664130398102L;
    private JPanel theTermPanel;
    private JTextArea terminalTextArea;
    private JTextField inputTextField;
    private StringBuilder terminalText = new StringBuilder();
    private String mostRecentInput = new String();

    public TerminalPanel() {
        // Initialize panel and text fields
        theTermPanel = new JPanel(new BorderLayout());
        terminalTextArea = new JTextArea();
        terminalTextArea.setEditable(false);
        terminalTextArea.setLineWrap(true);
        terminalTextArea.setWrapStyleWord(true);
        
        inputTextField = new JTextField();
        inputTextField.addActionListener(e -> handleUserInput());

        // Wrap text area in a scroll pane for scrolling support
        JScrollPane scrollPane = new JScrollPane(terminalTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Add components to panel
        theTermPanel.add(scrollPane, BorderLayout.CENTER);
        theTermPanel.add(inputTextField, BorderLayout.SOUTH);
    }

    private void handleUserInput() {
        String userInput = inputTextField.getText();
        if (!userInput.isEmpty()) {
            updateText("User: " + userInput + "\n");
            mostRecentInput = userInput;
            inputTextField.setText(""); // Clear input field after submission
        }
    }

    public void updateText(String newInput) {
        terminalText.append(newInput);
        terminalTextArea.setText(terminalText.toString());
    }

    public JPanel getTerminalPanel() {
        return theTermPanel;
    }
    
    /** 
     * gets it and then sets it to null
     * @return
     */
    public String getMostRecentInput() {
    	if(this.mostRecentInput != null) {
    		String oldInput = this.mostRecentInput;
    		this.mostRecentInput = "";
    		return oldInput;
    	} 
    	return this.mostRecentInput;
    	
    }
    
    
}
