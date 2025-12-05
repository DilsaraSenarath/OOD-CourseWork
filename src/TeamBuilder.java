import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Core logic class responsible for forming teams based on algorithmic constraints.
 * It handles loading data, applying selection rules, and exporting results.
 */
public class TeamBuilder {
    private static final Logger LOGGER = Logger.getLogger(TeamBuilder.class.getName());

    /**
     * Internal helper class representing a single team entity.
     * Contains methods to calculate team statistics and validate composition.
     */
    static class Team implements CSVWritable {
        List<Participant> members = new ArrayList<>();
        int teamId;

        public Team(int id) {
            this.teamId = id;
        }

        @Override
        public String toCSVLine() {
            // Formats team summary data for CSV output
            return teamId + "," + getAverageSkill() + "," + members.size();
        }

        // Counts how many members play a specific game (used for diversity checks)
        public int countGame(String game) {
            int count = 0;
            for (Participant p : members) {
                if (p.getGame().equalsIgnoreCase(game)) count++;
            }
            return count;
        }

        // Calculates the average skill level of the current team members
        public double getAverageSkill() {
            if (members.isEmpty()) return 0;
            int sum = 0;
            for(Participant p : members) {
                sum += Integer.parseInt(p.getSkillLevel());
            }
            return (double) sum / members.size();
        }
    }

    /**
     * Main algorithm to generate teams of a specific size using personality and skill logic.
     * @param teamSize The target number of members per team.
     */
    public static void createTeams(int teamSize) {
        LOGGER.info("Starting team formation. Target size: " + teamSize);

        // 1. Load Data: Retrieve participants from all available CSV sources
        List<Participant> allPlayers = HandleCSV.loadParticipants();

        if (allPlayers.isEmpty()) {
            System.out.println("No participants found.");
            return;
        }

        // Randomize the list to ensure fairness in selection
        Collections.shuffle(allPlayers);

        List<Team> formedTeams = new ArrayList<>();
        int teamCounter = 1;
        boolean canFormMoreTeams = true;

        // 2. Main Allocation Loop: Attempt to build teams until pool is exhausted
        while (canFormMoreTeams && !allPlayers.isEmpty()) {
            Team currentTeam = new Team(teamCounter);

            // --- STEP 1: MANDATORY LEADER ---
            // Every team must start with one 'Leader' personality type
            Participant leader = findParticipant(allPlayers, currentTeam, "Leader");

            if (leader == null) {
                // Critical Failure: No leaders left, cannot form more valid teams
                canFormMoreTeams = false;
                break;
            } else {
                currentTeam.members.add(leader);
                allPlayers.remove(leader);
            }

            // --- STEP 2: STRATEGIC THINKERS ---
            // Determine how many 'Thinkers' are needed based on total team size
            int thinkersNeeded = 0;
            if (teamSize >= 2) thinkersNeeded++;
            if (teamSize >= 3) thinkersNeeded++;

            for (int i = 0; i < thinkersNeeded; i++) {
                // Prevent overfilling if team size is very small
                if (currentTeam.members.size() >= teamSize) break;

                Participant thinker = findParticipant(allPlayers, currentTeam, "Thinker");
                if (thinker != null) {
                    currentTeam.members.add(thinker);
                    allPlayers.remove(thinker);
                }
            }

            // --- STEP 3: FILL WITH BALANCED ---
            // Fill remaining slots with 'Balanced' personality types
            while (currentTeam.members.size() < teamSize) {
                Participant balanced = findParticipant(allPlayers, currentTeam, "Balanced");

                if (balanced != null) {
                    currentTeam.members.add(balanced);
                    allPlayers.remove(balanced);
                } else {
                    // Pool of 'Balanced' players is exhausted
                    break;
                }
            }

            // --- STEP 4: FINAL VALIDATION ---
            // Ensure the team is fully staffed before accepting it
            if (currentTeam.members.size() == teamSize) {
                formedTeams.add(currentTeam);
                LOGGER.info("Formed Team " + teamCounter);
                teamCounter++;
            } else {
                System.out.println("Could not fill Team " + teamCounter + " (Size: " + currentTeam.members.size() + "/" + teamSize + "). Stopping.");

                // Rollback: Return members of the failed team to the main pool
                allPlayers.addAll(currentTeam.members);
                LOGGER.warning("Could not fill Team " + teamCounter + ". Disbanding.");
                canFormMoreTeams = false;
            }
        }

        LOGGER.info("Process finished. Total teams formed: " + formedTeams.size());

        // 3. Export Results: Display to console and save to CSV
        displayTeams(formedTeams);

        // 4. Handle Leftovers: Save unassigned participants to a separate file
        saveLeftovers(allPlayers);
    }

    /**
     * Helper method to find a participant that matches the required personality
     * and fits the current team's balance criteria.
     */
    private static Participant findParticipant(List<Participant> sourceList, Team team, String requiredType) {
        for (Participant p : sourceList) {
            // Match Personality Type
            if (p.getPersonalityType().equalsIgnoreCase(requiredType)) {
                // Match Game constraints and Skill constraints
                if (fitsCriteria(team, p)) {
                    return p;
                }
            }
        }
        return null; // No matching participant found
    }

    /**
     * Validates if a participant matches the game diversity and skill rules.
     */
    private static boolean fitsCriteria(Team team, Participant p) {

        // Rule: A team cannot have more than 2 players of the exact same game preference
        if (team.countGame(p.getGame()) >= 2) {
            return false;
        }

        // Rule: Skill Balancing
        // If the team is already highly skilled (Avg > 8), do not add another high-skill player (> 8)
        if (!team.members.isEmpty()) {
            double currentAvg = team.getAverageSkill();
            int pSkill = Integer.parseInt(p.getSkillLevel());
            if (currentAvg > 8.0 && pSkill > 8) {
                return false;
            }
        }

        return true;
    }

    /**
     * Output method: Prints team details to console and writes to "formed_teams.csv".
     */
    private static void displayTeams(List<Team> teams) {
        System.out.println("\n--- TEAMS CREATED ---");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("formed_teams.csv"))) {
            // Write CSV Header
            bw.write("TeamID,Role,Name,Game,Skill,PersonalityType");
            bw.newLine();

            for (Team t : teams) {
                System.out.println("Team " + t.teamId + " (Avg Skill: " + String.format("%.2f", t.getAverageSkill()) + ")");
                for (Participant p : t.members) {
                    // Console Output
                    System.out.println(" - " + p.getPersonalityType() + " | " + p.getName() +
                            " | " + p.getGame() + " (" + p.getSkillLevel() + ")");

                    // CSV Output
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

    /**
     * Handles participants who could not be placed into a team.
     * Saves them to "leftovers.csv" for future reference.
     */
    private static void saveLeftovers(List<Participant> leftovers) {
        if (leftovers.isEmpty()) {
            LOGGER.info("No leftovers. All participants assigned.");
            System.out.println("All participants were assigned to teams!");
            return;
        }

        System.out.println("\n" + leftovers.size() + " participants could not be assigned (Leftovers).");
        System.out.println("Saving leftovers to leftovers.csv...");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("leftovers.csv"))) {
            bw.write("ID,Name,Email,Game,Skill,Role,PersonalityScore,PersonalityType");
            bw.newLine();

            int idCounter = 1;
            for (Participant p : leftovers) {
                // Generate temporary ID and write to file
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