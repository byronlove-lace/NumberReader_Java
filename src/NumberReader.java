import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.lang.SecurityException;
import java.io.FileNotFoundException;
import java.io.IOException;

class NumberReader
{
        private int nonDigits;
        private int numCount;
        private int numbersNotInRange;
        private int dupeCount;
        private int removeCount;
        private int[] numbers;
        int[] removeElements;
        int[] justDigits;
        int[] output;
        private String userInput;

        // Get User Input
        NumberReader()
        {
                // Possible validation of input
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter 5 integers between 22 and 222.");
                userInput = scanner.nextLine();
                processInput();
                System.out.printf(this.toString());
                whatNow();
        }

        private void processInput()
        {
                stripInput();
                idOutOfRange();
                idDupes();
                remIlligitDigits();
        }

        // Strip Input
        private void stripInput()
        {
                char[] inputArr = userInput.toCharArray(); // Convert to char Arr for easier manipulation

                // Remove non-digits; count anything that isn't a ',' or a space as a nonDigit
                for (int i = 0; i < inputArr.length; ++i)
                {
                        if (!Character.isWhitespace(inputArr[i]))
                        {
                                if (inputArr[i] == ',')
                                {
                                        inputArr[i] = ' ';
                                }

                                else if (!Character.isDigit(inputArr[i]))
                                {
                                        ++nonDigits;
                                        inputArr[i] = ' '; 
                                }
                        }
                }
                        
                userInput = new String(inputArr); // Convert to Strring to use split
                String[] strippedInput = userInput.split("\\s+"); //.split thankfully takes regex patterns

                numCount = strippedInput.length; 
                justDigits = new int[numCount];

                for (int i = 0; i < numCount; ++i)
                {
                        justDigits[i] = Integer.parseInt(strippedInput[i]);
                }

        }

        //  Identify Out of Range 
        private void idOutOfRange()
        {
                numbersNotInRange = 0;
                removeCount = 0;
                removeElements = new int[numCount];

                for (int i = 0; i < numCount; ++i)
                {
                        if (justDigits[i] < 22 | justDigits[i] > 222)
                        {
                                removeElements[removeCount] = i;
                                ++numbersNotInRange;
                                ++removeCount;
                        }
                }
                
        }

        // Identify Dupes
        private void idDupes() 
        {
                dupeCount = 0;

                for (int i = 0; i < justDigits.length - 1; ++i) 
                {
                        for (int j = i + 1; j < justDigits.length; ++j)
                        {
                                if (justDigits[i] == justDigits[j])
                                {
                                        removeElements[removeCount] = j;
                                        ++dupeCount;
                                        ++removeCount;
                                        break; 
                                }
                        }
                }

        }

        private void remIlligitDigits()
        {
                output = new int [numCount - removeCount];

                if (removeCount > 0)
                {
                        int outputCounter = 0;
                        boolean shouldRemove;

                        for (int i = 0; i < numCount; ++i)
                        {

                                shouldRemove = false;

                                for (int j = 0; j < removeCount; ++j)
                                {

                                        if (removeElements[j] == i)
                                        {
                                                shouldRemove = true;
                                                break;
                                        }
                                }

                                if (!shouldRemove)
                                {
                                        output[outputCounter] = justDigits[i];
                                        ++outputCounter;
                                }
                        }
                }
                else
                {
                        for (int i = 0; i < numCount; ++i)
                        {
                                output[i] = justDigits[i];
                        }
                }
        }

