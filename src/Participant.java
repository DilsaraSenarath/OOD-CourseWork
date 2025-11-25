import java.util.List;

public class Participant {
    private String name;
    private String email;
    private Enums.Role role;
    private String personalityType;

    // Constructor using the list from Questions() and the personality type
    public Participant(List<String> info, String type) {
        this.name = info.get(0);
        this.email = info.get(1);
        this.role = Enums.Role.valueOf(info.get(2)); // Convert string back to Enum
        this.personalityType = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Enums.Role getRole() {
        return role;
    }

    public String getPersonalityType() {
        return personalityType;
    }

    // Display participant summary
    public void displayParticipantInfo() {
        System.out.println("\nParticipant Details:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Preferred Role: " + role + " - " + role.getDescription());
        System.out.println("Personality Type: " + personalityType);
    }
}
