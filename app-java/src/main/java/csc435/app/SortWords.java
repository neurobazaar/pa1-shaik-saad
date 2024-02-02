package csc435.app;
import java.io.*;
import java.util.*;

public class SortWords
{
    private static long num_words = 0;
    private static long startTime;
    private static long endTime;

    private static void sortWords(String inputDirectory, String outputDirectory)
    {
        // TO-DO implement sort words logic
        File inputDir = new File(inputDirectory);
        File outputDir = new File(outputDirectory);

        //Checking if provided input exists
        // Also if it's a directory or not
        if(!inputDir.exists() || !inputDir.isDirectory()) {
            System.out.println("Input directory does not exist or is not a directory");
            System.exit(1);
        }

        // Checking output directory exists
        // If it does not exist, then creating a directory
        // If it encounters a problem throwing an error by exiting the program.
        if(!outputDir.exists() && !outputDir.mkdirs()) {
            System.out.println("Failed to create output directory.");
            System.exit(1);
        }

        //Creating a timestamp before starting the main executions
        startTime = System.currentTimeMillis();
        //Starting to process the files
        processFiles(inputDir, outputDir);
        //Creating a timestamp after the dataset is cleaned
        endTime = System.currentTimeMillis();
    }

    private static void processFiles(File inputDir, File outputDir) {
        //using for each to traverse through the directories/files
        for (File file : inputDir.listFiles()) {
            if (file.isDirectory()) {
                //recursively process subdirectories if the file is subdirectory
                processFiles(file, new File(outputDir, file.getName()));
            } else if (file.isFile() && file.getName().endsWith(".txt")) {
                //Calls further constructor to process the file with the task
                sortAndWriteWords(file, new File(outputDir, file.getName()));
            }
        }
    }

    private  static void sortAndWriteWords(File inputFile, File outputFile) {
        try {
            //Checking if the output file's parent directory exists
            //And if it does not, creating the parent directory
            if(!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
                System.out.println("Failed to create output directory: " + outputFile.getParentFile());
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                FileWriter writer = new FileWriter(outputFile)) {

                //Using list data structure to store the list of words in the file
                List<WordFrequency> wordFrequencyList = new ArrayList<>();
                String line;

                // Reading by line and loading the words in the list
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        String word = parts[0];
                        int frequency = Integer.parseInt(parts[1]);
                        wordFrequencyList.add(new WordFrequency(word, frequency));
                    }
                    //Incrementing the total words count
                    num_words++;
                }

                //Sorting the list in ascending order and reversing it.
                wordFrequencyList.sort(Comparator.comparingInt(WordFrequency::getFrequency).reversed());

                // Writing the sorted words list in the output file
                for (WordFrequency wordFrequency : wordFrequencyList) {
                    writer.write(wordFrequency.getWord() + " " + wordFrequency.getFrequency() + System.lineSeparator());
                }
            }
        } catch (IOException | NumberFormatException e) {
            // Handling Error Case
            System.out.println("Error processing file: " + inputFile.getName());
            e.printStackTrace();
        }
    }

    //Class for structuring List
    private static class WordFrequency {
        private final String word;
        private final int frequency;

        public WordFrequency(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        public String getWord() {
            return word;
        }

        public int getFrequency() {
            return frequency;
        }
    }

    public static void main(String[] args)
    {
        //Program is exited in case of improper number of arguments provided
        if (args.length != 2) {
            System.err.println("improper number of arguments");
            System.exit(1);
        }

        //Input and output directory is passed to the countWords constructor
        sortWords(args[0], args[1]);
        // Total execution time/wall time is calculated
        long execution_time = endTime - startTime;

        long executionTimeInSeconds = execution_time / 1000;

        //Calculate throughput of the program
        long throughput =  num_words / executionTimeInSeconds;
        
        System.out.print("Finished sorting " + num_words + " words");
        System.out.println(" in " + execution_time + " milliseconds");
        System.out.println("Throughput of program: " + throughput + " Words/Second");
    }
}