        private void whatNow()
        {
                enum MenuChoice
                {
                        SAVE("save"),
                        SORT("sort"),
                        QUIT("quit");

                        private final String choice;

                        private MenuChoice(String choice)
                        {
                                this.choice = choice;
                        }

                        public String getChoice()
                        {
                                return choice;
                        }
                }

                boolean menu = true;
                while (menu)
                {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("What now? quit, sort, save or read?");
                        String input = scanner.nextLine();
                        input = input.toLowerCase();

                        switch(input)
                        {
                                case "read":
                                        System.out.println("Reading. *beep-boop*");
                                        readFromFile();
                                        this.processInput();
                                        System.out.printf(this.toString());
                                        break;

                                case "save":
                                        System.out.println("Saving. *beep-boop*");
                                        saveToFile();
                                        break;

                                case "sort":
                                        System.out.println("Sorting. *beep-boop*. Ascending:");
                                        insertionSort(output);

                                        for (int i = 0; i < output.length; ++i)
                                        {
                                                if (i == (output.length - 1))
                                                {
                                                        System.out.printf("%d%n", output[i]);
                                                }

                                                else
                                                {
                                                        System.out.printf("%d, ", output[i]);
                                                }
                                        }
                                        break;

                                case "quit":
                                        System.exit(0);
                                        break;
                        }
                }

        }

        private void insertionSort(int[] unsorted)
        {
                for (int i = 1; i < unsorted.length;  ++i)
                {
                        int j = i;
                        while (j > 0 && unsorted[j - 1] > unsorted[j])
                        {
                                int temp = unsorted[j];
                                unsorted[j] = unsorted[j - 1];
                                unsorted[j - 1] = temp;
                                --j;
                        }
                }
        }

        private void saveToFile()
        {
                try 
                {
                        // Setting second param to true allows for appending to file
                        BufferedWriter writer = new BufferedWriter(new FileWriter("storedNums.txt", true));

                        for (int i = 0; i < output.length; ++i)
                        {
                                String num;

                                if (i == (output.length - 1))
                                {
                                        num = String.format("%d%n", output[i]);
                                }

                                else
                                {
                                       num = String.format("%d,", output[i]); 
                                }

                                writer.write(num);
                        }

                        writer.close();
                }
                catch (SecurityException securityException)
                {
                        System.err.println("Write permission denied. Terminating");
                        System.exit(1);
                }
                catch (FileNotFoundException fileNotFoundException)
                {
                        System.err.println("Error opening file. Terminatin");
                        System.exit(1);
                }
                catch (IOException ioException)
                {
                        ioException.printStackTrace();
                        System.exit(1);
                }
        }

        private void readFromFile()
        {
                String fileName;
                System.out.println("Enter the name of the file you want to read from:");
                                
                Scanner scanner = new Scanner(System.in);
                fileName = scanner.next();
                fileName = fileName.trim(); // strip whitespace 

                try 
                {
                        BufferedReader reader = new BufferedReader(new FileReader(fileName));
                        userInput = reader.readLine();

                        reader.close();
                }
                catch (SecurityException securityException)
                {
                        System.err.println("Write permission denied. Terminating");
                        System.exit(1);
                }
                catch (FileNotFoundException fileNotFoundException)
                {
                        System.err.println("Error opening file. Terminating");
                        System.exit(1);
                }
                catch (IOException ioException)
                {
                        ioException.printStackTrace();
                        System.exit(1);
                }
        }

        public String toString()
        {
                StringBuilder result = new StringBuilder();

                String newLine = String.format("%n%n");
                String itemCount = String.format("You gave me a list of %d items%n", numCount);
                String nonInts = String.format("Non-integers: %d%n", nonDigits);
                String notInRange = String.format("Numbers not in Range: %d%n", numbersNotInRange);
                String duplicates = String.format("Duplicates in Range: %d%n", dupeCount);
                String listTop = String.format("Corrected List:%n");

                result.append(newLine);
                result.append(itemCount);
                result.append(nonInts);
                result.append(notInRange);
                result.append(duplicates);
                result.append(listTop);

                for (int i = 0; i < output.length; ++i)
                {
                        if (i == (output.length - 1))
                        {
                                result.append(output[i]);
                                result.append(newLine);
                        }

                        else
                        {
                                result.append(output[i] + ", ");
                        }
                }

                return result.toString();
        }
}

