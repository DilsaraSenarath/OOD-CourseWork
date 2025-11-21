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
            ParticipantSurvey.startSurvey();
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
    public static void startSurvey (){
        Scanner UserInput = new Scanner(System.in);
        System.out.println("Welcome participant! \n For the questions below, provide answers from 1(Strongly Disagree) - 5(Strongly Agree)");
        System.out.println("Q1. I enjoy taking the lead and guiding others during group activities.");
        int Ans1 = UserInput.nextInt();
        System.out.println("Q2. I prefer analyzing situations and coming up with strategic solutions.");
        int Ans2 = UserInput.nextInt();
        System.out.println("Q3. I work well with others and enjoy collaborative teamwork.");
        int Ans3 = UserInput.nextInt();
        System.out.println("Q4. I am calm under pressure and can help maintain team morale.");
        int Ans4 = UserInput.nextInt();
        System.out.println("Q5. I like making quick decisions and adapting in dynamic situations.");
        int Ans5 = UserInput.nextInt();
    }
}

