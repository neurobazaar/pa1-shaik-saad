package csc435.app;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CountWords
{
    private static long dataset_size = 0;
    private static long startTime;
    private static long endTime;


    private static void countWords(String inputDirectory, String outputDirectory)
    {
        // TO-DO implement count words logic
        File inputDir = new File(inputDirectory);
        File outputDir = new File(outputDirectory);

        //Checking if provided input exists
        // Also if it's a directory or not
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.out.println("Input directory does not exist or is not a directory.");
            System.exit(1);
        }

        // Checking output directory exists
        // If it does not exist, then creating a directory
        // If it encounters a problem throwing an error by exiting the program.
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            System.out.println("Failed to create an output directory.");
            System.exit(1);
        }

        //Saving the current time before starting the main executions
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
                //Adding the size of the file to the total dataset size processed
                // Also calls further constructor to process the file with the task
                dataset_size += file.length();
                countWordsAndWriteFile(file, new File(outputDir, file.getName()));
            }
        }
    }

    private static void countWordsAndWriteFile(File inputFile, File outputFile) {
        try {
            //Checking if the output file's parent directory exists
            //And if it does not, creating the parent directory
            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
                System.out.println("Failed to create output directory: " + outputFile.getParentFile());
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            FileWriter writer = new FileWriter(outputFile)) {

                //Creating a map data structure to store word & count of occurrence of that word
                Map<String, Integer> wordCountMap = new HashMap<>();
                String line;

                //Each line is read first, split on whitespace characters and saved in a strings array
                //Each word from the array is transformed to lowercase to avoid duplicates
                //then a word for it's first time is pushed into the map, if already in the map then the count is increased
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if (!word.trim().isEmpty()) {
                            word = word.toLowerCase();
                            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                        }
                    }
                }

               // Each map entry is written on separate new line in the output file
                for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
                    writer.write(entry.getKey() + " " + entry.getValue() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            //Handling error case
            System.out.println("Error processing file: " + inputFile.getName());
            e.printStackTrace();
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
        countWords(args[0], args[1]);
        // Total execution time/wall time is calculated
        long execution_time = endTime - startTime;

        long executionTimeInSeconds = execution_time / 1000;
        double datasetSizeInMiB = ((double) dataset_size / (1024 * 1024));

        //Calculate throughput of the program
        double throughput =  datasetSizeInMiB / executionTimeInSeconds;
        
        System.out.print("Finished counting " + datasetSizeInMiB + " MiB of words");
        System.out.println(" in " + execution_time + " milliseconds");
        System.out.println("Throughput of program: " + throughput + " MiB/Second");
    }
}