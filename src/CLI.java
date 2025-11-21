import java.util.Scanner;

public class CLI {
    public static void main (String[] args){
        Scanner UserInput = new Scanner(System.in);
        System.out.println("Welcome to TeamMate system! \nYou are... \n1. An organizer\n2. A participant \n");
        int choice = UserInput.nextInt();
        if (choice == 1){
            OrganizerInterface Organizer = new OrganizerInterface();
            OrganizerInterface.runInterface();
        }else if (choice == 2){
            ParticipantSurvey Participant = new ParticipantSurvey();
            int[] Answer = Participant.TakeSurvey(UserInput);
            PersonalityClassify Personality =  new PersonalityClassify();
            String Type = Personality.Classify(Answer);
            System.out.println("Return is: " + Type);
        }else{

        }
    }
}

class OrganizerInterface{
    public static void runInterface (){
        Scanner UserInput = new Scanner(System.in);
        System.out.println("Welcome organizer! \n1. Upload a CSV\n2. Create Teams");
        int choice = UserInput.nextInt();
    }
}

class ParticipantSurvey{
    public int[] TakeSurvey (Scanner UserInput){
        int[] Answer = new int[5];
        System.out.println("Welcome participant! \nFor the questions below, provide answers from 1(Strongly Disagree) - 5(Strongly Agree)");
        System.out.println("Q1. I enjoy taking the lead and guiding others during group activities.");
        Answer[0] = UserInput.nextInt();
        System.out.println("Q2. I prefer analyzing situations and coming up with strategic solutions.");
        Answer[1] = UserInput.nextInt();
        System.out.println("Q3. I work well with others and enjoy collaborative teamwork.");
        Answer[2] = UserInput.nextInt();
        System.out.println("Q4. I am calm under pressure and can help maintain team morale.");
        Answer[3]= UserInput.nextInt();
        System.out.println("Q5. I like making quick decisions and adapting in dynamic situations.");
        Answer[4] = UserInput.nextInt();

        return Answer;
    }
}
