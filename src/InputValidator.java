import java.util.Scanner;

class InputValidator {

    // We use a specific logic to ensure the app doesn't crash on letters
    public static int numberValidator(int min, int max) {
        // ideally, pass the scanner as an argument, but for this structure:Z
        Scanner userInput = new Scanner(System.in);

        int answer = 0;

        while (true) {
            // 1. Check if the next input is actually an Integer
            if (userInput.hasNextInt()) {
                answer = userInput.nextInt();

                // 2. Check Range Logic
                if (answer >= min && answer <= max) {
                    return answer;
                } else {
                    System.out.println("Invalid range. Please enter a number between " + min + " and " + max + ".");
                }
            } else {
                // 3. Handle Non-Integer Input (Letters, Symbols)
                String badInput = userInput.next(); // Read and discard the invalid token
                System.out.println("Invalid input: \"" + badInput + "\". Please enter a valid number.");
            }
            // The loop repeats until a valid number in the correct range is returned
            userInput.nextLine();
        }
    }
}