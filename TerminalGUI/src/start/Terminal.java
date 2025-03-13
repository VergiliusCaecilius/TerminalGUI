package start;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;


public class Terminal {
    private static final String INPUTFILE = "inputFile.txt";
    private static final String OUTPUTFILE = "outputFile.txt";

    public static void main(String[] args) {
        try {
            FileWriter inputWriter = new FileWriter(new File(INPUTFILE), true);
            FileWriter outputWriter = new FileWriter(new File(OUTPUTFILE), true);

            // Homebrew path adjustment
            String homebrewPath = "/opt/homebrew/bin";
            String username = "jacobtinkelman";
            ThreeSectionPanel theGUI = new ThreeSectionPanel();
            
            // Create process with environment variables set properly
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash");
            
            // we load homebrew into the processbuilder from the homebrew on our mac
            Map<String, String> env = processBuilder.environment();
            env.put("PATH", env.getOrDefault("PATH", "") + ":" + homebrewPath);
            
            processBuilder.redirectErrorStream(true); // Merge stdout and stderr
            
            // Start as the specified user
            processBuilder.command("sudo", "-u", username, "/bin/bash");
            Process process = processBuilder.start();

            // Get input/output streams
            BufferedWriter terminalWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            BufferedReader terminalReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String userInput = null;
            
            // Send Homebrew environment setup command
            terminalWriter.write("eval \"$(/opt/homebrew/bin/brew shellenv)\"\n");
            terminalWriter.flush();

            // Start reading output in a separate thread
            Thread outputReaderThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = terminalReader.readLine()) != null) {
                        theGUI.updateTerminalPanel("Terminal: " + line + "\n");
                        outputWriter.write(line + "\n");
                        outputWriter.flush(); // Ensure output is written immediately
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputReaderThread.start();  // Start the output reader thread

            // UI updates and command handling loop
            theGUI.updateTerminalPanel("Bash interactive session started. Type commands or 'exit' to quit.\n");
            
            while (true) {
                userInput = theGUI.getUserCommand() + "\n"; //this \n is crucial

                if ("exit".equalsIgnoreCase(userInput)) {
                    terminalWriter.write("exit\n");
                    terminalWriter.flush();
                    break;
                }
                
             // Send the command to the Bash shell
                
                terminalWriter.write(userInput);
                terminalWriter.flush();
                
                //just the command
                if(!userInput.equals("\n") ) {
	                int spaceIndex = userInput.indexOf(' '); // Find the first space
	
	                String theCommand = (spaceIndex == -1) ? userInput : userInput.substring(0, spaceIndex);
	                System.out.println(theCommand + "was written");
	             
	                inputWriter.write(theCommand); // Writes to the input file
                }
            }
            
            
            

            // Cleanup after command execution
            inputWriter.close();
            outputWriter.close();
            terminalWriter.close();
            terminalReader.close();
            process.waitFor();
            outputReaderThread.join(); // Ensure the output thread completes after the process finishes

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
