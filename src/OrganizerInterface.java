import java.util.Scanner;

class OrganizerInterface {

    /**
     * Displays the Organizer menu options and routes user input to the appropriate
     * logic handlers.
     */
    public static void runInterface (){
        // Initialize a scanner specifically for the Organizer's input stream
        Scanner UserInput = new Scanner(System.in);

        System.out.println("\n--- Organizer Menu ---");
        System.out.println("1. Upload a CSV");
        System.out.println("2. Create Teams");
        System.out.print("Enter choice: ");

        // Validate that the user input falls strictly within the menu options (1 or 2)
        int choice = InputValidator.numberValidator(1, 2);
        UserInput.nextLine(); // Consume the newline character left in the buffer to prevent input skipping

        if (choice == 1) {
            // Delegate the file upload and copy operation to the CSV handler
            HandleCSV.copyFile(choice);

        } else if (choice == 2) {
            // Begin the Team Formation workflow
            System.out.println("Enter preferred team size (e.g., 5):");

            // Validate the team size input to ensure a valid positive integer
            int teamSize = InputValidator.numberValidator(0, 999);

            System.out.println("Processing data and forming teams...");

            /*
             * Separate the computationally intensive logic from the main thread,
             */
            Thread processingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Execute the core team building logic with the specified team size
                    TeamBuilder.createTeams(teamSize);
                    System.out.println("\nProcess complete. Press Enter to return to menu...");
                }
            });

            // Start concurrent execution
            processingThread.start();

            try {
                // The main thread waits (joins) until the processing thread completes.
                // This ensures the menu does not reappear until the operation is finished.
                processingThread.join();

                // Pause execution to allow the user to review the console output
                // before the application loop refreshes.
                System.in.read();
            } catch (Exception e) {
                // Log stack trace if thread interruption or IO errors occur
                e.printStackTrace();
            }
        }
    }
}