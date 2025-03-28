package start;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import Predictions.nGrams;

public class ScrollableClickableList extends JPanel implements ListSelectionListener, OSInterface {

    private static final long serialVersionUID = 1L;

    private String[] commands = {"Start Population Error"};
    private JList<String> displayList;

    private JPanel manPanel;  // Reference to the man panel
    private JTextField textField; // TextField for search
    private JLabel resultLabel; // Label to display the result of the search
    private ArrayList<String> theCommands;
    
    // Constructor that accepts the manPanel to update its content
    
    public ScrollableClickableList() {
        try {
            this.theCommands = OSInterface.getCommands();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.manPanel = new JPanel(); // Ensure manPanel is initialized
        this.setLayout(new BorderLayout());

        makeMainPanel("commandsPanel"); // Pass a valid string
    }

    public ScrollableClickableList(ScrollableClickableList manPanel) {
        
        this.theCommands = manPanel.theCommands;
        this.manPanel = manPanel;
        this.setLayout(new BorderLayout());
        this.textField = manPanel.textField;

        makeMainPanel("commandsPanel"); // Ensures consistency
    }
    
    
   
    
    /**
     * NOT DONE
     * updates the list.
     *  I should have made all the commands a  heap grrr. 
     *  BUT NOW WE SLOW FR BRO
     * @param keyword: the thing that updates the commands and puts likely commands on top.
     * @throws InterruptedException 
     */
    public ArrayList<String > updateList(String keyword, String filename, int gramSize) throws InterruptedException{
    	
    	nGrams myGrams = new nGrams(gramSize, filename);
    	
    	if( (! keyword.equals("") ) && myGrams.outerKeyExists(keyword) ) {//emptiness is not a valid keyword so we block it to save runtime
    		
    		List<String> myList = (List<String>) myGrams.getSortedInnerKeys(keyword); // Ensure correct type
        	
    		myGrams.printNGrams();
        	ArrayList<String> newCommands = new ArrayList<>();

        	// Adds everything in the sorted List
        	for (int i = 0; i < myList.size(); i++) {  // Fixed condition
        	    newCommands.add(myList.get(i));
        	}
        	
        	
    		//System.out.println(keyword);
    		Set<String> thekeySet = myGrams.getInnerGrams(keyword).keySet();
    		
    		//System.out.println("KeySet: " + thekeySet);
    		
    		
    		
    		for(String command: this.theCommands) {
        		if(!thekeySet.contains(command) ) { //should check in of O(1) ish time
        			
        			newCommands.add(command); //updates if necessary
        			this.theCommands = newCommands;
        		}
        		
        	}
    		
    		return newCommands;
    		
    	}  else if( !myGrams.outerKeyExists(keyword) && !keyword.equals("") ){
    		System.out.println( "*" + keyword + "*" + "not found in nGram"); }
    	
    	return null;
    		
    }
    
    public void updateDisplayTerm(String keyword, String filename, int gramSize) {
    	ArrayList<String> myList = null;
		
    	try {
			myList = updateList(keyword, filename, gramSize);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if( myList != null ) {
    		populateList(myList);
    	}
    	
    }
    

    private void makeMainPanel(String panelName) {
        displayList = new JList<>(commands);
        displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        displayList.addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(displayList);

        this.add(new JLabel(panelName, JLabel.CENTER), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        // Create the search bar and result label
        searchBar();
        
        try {
            populateList(OSInterface.getCommands() );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            String selectedItem = displayList.getSelectedValue();
            if (selectedItem != null) {
                // Retrieve the JScrollPane from the manPanel and update it
                JScrollPane manPageScrollPane = (JScrollPane) ThreeSectionPanel.manPanel.getClientProperty("manPageScrollPane");

                // Get the manPage as a string
                String manPage = OSInterface.getInfo(selectedItem);

                // Create a new JTextArea and set the text from the manPage
                JTextArea textArea = new JTextArea(10, 30);  // 10 rows, 30 columns
                textArea.setText(manPage);

                textArea.setLineWrap(true);  // Wrap text for better readability
                textArea.setWrapStyleWord(true);  // Wrap at word boundaries

                manPageScrollPane.setViewportView(textArea);  // This will update the content inside the JScrollPane
            }
        }
    }

    public void searchBar() {
        // Create a JTextField for search input
        textField = new JTextField(20);
        resultLabel = new JLabel("Search: ");

        // Add ActionListener to the text field
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userInput = textField.getText();
                resultLabel.setText("Searching for: " + userInput);

                // Filter the list based on the search input
                filterList(userInput);
            }
        });

        // Add search components to the panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(resultLabel);
        searchPanel.add(textField);
        
        this.add(searchPanel, BorderLayout.SOUTH);  // Place the search bar on top
    }

    private void filterList(String searchText) {
        ArrayList<String> filteredCommands = new ArrayList<>();
       
        System.out.println(searchText.isEmpty());
        //case of resetting the list
        if(searchText.isEmpty() ) {
				populateList(this.theCommands ); //this isn't working
        }
        
        
        for (String command : theCommands) {
            if (command.toLowerCase().contains(searchText.toLowerCase())) {
                filteredCommands.add(command);
            }
        }
        
        if (filteredCommands.isEmpty()) {
            resultLabel.setText("No results found.");
          
        } else {
        populateList(filteredCommands);
        }
        
    }
    
    /**
     * 
     * @param aList an arrayList that activiely updates the values
     */
    public void populateList(ArrayList<String> aList) {
        SwingUtilities.invokeLater(() -> {
        	
        	displayList.setListData(aList.toArray(new String[0]));
            //System.out.println("done");
        });
    }
    


    public static void updateContent(JLabel label, String newContent) {
        label.setText("<html>" + newContent.replaceAll("\n", "<br>") + "</html>");
    }
}
