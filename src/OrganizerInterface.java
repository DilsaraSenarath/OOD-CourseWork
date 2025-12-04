import java.util.Scanner;

class OrganizerInterface {
    public static void runInterface (){
        Scanner UserInput = new Scanner(System.in);
        System.out.println("\n--- Organizer Menu ---");
        System.out.println("1. Upload a CSV");
        System.out.println("2. Create Teams");
        System.out.print("Enter choice: ");

        int choice = InputValidator.numberValidator(1, 2);
        UserInput.nextLine(); // consume newline

        if (choice == 1) {
            HandleCSV.copyFile(choice);
        } else if (choice == 2) {
            System.out.println("Enter preferred team size (e.g., 5):");
            int teamSize = InputValidator.numberValidator(0, 999);

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