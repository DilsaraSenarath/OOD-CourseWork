import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class TeamBuilder {
    private static final Logger LOGGER = Logger.getLogger(TeamBuilder.class.getName());

    // Inner class to represent a formed Team
    static class Team implements CSVWritable {
        List<Participant> members = new ArrayList<>();
        int teamId;

        @Override
        public String toCSVLine() {
            // Defines how a TEAM writes itself to CSV
            return teamId + "," + getAverageSkill() + "," + members.size();
        }


        public Team(int id) {
            this.teamId = id;
        }

        public int countGame(String game) {
            int count = 0;
            for (Participant p : members) {
                if (p.getGame().equalsIgnoreCase(game)) count++;
            }
            return count;
        }

        public double getAverageSkill() {
            if (members.isEmpty()) return 0;
            int sum = 0;
            for(Participant p : members) {
                sum += Integer.parseInt(p.getSkillLevel());
            }
            return (double) sum / members.size();
        }
    }

    public static void createTeams(int teamSize) {
        LOGGER.info("Starting team formation. Target size: " + teamSize);
        // 1. Load and Shuffle Data
        List<Participant> allPlayers = HandleCSV.loadParticipants();

        if (allPlayers.isEmpty()) {
            System.out.println("No participants found.");
            return;
        }

        Collections.shuffle(allPlayers);

        List<Team> formedTeams = new ArrayList<>();
        int teamCounter = 1;
        boolean canFormMoreTeams = true;

        // 2. Build Teams Loop
        while (canFormMoreTeams && !allPlayers.isEmpty()) {
            Team currentTeam = new Team(teamCounter);

            // --- STEP 1: FIND A LEADER (Mandatory) ---
            Participant leader = findParticipant(allPlayers, currentTeam, "Leader");

            if (leader == null) {
                // If no Leader is available, we cannot start a team. Stop.
                canFormMoreTeams = false;
                break;
            } else {
                currentTeam.members.add(leader);
                allPlayers.remove(leader);
            }

            // --- STEP 2: FIND THINKERS (Priority 2) ---
            // If teamSize is small (<4), we only fill up to the limit.
            int thinkersNeeded = 0;
            if (teamSize >= 2) thinkersNeeded++;
            if (teamSize >= 3) thinkersNeeded++;

            for (int i = 0; i < thinkersNeeded; i++) {
                // Stop trying to add thinkers if we already reached team size (e.g. if size is 2)
                if (currentTeam.members.size() >= teamSize) break;

                Participant thinker = findParticipant(allPlayers, currentTeam, "Thinker");
                if (thinker != null) {
                    currentTeam.members.add(thinker);
                    allPlayers.remove(thinker);
                }
            }

            // --- STEP 3: FILL REST WITH BALANCED (Priority 3) ---
            while (currentTeam.members.size() < teamSize) {
                Participant balanced = findParticipant(allPlayers, currentTeam, "Balanced");

                if (balanced != null) {
                    currentTeam.members.add(balanced);
                    allPlayers.remove(balanced);
                } else {
                    // Ran out of Balanced players to fill the slots
                    break;
                }
            }

            // --- STEP 4: STRICT SIZE CHECK ---
            // This ensures we don't accept incomplete teams (e.g. just a leader)
            if (currentTeam.members.size() == teamSize) {
                formedTeams.add(currentTeam);
                LOGGER.info("Formed Team " + teamCounter);
                teamCounter++;
            } else {
                System.out.println("Could not fill Team " + teamCounter + " (Size: " + currentTeam.members.size() + "/" + teamSize + "). Stopping.");
                // Return the members of this failed team back to the pool so they are saved as leftovers
                allPlayers.addAll(currentTeam.members);
                LOGGER.warning("Could not fill Team " + teamCounter + ". Disbanding.");
                canFormMoreTeams = false; // Stop the main loop
            }
        }

        LOGGER.info("Process finished. Total teams formed: " + formedTeams.size());
        saveLeftovers(allPlayers);

        // 3. Output Results
        displayTeams(formedTeams);

        // 4. Save Leftovers to CSV
        saveLeftovers(allPlayers);
    }

    // Helper: Find a specific type of participant that fits criteria
    private static Participant findParticipant(List<Participant> sourceList, Team team, String requiredType) {
        for (Participant p : sourceList) {
            // Check if Type matches
            if (p.getPersonalityType().equalsIgnoreCase(requiredType)) {
                // Check if Game/Skill criteria match
                if (fitsCriteria(team, p)) {
                    return p;
                }
            }
        }
        return null; // None found
    }

    // Helper: Check Game constraints and Skill Balance
    private static boolean fitsCriteria(Team team, Participant p) {

        // "Max 2 from same game per team"
        if (team.countGame(p.getGame()) >= 2) {
            return false;
        }

        // "Avoid stacking all high-skill players"
        // If team average is already High (>8) and new player is High (>8), reject.
        if (!team.members.isEmpty()) {
            double currentAvg = team.getAverageSkill();
            int pSkill = Integer.parseInt(p.getSkillLevel());
            if (currentAvg > 8.0 && pSkill > 8) {
                return false;
            }
        }

        return true;
    }

    private static void displayTeams(List<Team> teams) {
        System.out.println("\n--- TEAMS CREATED ---");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("formed_teams.csv"))) {
            bw.write("TeamID,Role,Name,Game,Skill,PersonalityType");
            bw.newLine();

            for (Team t : teams) {
                System.out.println("Team " + t.teamId + " (Avg Skill: " + String.format("%.2f", t.getAverageSkill()) + ")");
                for (Participant p : t.members) {
                    // Print to Console
                    System.out.println(" - " + p.getPersonalityType() + " | " + p.getName() +
                            " | " + p.getGame() + " (" + p.getSkillLevel() + ")");

                    // Write to CSV
                    String csvLine = t.teamId + "," + p.getRole() + "," + p.getName() + "," +
                            p.getGame() + "," + p.getSkillLevel() + "," + p.getPersonalityType();
                    bw.write(csvLine);
                    bw.newLine();
                }
                System.out.println("---------------------");
            }
            System.out.println("Teams saved to 'formed_teams.csv'");
        } catch (IOException e) {
            System.out.println("Error saving formed teams: " + e.getMessage());
        }
    }

    private static void saveLeftovers(List<Participant> leftovers) {
        if (leftovers.isEmpty()) {
            LOGGER.info("Saving " + leftovers.size() + " leftovers to CSV.");
            System.out.println("All participants were assigned to teams!");
            return;
        }

        System.out.println("\n" + leftovers.size() + " participants could not be assigned (Leftovers).");
        System.out.println("Saving leftovers to leftovers.csv...");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("leftovers.csv"))) {
            // Write Header
            bw.write("ID,Name,Email,Game,Skill,Role,PersonalityScore,PersonalityType");
            bw.newLine();

            int idCounter = 1;
            for (Participant p : leftovers) {
                // Generate a temporary ID for the leftover file
                String line = "LO" + idCounter + "," + p.toCSVLine();
                bw.write(line);
                bw.newLine();
                idCounter++;
            }
            System.out.println("Leftovers saved successfully.");

        } catch (IOException e) {
            LOGGER.severe("Failed to save leftovers: " + e.getMessage());
            System.out.println("Error saving leftovers: " + e.getMessage());
        }
    }
}