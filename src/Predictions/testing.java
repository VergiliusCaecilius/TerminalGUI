package Predictions;

import java.util.List;

public class testing {
	 
	
	public static void main(String[] args) {
			String sentenceData = "testing.txt"; // Example n-gram key
	        nGrams ngrams = new nGrams(1, sentenceData);
	        
	        
	        String key = "hello";
	        ngrams.printNGrams();
	        List<String> sortedInnerKeys = ngrams.getSortedInnerKeys(key);

	        System.out.println("Sorted words following '" + key + "': " + sortedInnerKeys);
	        
	        
	        String test1 = cleanString("echo tim");
	        System.out.println(test1);
	       
	        
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

