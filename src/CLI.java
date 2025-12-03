import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI {
    public static void main (String[] args){
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
            System.out.print("Enter choice: ");

            int choice = UserInput.nextInt();

            if (choice == 1){
                // Run Organizer Logic
                OrganizerInterface.runInterface();

            } else if (choice == 2) {
                // Run Participant Logic
                boolean finished = false;

                while (!finished) {
                    ParticipantSurvey Participant = new ParticipantSurvey();

                    // Returns Name, Email,Preferred Game, Skill Level, Role
                    List<String> info = ParticipantSurvey.Questions();

                    // Return PersonalityScore
                    int[] PersonalityScore = Participant.TakeSurvey();
                    int totalPersonalityScore = 0;
                    for (int a : PersonalityScore) {
                        totalPersonalityScore += a;
                    }

                    PersonalityClassify Personality =  new PersonalityClassify();
                    String Type = Personality.Classify(PersonalityScore);

                    Participant participant = new Participant(info, totalPersonalityScore, Type);
                    participant.displayParticipantInfo();

                    // Ask user what to do next
                    System.out.println("\nWhat would you like to do?");
                    System.out.println("1. Save my details and survey to CSV");
                    System.out.println("2. Retake the survey");
                    System.out.print("Enter your choice: ");

                    int surveyChoice = UserInput.nextInt();

                    if (surveyChoice == 1) {
                        // Save to CSV and finish this participant loop
                        HandleCSV.saveSurveyParticipant(participant);
                        System.out.println("Your details and survey have been saved. Thank you!");
                        finished = true; // Breaks the participant loop, returns to Main Menu

                    } else if (surveyChoice == 2) {
                        // Retake the survey (loop repeats)
                        System.out.println("You chose to retake the survey.\n");

                    } else {
                        // Any other value: exit participant flow
                        System.out.println("Invalid choice. Returning to main menu.");
                        finished = true;
                    }
                }

            } else if (choice == 3) {
                // Exit the main loop
                System.out.println("Exiting TeamMate System. Goodbye!");
                systemActive = false;

            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        UserInput.close();
    }
}

class OrganizerInterface {
    public static void runInterface (){
        Scanner UserInput = new Scanner(System.in);
        System.out.println("\n--- Organizer Menu ---");
        System.out.println("1. Upload a CSV");
        System.out.println("2. Create Teams");
        System.out.print("Enter choice: ");

        int choice = UserInput.nextInt();
        UserInput.nextLine(); // consume newline

        if (choice == 1) {
            HandleCSV.copyFile(choice);
        } else if (choice == 2) {
            System.out.println("Enter preferred team size (e.g., 5):");
            int teamSize = UserInput.nextInt();

            System.out.println("Processing data and forming teams...");


            Thread processingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Call the new logic
                    TeamBuilder.createTeams(teamSize);
                    System.out.println("\nProcess complete. Press Enter to return to menu...");
                }
            });

            processingThread.start();

            try {
                processingThread.join();
                // Pause so the user can read the output before the menu appears again
                System.in.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ParticipantSurvey{
    public static List<String> Questions(){
        Scanner UserInput = new Scanner(System.in);

        System.out.println("Enter your Name:");
        String name = UserInput.nextLine();

        System.out.println("Enter your Email:");
        String email = UserInput.nextLine();

        System.out.println("Select your preferred game");
        int j = 1;
        for(Enums.Game Game:  Enums.Game.values()){
            System.out.println(String.valueOf(j) + ". " + Game);
            j++;
        }
        int gameNum = inputValidator.scoreValidator(1, j - 1);
        Enums.Game[] games = Enums.Game.values();
        String gameChoice = games[gameNum - 1].toString();

        System.out.println("From 1 to 10 (1 = lowest, 10 = Highest),\nWhat is your skill level of the above selected game?");
        int skillLevel = inputValidator.scoreValidator(1, 10);

        System.out.println("Select your preferred role");
        int i = 1;
        for(Enums.Role Role:  Enums.Role.values()){
            System.out.println( String.valueOf(i)+". " + Role +" : " + Role.getDescription());
            i++;
        }
        int roleNum = inputValidator.scoreValidator(1, i - 1);
        Enums.Role[] roles = Enums.Role.values();
        String roleChoice = roles[roleNum - 1].toString();

        List<String> participantInfo = new ArrayList<>();
        participantInfo.add(name);
        participantInfo.add(email);
        participantInfo.add(gameChoice);
        participantInfo.add(Integer.toString(skillLevel));
        participantInfo.add(roleChoice);

        return participantInfo;
    }
    public int[] TakeSurvey (){
        int[] Answer = new int[5];
        System.out.println("Personality classification...\nFor the questions below, provide answers from 1(Strongly Disagree) - 5(Strongly Agree)");
        System.out.println("Q1. I enjoy taking the lead and guiding others during group activities.");
        Answer[0] = inputValidator.scoreValidator(1,5);
        System.out.println("Q2. I prefer analyzing situations and coming up with strategic solutions.");
        Answer[1] = inputValidator.scoreValidator(1,5);
        System.out.println("Q3. I work well with others and enjoy collaborative teamwork.");
        Answer[2] = inputValidator.scoreValidator(1,5);
        System.out.println("Q4. I am calm under pressure and can help maintain team morale.");
        Answer[3] = inputValidator.scoreValidator(1,5);
        System.out.println("Q5. I like making quick decisions and adapting in dynamic situations.");
        Answer[4] = inputValidator.scoreValidator(1,5);

        return Answer;
    }
}

class inputValidator{
    public static int scoreValidator (int min, int max) {
        Scanner UserInput = new Scanner(System.in);
        int answer = 0;
        while (true) {
            answer = UserInput.nextInt();
            if(answer>= min && answer<= max){
                System.out.println("Answer accepted");
                return answer;
            }else{
                System.out.println("Answer rejected");
            }
        }
    }
}