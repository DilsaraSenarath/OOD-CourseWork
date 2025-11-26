import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI {
    public static void main (String[] args){
        Scanner UserInput = new Scanner(System.in);
        System.out.println("Welcome to TeamMate system! \nYou are... \n1. An organizer\n2. A participant \n");
        int choice = UserInput.nextInt();
        if (choice == 1){
            OrganizerInterface.runInterface();
        }else if (choice == 2){
            ParticipantSurvey Participant = new ParticipantSurvey();
            List<String> info = ParticipantSurvey.Questions();  // Name, Email, Role
            int[] PersonalityScore = Participant.TakeSurvey();
            PersonalityClassify Personality =  new PersonalityClassify();
            String Type = Personality.Classify(PersonalityScore);
            Participant participant = new Participant(info, Type);
            participant.displayParticipantInfo();
        }else{

        }
    }
}

class OrganizerInterface{
    public static void runInterface (){
        Scanner UserInput = new Scanner(System.in);
        System.out.println("Welcome organizer! \n1. Upload a CSV\n2. Create Teams");
        int choice = UserInput.nextInt();
        UserInput.nextLine();

        HandleCSV.copyFile(choice);
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