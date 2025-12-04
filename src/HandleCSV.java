import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class HandleCSV {
    private static final Logger LOGGER = Logger.getLogger(HandleCSV.class.getName());

    private static final String SURVEY_FILE = "surveyParticipant.csv";
    private static final String ORGANIZER_FILE = "OrganizerParticipants.csv";

    public static void copyFile(int choice) {
        Scanner userInput = new Scanner(System.in);
        if (choice == 1) {
            System.out.println("Please enter the full path to the CSV file you want to upload:");
            String sourcePathString = userInput.nextLine().replace("\"", "");
            String targetPathString = "./" + ORGANIZER_FILE;

            try {
                Files.copy(Paths.get(sourcePathString), Paths.get(targetPathString), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Success! CSV file copied to: " + targetPathString);
            } catch (IOException e) {
                System.out.println("Error copying file. Make sure the path is correct and the file exists.");
            }
        } else if (choice == 2) {
            System.out.println("Please select 'Create Teams' from the menu to proceed.");
        } else {
            System.out.println("Invalid choice for organizer.");
        }
    }

    public static String generateNextSurveyId() {
        String lastId = null;
        try (BufferedReader br = new BufferedReader(new FileReader(SURVEY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    lastId = parts[0];
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }

        int nextNumber = 1;
        if (lastId != null && lastId.startsWith("SP")) {
            try {
                String numberPart = lastId.substring(2);
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }

        if (nextNumber < 10) return "SP00" + nextNumber;
        else if (nextNumber < 100) return "SP0" + nextNumber;
        else return "SP" + nextNumber;
    }

    public static void saveSurveyParticipant(Participant participant) {
        String id = generateNextSurveyId();
        String line = id + "," + participant.toCSVLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SURVEY_FILE, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing survey participant CSV: " + e.getMessage());
        }
    }

    // --- LOADING LOGIC ---

    public static List<Participant> loadParticipants() {
        LOGGER.info("Reading participant files...");
        List<Participant> allParticipants = new ArrayList<>();

        System.out.println("Loading participants...");
        readParticipantsFromFile(SURVEY_FILE, allParticipants);
        readParticipantsFromFile(ORGANIZER_FILE, allParticipants);

        System.out.println("Total participants loaded: " + allParticipants.size());
        return allParticipants;
    }

    private static void readParticipantsFromFile(String fileName, List<Participant> listToAdd) {
        File file = new File(fileName);

        if (!file.exists()) {
            LOGGER.warning("File not found: " + fileName + " (Skipping)");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // Needs 8 columns: ID,Name,Email,Game,Skill,Role,Score,Type
                if (data.length < 8) continue;

                try {
                    int score = Integer.parseInt(data[6]); // Fails if header row

                    List<String> info = new ArrayList<>();
                    info.add(data[1]); // Name
                    info.add(data[2]); // Email
                    info.add(data[3]); // Game
                    info.add(data[4]); // Skill

                    // FIX: Convert Role to Uppercase and remove spaces to prevent Enum Error
                    String roleCleaned = data[5].trim().toUpperCase();
                    info.add(roleCleaned);

                    String type = data[7];

                    // Try creating the participant (This checks if Role exists in Enums)
                    Participant p = new Participant(info, score, type);
                    listToAdd.add(p);

                } catch (NumberFormatException e) {
                    // Skip header row
                    continue;
                } catch (IllegalArgumentException e) {
                    LOGGER.warning("Skipped invalid role in " + fileName);
                    // This catches the Enum error if the Role is completely wrong (e.g. "Sniper")
                    System.out.println("Warning: Skipping participant " + data[1] + " due to invalid Role: " + data[5]);
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error reading " + fileName + ": " + e.getMessage());
            System.out.println("Error reading file " + fileName + ": " + e.getMessage());
        }
    }
}