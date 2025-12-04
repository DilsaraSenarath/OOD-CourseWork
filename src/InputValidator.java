import java.util.Scanner;

class InputValidator{
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