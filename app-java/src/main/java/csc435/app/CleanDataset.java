package csc435.app;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;
public class CleanDataset
{
    private static long dataset_size = 0;
    private static long startTime;
    private static long endTime;

    private static void cleanDataset(String inputDirectory, String outputDirectory)  {
        // TO-DO implement clean dataset logic
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
                //Adding the size of the file to the total dataset size processed
                // Also calls further constructor to process the file with the task
                dataset_size += file.length();
                cleanAndWriteFile(file, new File(outputDir, file.getName()));
            }
        }
    }

    private static void cleanAndWriteFile(File inputFile, File outputFile) {
        try {
            //Checking if the output file's parent directory exists
            //And if it does not, creating the parent directory
            if(!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
                System.out.println("Failed to create output directory: " + outputFile.getParentFile());
                return;
            }
            try (InputStream inputStream = new FileInputStream(inputFile);
                OutputStream outputStream = new FileOutputStream(outputFile)) {

                //Buffer array to store binary chunks of text
                byte[] buffer = new byte[8192];
                int bytesRead;

                //Looping until the end of file is reached
                while ((bytesRead = inputStream.read(buffer)) != -1) {

                    //Converting binary buffer to string
                    String readData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

                    //Removing '\r' carriage characters
                    String removedCarriageString = readData.replaceAll("\r", "");

                    //Removing Repeating Sequence of Delimiters
                    String removedRepeatingDelimitersString = replaceRepeatingDelimiters(removedCarriageString);

                    //Removing Separators
                    String removedSeparatorsString = removedRepeatingDelimitersString.replaceAll("[^0-9a-zA-Z\\s]", "");

                    //Converting again to binary and writing in the file
                    byte[] cleanedBuffer = removedSeparatorsString.getBytes();
                    outputStream.write(cleanedBuffer, 0, cleanedBuffer.length);
                }
            }
        } catch (IOException e) {
            // Handling Error Case
            System.out.println("Error processing file: " + inputFile.getName());
            e.printStackTrace();
        }
    }

    private static String replaceRepeatingDelimiters(String input) {
        //Matches one or more whitespace characters in the input string
        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(input);

        StringBuffer result = new StringBuffer();

        //for each match in input, replacing with the last character
        while (matcher.find()) {
            String match = matcher.group();
            char lastChar = match.charAt(match.length() - 1);
            matcher.appendReplacement(result, String.valueOf(lastChar));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static void main(String[] args)
    {
        //Program is exited in case of improper number of arguments provided
        if (args.length != 2) {
            System.err.println("improper number of arguments");
            System.exit(1);
        }

        //Input and output directory is passed to the countWords constructor
        cleanDataset(args[0], args[1]);
        // Total execution time/wall time is calculated
        long execution_time = endTime - startTime;

        long executionTimeInSeconds = execution_time / 1000;
        double datasetSizeInMiB = ((double) dataset_size / (1024 * 1024));

        //Calculate throughput of the program
        double throughput =  datasetSizeInMiB / executionTimeInSeconds;

        System.out.print("Finished cleaning " + datasetSizeInMiB + " MiB of data");
        System.out.println(" in " + execution_time + " milliseconds");
        System.out.println("Throughput of program: " + throughput + " MiB/Second");
    }
}