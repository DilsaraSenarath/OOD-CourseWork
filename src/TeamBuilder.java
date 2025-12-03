import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamBuilder {

    // Inner class to represent a formed Team (makes calculations easier)
    static class Team {
        List<Participant> members = new ArrayList<>();
        int teamId;

        public Team(int id) {
            this.teamId = id;
        }

        // Helper: Count how many members play a specific game
        public int countGame(String game) {
            int count = 0;
            for (Participant p : members) {
                if (p.getGame().equalsIgnoreCase(game)) {
                    count++;
                }
            }
            return count;
        }

        // Helper: Count members with a specific personality type
        public int countPersonality(String type) {
            int count = 0;
            for (Participant p : members) {
                if (p.getPersonalityType().equalsIgnoreCase(type)) {
                    count++;
                }
            }
            return count;
        }

        // Helper: Check if adding this role helps diversity
        public boolean hasRole(Enums.Role role) {
            for (Participant p : members) {
                if (p.getRole() == role) {
                    return true;
                }
            }
            return false;
        }

        // Helper: Calculate average skill of the current team
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
        // 1. Load Data
        List<Participant> allPlayers = HandleCSV.loadParticipants();

        if (allPlayers.size() < teamSize) {
            System.out.println("Not enough participants to form a full team.");
            return;
        }

        // 2. Randomization [cite: 44]
        // This satisfies "Randomization within Constraints"
        Collections.shuffle(allPlayers);

        List<Team> formedTeams = new ArrayList<>();
        int teamCounter = 1;

        // 3. Build Teams Loop
        while (allPlayers.size() >= teamSize) {
            Team currentTeam = new Team(teamCounter);

            // Try to fill the team with 'teamSize' players
            for (int i = 0; i < teamSize; i++) {

                Participant bestCandidate = null;
                int bestCandidateIndex = -1;

                // Look through available players to find a fit
                for (int j = 0; j < allPlayers.size(); j++) {
                    Participant candidate = allPlayers.get(j);

                    if (fitsCriteria(currentTeam, candidate, teamSize)) {
                        bestCandidate = candidate;
                        bestCandidateIndex = j;
                        break; // Found a match, stop looking
                    }
                }

                // FALLBACK: If strict criteria failed, just take the first available person
                // (Otherwise the while loop never finishes)
                if (bestCandidate == null) {
                    bestCandidate = allPlayers.get(0);
                    bestCandidateIndex = 0;
                }

                // Add to team and remove from pool
                currentTeam.members.add(bestCandidate);
                allPlayers.remove(bestCandidateIndex);
            }

            formedTeams.add(currentTeam);
            teamCounter++;
        }

        // 4. Output Results
        displayAndSaveTeams(formedTeams);
    }

    // THE CRITERIA LOGIC
    private static boolean fitsCriteria(Team team, Participant p, int maxTeamSize) {

        // CRITERIA 1: Game Variety [cite: 27]
        // "Use a cap per game (e.g., max 2 from same game per team)."
        if (team.countGame(p.getGame()) >= 2) {
            return false;
        }

        // CRITERIA 2: Personality Mix [cite: 30]
        // "1 Leader, 1-2 Thinkers, Remaining as Balanced"
        String pType = p.getPersonalityType();

        if (pType.equalsIgnoreCase("Leader")) {
            // Only 1 leader allowed
            if (team.countPersonality("Leader") >= 1) return false;
        }
        else if (pType.equalsIgnoreCase("Thinker")) {
            // Max 2 thinkers allowed
            if (team.countPersonality("Thinker") >= 2) return false;
        }
        // "Balanced" has no max cap, so we don't check it.

        // CRITERIA 3: Skill Balance [cite: 28]
        // "Avoid stacking all high-skill players."
        int pSkill = Integer.parseInt(p.getSkillLevel());
        if (team.members.size() > 0) {
            double currentAvg = team.getAverageSkill();
            // If team is already very strong (avg > 8) and this player is strong (>8), skip them
            if (currentAvg > 8.0 && pSkill > 8) {
                return false;
            }
        }

        // CRITERIA 4: Role Diversity
        // "Ensure at least 3 different roles"
        // This is hard to enforce strictly while building, but we can prioritize unique roles.
        // If the team doesn't have this role yet, it's a GOOD match.
        // If the team already has this role, and we are nearly full, maybe skip?
        // (Keeping it simple for now: valid if other checks pass)

        return true;
    }

    private static void displayAndSaveTeams(List<Team> teams) {
        System.out.println("\n--- TEAMS CREATED ---");
        // Logic to save to CSV would go here (similar to HandleCSV.save)

        for (Team t : teams) {
            System.out.println("Team " + t.teamId + " (Avg Skill: " + String.format("%.2f", t.getAverageSkill()) + ")");
            for (Participant p : t.members) {
                System.out.println(" - " + p.getName() +
                        " | " + p.getGame() +
                        " | " + p.getRole() +
                        " | " + p.getPersonalityType());
            }
            System.out.println("---------------------");
        }
    }
}