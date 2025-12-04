import java.util.List;

public class Participant extends User implements CSVWritable {

    private String game;
    private String skillLevel;
    private Enums.Role role;
    private int personalityScore;
    private String personalityType;

    public Participant(List<String> info, int totalPersonalityScore, String type) {
        // CHANGE 2: Pass name/email to parent constructor (super)
        super(info.get(0), info.get(1));

        this.game = info.get(2);
        this.skillLevel = info.get(3);
        this.role = Enums.Role.valueOf(info.get(4)); // Convert string back to Enum
        this.personalityScore = totalPersonalityScore;
        this.personalityType = type;
    }

    // Getters
    public String getGame() {
        return game;
    }
    public String getSkillLevel() {
        return skillLevel;
    }

    public Enums.Role getRole() {
        return role;
    }
    public int getPersonalityScore() {
        return personalityScore;
    }

    public String getPersonalityType() {
        return personalityType;
    }

    // Display participant summary
    public void displayParticipantInfo() {
        System.out.println("\nParticipant Details:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Preferred Game: " + game);
        System.out.println("Skill Level: " + skillLevel);
        System.out.println("Preferred Role: " + role + " - " + role.getDescription());
        System.out.println("Personality Score: " + personalityScore);
        System.out.println("Personality Type: " + personalityType);
    }

    // Convert to a CSV line
    @Override
    public String toCSVLine() {
        return name + "," + email + "," + game + "," + skillLevel + "," +
                role + "," + personalityScore + "," + personalityType;
    }
}
