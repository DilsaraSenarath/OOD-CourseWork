import java.io.*;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class HandleCSV {

    public static void copyFile(int choice) {
        Scanner userInput = new Scanner(System.in);
        if (choice == 1) {
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
                // System.err.println("I/O Error: " + e.getMessage());
            }
        } else if (choice == 2) {
            System.out.println("Starting team creation process...");
            // Logic for team creation would go here
        } else {
            System.out.println("Invalid choice for organizer.");
        }
    }

    // CSV file that stores survey participants
    private static final String SURVEY_FILE = "surveyParticipant.csv";

    // Generate next ID like SP001, SP002, ...
    public static String generateNextSurveyId() {
        String lastId = null;

        try (BufferedReader br = new BufferedReader(new FileReader(SURVEY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Each line: ID,Name,Email,Game,Skill,Role,Score,Type
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    lastId = parts[0];
                }
            }
        } catch (IOException e) {
            // File might not exist yet; we'll start from SP001
        }

        int nextNumber = 1;
        if (lastId != null && lastId.startsWith("SP")) {
            try {
                String numberPart = lastId.substring(2); // skip "SP"
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }

        // Format as SP001, SP002, etc.
        if (nextNumber < 10) {
            return "SP00" + nextNumber;
        } else if (nextNumber < 100) {
            return "SP0" + nextNumber;
        } else {
            return "SP" + nextNumber;
        }
    }

    // Save one participant to surveyParticipant.csv
    // Line format: ID,Name,Email,Game,SkillLevel,Role,PersonalityScore,PersonalityType
    public static void saveSurveyParticipant(Participant participant) {
        String id = generateNextSurveyId();                 // e.g. SP001
        String line = id + "," + participant.toCSVLine();   // add ID in front

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SURVEY_FILE, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing survey participant CSV: " + e.getMessage());
        }
    }
}
