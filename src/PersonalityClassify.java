
public class PersonalityClassify {
    int totalScore;
    String personalityType;

    public String Classify(int[] Ans){
        this.totalScore = (Ans[0] + Ans[1] + Ans[2] + Ans[3] + Ans[4])*4;
        if(this.totalScore >= 90){
            System.out.println("Your personality type is a \"Leader\"");
            this.personalityType = "Leader";
            return personalityType;
        }else if (this.totalScore < 90 && this.totalScore >= 70) {
            System.out.println("Your personality type is a \"Balanced\"");
            this.personalityType = "Balanced";
            return this.personalityType;
        }else if (this.totalScore < 70 && this.totalScore >= 50) {
            System.out.println("Your personality type is a \"Thinker\"");
            this.personalityType = "Thinker";
            return this.personalityType;
        }else{
            System.out.println("Please achieve a higher personality score to be eligible to be a participant");
            this.personalityType = "Invalid";
            return this.personalityType;
        }
    }
}
