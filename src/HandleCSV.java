import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class HandleCSV {

    public static void  copyFile (int choice) {
        Scanner userInput = new Scanner(System.in);
        if (choice == 1){
            System.out.println("Please enter the full path to the CSV file you want to upload:");
            String sourcePathString = userInput.nextLine().replace("\"", "");

            // Define the name of the file in the current program directory
            String fileName = Paths.get(sourcePathString).getFileName().toString();
            String targetPathString = "./" + fileName;

            try {
                // Perform the file copy using java.nio.file.Files for simplicity
                Files.copy(Paths.get(sourcePathString), Paths.get(targetPathString), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Success! CSV file copied to: " + targetPathString);
            } catch (IOException e) {
                System.out.println("Error copying file. Make sure the path is correct and the file exists.");
                // Printing the exception details for debugging
                // System.err.println("I/O Error: " + e.getMessage());
            }
        } else if (choice == 2){
            System.out.println("Starting team creation process...");
            // Logic for team creation would go here
        } else {
            System.out.println("Invalid choice for organizer.");
        }
    }
}
