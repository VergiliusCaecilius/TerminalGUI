package start;

import java.io.*;
import java.util.Map;

public class Terminal {
    private static final String INPUTFILE = "inputFile.txt";
    private static final String OUTPUTFILE = "outputFile.txt";
    private String terminalHeader = "Terminal: ";

    public static void main(String[] args) {
        try (
            BufferedWriter inputWriter = new BufferedWriter(new FileWriter(INPUTFILE, true));
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(OUTPUTFILE, true))
        ) {
            // Homebrew path adjustment
            String homebrewPath = "/opt/homebrew/bin";
            String username = System.getProperty("user.name"); // Dynamically get current user
            ThreeSectionPanel theGUI = new ThreeSectionPanel();

            // Create process with environment variables
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash");
            Map<String, String> env = processBuilder.environment();
            env.put("PATH", env.getOrDefault("PATH", "") + ":" + homebrewPath);
            
            processBuilder.redirectErrorStream(true);

            // Start process with user privileges
            processBuilder.command("sudo", "-u", username, "/bin/bash");
            Process process = processBuilder.start();

            // Get input/output streams
            BufferedWriter terminalWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            BufferedReader terminalReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Send Homebrew environment setup command
            terminalWriter.write("eval \"$(/opt/homebrew/bin/brew shellenv)\"\n");
            terminalWriter.flush();

            // Start output reader thread
            Thread outputReaderThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = terminalReader.readLine()) != null) {
                        theGUI.updateTerminalPanel("Terminal: " + line + "\n");
                        outputWriter.write(line + "\n");
                        outputWriter.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputReaderThread.start();

            // UI updates and command handling loop
            theGUI.updateTerminalPanel("Bash interactive session started. Type commands or 'exit' to quit.\n");

            String userInput;
            while (true) {
                userInput = theGUI.getUserCommand();
              
                //theGUI.updateCommandsList(userInput, INPUTFILE, 1); //updates commands accordingly
                
                
                
                if (userInput == null) continue; // Avoid null input issues

                userInput = userInput.trim() + "\n"; // Ensure proper command formatting
                
                String prettyInput = cleanString(userInput);
                theGUI.updateCommandsList(prettyInput, INPUTFILE, 1);
                
                if ("exit\n".equalsIgnoreCase(userInput)) {
                    terminalWriter.write("exit\n");
                    terminalWriter.flush();
                    break;
                }

                // Send command to Bash shell
                terminalWriter.write(userInput);
                terminalWriter.flush();

                if (!userInput.trim().isEmpty()) {
                    //System.out.println("User input: " + userInput);
                   
                	//method 
                	//processes the Input
                	int end = userInput.indexOf(" ");
                	
                	if(end == -1) {
                		end = userInput.length()-1; //last index of String
                	}
                	
                	
                	String prettyInput2 = userInput.substring(0, end) + "\n";
                	
                    inputWriter.write(prettyInput2);
                    
                    inputWriter.flush();
                }
            }

            // Cleanup
            terminalWriter.close();
            terminalReader.close();
            process.waitFor();
            outputReaderThread.join(); 

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    

  
    
    private static String cleanString(String userInput) {
    	  if (!userInput.trim().isEmpty()) {
            
          	int end = userInput.indexOf(" ");
          	
          	if(end == -1) {
          		end = userInput.length()-1; //last index of String if no space
          	}
          	userInput = userInput.replace("\n", "");
          	
          	
          	return userInput.substring(0, end);
    	  }
    	  return "";
  	
  	}
  

    
}
