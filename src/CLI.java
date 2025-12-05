import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class CLI {
    private static final Logger LOGGER = Logger.getLogger(CLI.class.getName());
    public static void main (String[] args){
        AppLogger.setup();
        LOGGER.info("System Initialized...");
        Scanner UserInput = new Scanner(System.in);

        // Flag to keep the application running until the user chooses to exit
        boolean systemActive = true;

        while (systemActive) {
            System.out.println("\n=================================");
            System.out.println("Welcome to TeamMate system!");
            System.out.println("Please select your role:");
            System.out.println("1. An organizer");
            System.out.println("2. A participant");
            System.out.println("3. Exit System");
            System.out.println("=================================");
            System.out.println("Enter choice: ");
            int choice = InputValidator.numberValidator(1,3);
            if (choice == 1){
                // Run Organizer Logic
                OrganizerInterface.runInterface();

            } else if (choice == 2) {
                // Run Participant Logic
                boolean finished = false;

                while (!finished) {
                    ParticipantSurvey participantSurvey = new ParticipantSurvey();

                    // Returns Name, Email,Preferred Game, Skill Level, Role
                    List<String> info = ParticipantSurvey.Questions();

                    // Return PersonalityScore
                    int[] PersonalityScore = participantSurvey.TakeSurvey();
                    int totalPersonalityScore = 0;
                    for (int a : PersonalityScore) {
                        totalPersonalityScore += a;
                    }

                    PersonalityClassify surveyHelper =  new PersonalityClassify();
                    String Type = surveyHelper.classify(PersonalityScore);

                    Participant participant = new Participant(info, totalPersonalityScore, Type);
                    participant.displayParticipantInfo();

                    // Ask user what to do next
                    System.out.println("\nWhat would you like to do?");
                    System.out.println("1. Save my details and survey to CSV");
                    System.out.println("2. Retake the survey");
                    System.out.print("Enter your choice: ");

                    int surveyChoice = InputValidator.numberValidator(1, 2);

                    if (surveyChoice == 1) {
                        // Save to CSV and finish this participant loop
                        HandleCSV.saveSurveyParticipant(participant);
                        System.out.println("Your details and survey have been saved. Thank you!");
                        finished = true; // Breaks the participant loop, returns to Main Menu

                    } else if (surveyChoice == 2) {
                        // Retake the survey (loop repeats)
                        System.out.println("You chose to retake the survey.\n");

                    }
                }

            } else if (choice == 3) {
                // Exit the main loop
                LOGGER.info("System shutting down.");
                System.out.println("Exiting TeamMate System. Goodbye!");
                systemActive = false;

            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        UserInput.close();
    }
}