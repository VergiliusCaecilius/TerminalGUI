package start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
/**
 * IMPLEMENTED
 * Functions which use a series of processes to get all the bash keyword and packets downloaded and has another process to 
 * 
 * UNIMPLEMENTED
 * direct terminal access using a c program 
 * 
 * 
 */

public interface OSInterface {

    /**
     * Takes a terminal input and executes it in the terminal.
     * If you were to give "man ls" it would give you the manual page for ls etc..
     */
	 public static void executeCommand() {
	        try {
	            Files.list(Paths.get("."))
	                 .forEach(path -> System.out.println(path.getFileName()));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	  

	

    /**
     * @return returns a list of all the possible commands
     * @throws InterruptedException 
     */
	 public static ArrayList<String> getCommands() throws InterruptedException {
	        ArrayList<String> commandList = new ArrayList<>();

	        // Command to load Homebrew and list all commands (including brew commands)
	        String command = "eval \"$(brew shellenv)\" && compgen -c";

	        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);

	        try {
	            // Start the process
	            Process commandProcess = processBuilder.start();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(commandProcess.getInputStream()));

	            String aCommand;

	            // Read each command and add it to the list
	            while ((aCommand = reader.readLine()) != null) {
	                commandList.add(aCommand);
	            }

	            // Close the BufferedReader and wait for process to finish
	            reader.close();
	            commandProcess.waitFor();

	        } catch (IOException e) {
	            e.printStackTrace();  // Log the exception
	        }

	        return commandList;  // Return the list of commands
	    }

      
                                                                                                                                                                          
    /**          Process process = processBuilder.start();                                                                                                                                                      
     * Forks a side process to get the man page for a given command
     * https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html
     */
	 
	 private static String getManPage(String aCommand) {
    	
		 StringBuilder theManString = new StringBuilder("man "); //the command we are inputing
    		
    		 
		 theManString.append( aCommand);
		 StringBuilder theCleanString = new StringBuilder(" | col -b"); //cleans it from it's manPage format 
    	 StringBuilder theCommand = theManString.append(theCleanString);
    		  
         ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", theCommand.toString() );

         return readConsoleProcess(processBuilder);
     }
	 
	 
	 private static String getHelp(String bashString) {
		 
		 StringBuilder theHelp = new StringBuilder("help "); //help + bashString 
		 theHelp.append(bashString);
		 
		 ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", theHelp.toString() );
		 
		 
		 return readConsoleProcess(processBuilder);
	 }
	 
	/**
	 * It assumes it'll probably just be a man page; it doesn't check cuz if it doesn't have one it'll be a blank string.
	 * @param anInput: a bash keyword like ("function" or "if") or a command like ("ls" or "pwd")
	 * @return the corresponding man page or help page for that function 
	 */
	 public static String getInfo(String anInput) {
		 String theInfo = getManPage(anInput); //assumes it will be a man page
		 
		 if(theInfo.isBlank()) { //case where it doesn't have man page, so it's a bash command
			 theInfo = getHelp(anInput);
		 }
		 return theInfo;
		 
	 }

	 
	 /**
	  * 
	  * @param aPBuilder takes in a built process builder and then records the terminal output of it as a String
	  * @return what the terminal outputs and makes it  a String 
	  */
	 private static String readConsoleProcess(ProcessBuilder aPBuilder) {
		 	
		 	try {
		 	StringBuilder theTerminalOutput = new StringBuilder(); //the terminals output
		        // Start the process
	         Process process = aPBuilder.start();
	
	         // Read the output of the command
	         BufferedReader reader = new BufferedReader(
	                 new InputStreamReader(process.getInputStream()));
	
	         
	         String line;
	         while ((line = reader.readLine() ) != null) {
	     
	             theTerminalOutput.append(line + "\n");
	             
	         }
	
	         // Wait for the process to finish
	         process.waitFor();
	      
	         return theTerminalOutput.toString(); //often is blank but not wrong
	         
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
		 return "Error";
	 }
}
