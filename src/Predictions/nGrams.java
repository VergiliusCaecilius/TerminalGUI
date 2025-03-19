package Predictions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class nGrams {

    private Map<String, Map<String, Integer>> nGramMap = new HashMap<>();
    private ArrayList<String> sentenceData = new ArrayList<>();

    public nGrams(int gramSize, String filename) {
        loadSentenceData(filename);
        makeNGrams(gramSize, this.sentenceData);
    }

    public nGrams(int gramSize, ArrayList<String> theData) {
        this.sentenceData = theData;
        makeNGrams(gramSize, this.sentenceData);
    }

    /**
     * Generates n-grams from the input data and stores them in a nested HashMap.
     *
     * @param gramSize The size of the n-grams.
     * @param data     The list of words from which n-grams are generated.
     */
    private void makeNGrams(int gramSize, ArrayList<String> data) {
        for (int i = 0; i + gramSize < data.size(); i++) {
            StringBuilder keyBuilder = new StringBuilder();

            // Build the n-gram key
            for (int j = i; j < i + gramSize; j++) {
                keyBuilder.append(data.get(j)).append(" ");
            }

            String stringKey = keyBuilder.toString().trim(); // Remove trailing space
            String nextWord = data.get(i + gramSize); // The word that follows the n-gram

            // Check if key exists in map
            if (!nGramMap.containsKey(stringKey)) {
                nGramMap.put(stringKey, new HashMap<>());
            }

            // Get the inner map and update the count
            Map<String, Integer> valueMap = nGramMap.get(stringKey);
            valueMap.put(nextWord, valueMap.getOrDefault(nextWord, 0) + 1);
        }
    }

    /**
     * FIX THIS HERE !!!!!!!!
     * Returns the inner keys (words) for a given n-gram sorted by their frequency in descending order.
     *
     * @param outerKey The n-gram key to look up.
     * @return A sorted list of words that follow the given n-gram.
     */
    public List<String> getSortedInnerKeys(String outerKey) {
        if (!nGramMap.containsKey(outerKey)) {
            return Collections.emptyList(); // Return empty list if the key doesn't exist
        }

        return nGramMap.get(outerKey)
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())) // Sort by value (desc)
                .map(Map.Entry::getKey) // Extract just the keys
                .collect(Collectors.toList()); // Collect as a list
    }
    
 

    // Method to print the n-gram model for debugging
    public void printNGrams() {
    	System.out.println("nGrams");
        for (Map.Entry<String, Map<String, Integer>> entry : nGramMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println();
    }
   

    // Method to display the n-grams
    public Map<String, Map<String, Integer>> getNGramMap() {
        return this.nGramMap;
    }
    
    /**
     * 
     * @param keyword: the n grams after the following keyword
     * @return seq(a,v, a,b, a,h) keyword a -> c,b,h
     */
     public Map<String, Integer> getInnerGrams(String keyword) {
        Map<String, Integer> innerMap = this.nGramMap.get(keyword);
        if(innerMap != null) {
            return innerMap;
        } else {
            return null;
        }
    }
     
     
     /**
      * Tells you whether an outerkey Exists in the set
      * @param key: the outer-key you search with
      * @return whether or not the key exists
      */
     public boolean outerKeyExists(String key) {
    	 
    	 if(nGramMap.get(key) != null) {
    		 return true;
    	 } 
    	 return false;
   
     }

    /**
     * Reads from a file and loads the data line by line.
     *
     * @param filename the name of the file you are loading your data from
     */
    private void loadSentenceData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.sentenceData.add(line); // Add each line to the list
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading errors
        }
    }
}
